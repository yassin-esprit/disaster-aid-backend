from __future__ import annotations

from dataclasses import dataclass
from typing import Dict, List, Optional
import re


START_MESSAGE = (
    "I can help. Tell me: what type of disaster is happening "
    "(flood/fire/earthquake/storm/power outage) and are you safe now? "
    "Are there any injuries?"
)

SAFETY_REMINDER = (
    "Safety reminder: human life comes first. "
    "If you are unsure, choose the safest option and call emergency services."
)


SITUATION_KEYWORDS: Dict[str, List[str]] = {
    "flood": ["flood", "water rising", "flash flood", "inondation", "eau monte", "crue"],
    "fire": ["fire", "smoke", "flame", "incendie", "fumee", "feu"],
    "earthquake": ["earthquake", "quake", "tremor", "seisme", "tremblement", "secousse"],
    "storm": ["storm", "cyclone", "hurricane", "tempete", "orage", "vent violent"],
    "power_outage": ["power outage", "blackout", "no power", "coupure", "panne electrique"],
}

LIFE_THREAT_KEYWORDS = [
    "unconscious",
    "not breathing",
    "cannot breathe",
    "difficulty breathing",
    "chest pain",
    "heavy bleeding",
    "electrocution",
    "child in danger",
    "trapped",
    "thick smoke",
    "gas leak",
    "inconscient",
    "ne respire",
    "respire mal",
    "douleur thoracique",
    "saignement important",
    "fumee epaisse",
    "odeur de gaz",
]

YES_WORDS = {"yes", "y", "ok", "sure", "oui", "ouais"}
NO_WORDS = {"no", "n", "not", "non", "pas"}


@dataclass
class TriageState:
    location_and_danger: Optional[str] = None
    has_injured: Optional[bool] = None
    breathing_or_conscious_issue: Optional[bool] = None
    is_safe_now: Optional[bool] = None
    water_rising: Optional[bool] = None
    wet_electric_cables: Optional[bool] = None
    gas_smell: Optional[bool] = None
    thick_smoke: Optional[bool] = None
    people_count: Optional[int] = None
    vulnerable_people: Optional[bool] = None
    can_evacuate: Optional[bool] = None
    safe_exit_known: Optional[bool] = None
    situation_type: Optional[str] = None

    def to_dict(self) -> Dict[str, Optional[object]]:
        return self.__dict__.copy()

    @classmethod
    def from_dict(cls, data: Optional[Dict[str, object]]) -> "TriageState":
        if not data:
            return cls()
        obj = cls()
        for key, value in data.items():
            if hasattr(obj, key):
                setattr(obj, key, value)
        return obj


class CpsSafeChatbot:
    """Rule-based emergency assistant specialized for disaster scenarios."""

    def start_message(self) -> str:
        return START_MESSAGE

    def respond(self, user_text: str, state_data: Optional[Dict[str, object]] = None) -> Dict[str, object]:
        state = TriageState.from_dict(state_data)
        text = self._normalize(user_text)

        if self._looks_like_first_turn(text):
            return {"reply": self.start_message(), "state": state.to_dict()}

        self._update_state_from_text(state, text)

        if self._is_life_threatening(state, text):
            return {"reply": self._build_life_threat_response(state), "state": state.to_dict()}

        critical_missing = self._critical_missing_questions(state)
        if critical_missing:
            return {"reply": self._build_clarification_reply(critical_missing), "state": state.to_dict()}

        return {"reply": self._build_plan_reply(state), "state": state.to_dict()}

    def _normalize(self, text: str) -> str:
        return re.sub(r"\s+", " ", text.strip().lower())

    def _looks_like_first_turn(self, text: str) -> bool:
        return text in {"", "hello", "hi", "help", "bonjour", "salut", "aide"}

    def _contains_any(self, text: str, keywords: List[str]) -> bool:
        return any(k in text for k in keywords)

    def _parse_yes_no(self, text: str, hint: str) -> Optional[bool]:
        if hint not in text:
            return None
        tokens = set(re.findall(r"[a-zA-Z]+", text))
        if tokens & YES_WORDS:
            return True
        if tokens & NO_WORDS:
            return False
        return None

    def _extract_people_count(self, text: str) -> Optional[int]:
        match = re.search(r"\b(\d{1,2})\b", text)
        if not match:
            return None
        try:
            return int(match.group(1))
        except ValueError:
            return None

    def _update_state_from_text(self, state: TriageState, text: str) -> None:
        for situation, keys in SITUATION_KEYWORDS.items():
            if self._contains_any(text, keys):
                state.situation_type = situation

        if state.location_and_danger is None and text:
            if any(word in text for word in ["flood", "fire", "smoke", "gas", "electric", "quake", "storm", "outage", "inond", "incend", "fumee", "gaz", "seisme"]):
                state.location_and_danger = text

        if any(k in text for k in ["injured", "bleeding", "burn", "bless", "blesse", "saignement", "brulure"]):
            state.has_injured = not any(n in text for n in ["no", "none", "not", "pas", "aucun", "non"])

        if any(k in text for k in ["unconscious", "not breathing", "cannot breathe", "difficulty breathing", "inconscient", "ne respire", "respire mal"]):
            state.breathing_or_conscious_issue = True

        safe_flag = self._parse_yes_no(text, "safe")
        if safe_flag is not None:
            state.is_safe_now = safe_flag
        fr_safe_flag = self._parse_yes_no(text, "secur")
        if fr_safe_flag is not None:
            state.is_safe_now = fr_safe_flag

        if ("water" in text and any(k in text for k in ["rising", "rise"])) or (
            "eau" in text and any(k in text for k in ["monte", "monter", "rapid"])
        ):
            state.water_rising = True

        if ("cable" in text or "wire" in text) and any(k in text for k in ["wet", "water", "mouille", "eau", "nu"]):
            state.wet_electric_cables = True

        if any(k in text for k in ["gas smell", "smell gas", "gas leak", "odeur de gaz", "fuite gaz"]):
            state.gas_smell = True

        if any(k in text for k in ["thick smoke", "heavy smoke", "fumee epaisse"]):
            state.thick_smoke = True

        count = self._extract_people_count(text)
        if count is not None:
            state.people_count = count

        if any(k in text for k in ["child", "baby", "elderly", "disabled", "enfant", "bebe", "personne agee", "handicap"]):
            state.vulnerable_people = True

        if any(k in text for k in ["evacu", "sortie", "exit"]):
            if any(n in text for n in ["impossible", "blocked", "cannot", "bloque", "non"]):
                state.can_evacuate = False
            elif any(y in text for y in ["possible", "can", "yes", "oui", "ok"]):
                state.can_evacuate = True

        if any(k in text for k in ["safe exit", "sortie sure", "exit clear", "sortie libre"]):
            state.safe_exit_known = True

    def _is_life_threatening(self, state: TriageState, text: str) -> bool:
        if self._contains_any(text, LIFE_THREAT_KEYWORDS):
            return True
        if state.is_safe_now is False:
            return True
        if state.breathing_or_conscious_issue:
            return True
        if state.water_rising and (state.wet_electric_cables or state.gas_smell):
            return True
        if state.thick_smoke:
            return True
        return False

    def _critical_missing_questions(self, state: TriageState) -> List[str]:
        questions: List[str] = []

        if not state.location_and_danger:
            questions.append("Where are you and what exact hazard is present (flood/fire/smoke/gas/electricity/injury)?")
        if state.has_injured is None:
            questions.append("Are there injured people? Approximate age? Are they breathing and conscious?")
        if state.is_safe_now is None:
            questions.append("Are you safe right now (yes/no)?")
        if state.water_rising is None or state.wet_electric_cables is None or state.gas_smell is None or state.thick_smoke is None:
            questions.append("Is water rising? Any wet electrical cables? Gas smell? Thick smoke?")
        if state.people_count is None or state.vulnerable_people is None:
            questions.append("How many people are with you? Any children, elderly, or disabled persons?")
        if state.can_evacuate is None or state.safe_exit_known is None:
            questions.append("Can you evacuate now? Which exit is safe?")

        return questions[:3]

    def _build_clarification_reply(self, questions: List[str]) -> str:
        lines = ["I can help. Please answer these priority questions quickly:"]
        for q in questions:
            lines.append(f"- {q}")
        lines.append(SAFETY_REMINDER)
        return "\n".join(lines)

    def _build_life_threat_response(self, state: TriageState) -> str:
        steps = [
            "Call emergency services immediately (local emergency number).",
            "Do right now (0-2 min):",
            "- Move away from immediate danger and evacuate if a safe exit exists.",
            "- Do not use elevators.",
            "- If there is smoke: stay low and crawl under it.",
            "- If there is water and electricity risk: do not touch wet cables or devices.",
        ]

        if state.can_evacuate is False:
            steps.extend(
                [
                    "In the next 10-30 minutes:",
                    "- If exit is blocked, shelter near a window and signal your presence.",
                    "- Close the door (without locking) to slow smoke/heat if there is fire.",
                ]
            )
        else:
            steps.extend(
                [
                    "In the next 10-30 minutes:",
                    "- Move to a safe area: higher ground for flood, open air for fire/smoke.",
                    "- Prioritize vulnerable people and do a quick headcount.",
                ]
            )

        steps.extend(
            [
                "After / once stable:",
                "- Share your exact location with emergency responders and follow instructions.",
                "- Give basic first aid only if it is safe for you.",
                SAFETY_REMINDER,
            ]
        )
        return "\n".join(steps)

    def _build_plan_reply(self, state: TriageState) -> str:
        immediate, medium, later = self._scenario_steps(state)
        lines = [
            "Do right now (0-2 min):",
            *[f"- {s}" for s in immediate],
            "In the next 10-30 minutes:",
            *[f"- {s}" for s in medium],
            "After / once stable:",
            *[f"- {s}" for s in later],
            SAFETY_REMINDER,
        ]
        return "\n".join(lines)

    def _scenario_steps(self, state: TriageState) -> tuple[list[str], list[str], list[str]]:
        situation = state.situation_type

        if situation == "flood":
            immediate = [
                "Move to higher ground and avoid basements.",
                "Do not walk through water if there is current or electrical risk.",
                "Turn off power only if the breaker is reachable without crossing water.",
            ]
            medium = [
                "Prepare a go-bag: ID, medications, water, charger, flashlight.",
                "Shut off gas if a leak is suspected and it is safe to do so.",
                "Evacuate as soon as a safe route is confirmed.",
            ]
            later = [
                "Have electrical systems checked before restoring power.",
                "Use protection (gloves, mask) for cleanup and discard contaminated food.",
                "Protect property only after all people are safe.",
            ]
            return immediate, medium, later

        if situation == "fire":
            immediate = [
                "Evacuate immediately if fire or smoke is spreading.",
                "Stay low under smoke and close doors behind you if possible.",
                "Do not use elevators.",
            ]
            medium = [
                "If exit is blocked, shelter near a window and signal your location.",
                "If clothing catches fire: stop, drop, and roll.",
                "Watch for breathing problems and call emergency services.",
            ]
            later = [
                "Do not re-enter the building until emergency services approve.",
                "Have indoor air and structural safety assessed by professionals.",
                "Contact relatives/shelter for temporary accommodation if needed.",
            ]
            return immediate, medium, later

        if situation == "earthquake":
            immediate = [
                "During shaking: drop, cover, and hold on under sturdy furniture.",
                "Stay away from windows and objects that may fall.",
                "If outside, move away from buildings and power lines.",
            ]
            medium = [
                "After shaking stops, evacuate calmly to an open area.",
                "Shut off gas/electricity only if a leak is suspected and access is safe.",
                "Prepare for aftershocks and keep shoes/flashlight nearby.",
            ]
            later = [
                "Check injuries and call emergency services for serious symptoms.",
                "Avoid damaged buildings until inspected.",
                "Keep water, food, radio, and charged phone available.",
            ]
            return immediate, medium, later

        if situation == "power_outage":
            immediate = [
                "Check people first (fall risk, temperature exposure, medical devices).",
                "Unplug sensitive electronics to reduce surge risk when power returns.",
                "Use flashlights instead of candles when possible.",
            ]
            medium = [
                "Keep fridge/freezer closed to preserve cold.",
                "Charge phones using power banks if available.",
                "Check vulnerable neighbors if it is safe.",
            ]
            later = [
                "Restore appliances gradually when power returns.",
                "Discard perishable food kept too long at unsafe temperature.",
                "Report burning smell or persistent faults to a professional.",
            ]
            return immediate, medium, later

        immediate = [
            "Move everyone away from immediate danger.",
            "Call emergency services if there is any possible life risk.",
            "Avoid contact with water/electricity/gas/smoke until risk is cleared.",
        ]
        medium = [
            "Quickly assess injuries, breathing, and consciousness.",
            "Prioritize vulnerable people and prepare for evacuation.",
            "Keep phones charged and important documents ready.",
        ]
        later = [
            "Follow official local instructions.",
            "Do not re-enter hazardous areas without authorization.",
            "Handle property damage only when human safety is fully secured.",
        ]
        return immediate, medium, later


def run_terminal_chat() -> None:
    bot = CpsSafeChatbot()
    state: Dict[str, object] = {}

    print("CPS-SAFE (terminal mode)\n")
    print(bot.start_message())
    print("Type 'quit' to exit.\n")

    while True:
        user = input("You: ").strip()
        if user.lower() in {"quit", "exit", "q"}:
            print("CPS-SAFE: Stay safe. Goodbye.")
            break

        result = bot.respond(user, state)
        state = result["state"]
        print("\nCPS-SAFE:")
        print(result["reply"])
        print()


if __name__ == "__main__":
    run_terminal_chat()
