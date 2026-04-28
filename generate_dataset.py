import csv
import random
from pathlib import Path


PROFICIENCY_LEVELS = ["BEGINNER", "INTERMEDIATE", "ADVANCED", "EXPERT"]
LEVEL_TO_INT = {level: idx for idx, level in enumerate(PROFICIENCY_LEVELS)}


def weighted_choice(items):
    values, weights = zip(*items)
    return random.choices(values, weights=weights, k=1)[0]


def clamp(value, lower, upper):
    return max(lower, min(upper, value))


def generate_row():
    volunteer_id = random.randint(1, 550)
    opportunity_id = random.randint(1, 320)

    required_proficiency = weighted_choice(
        [
            ("BEGINNER", 0.25),
            ("INTERMEDIATE", 0.38),
            ("ADVANCED", 0.27),
            ("EXPERT", 0.10),
        ]
    )

    has_skill = random.random() < 0.78
    skill_match = has_skill

    if has_skill:
        req_idx = LEVEL_TO_INT[required_proficiency]
        delta = weighted_choice([(-1, 0.18), (0, 0.42), (1, 0.28), (2, 0.12)])
        vol_idx = clamp(req_idx + delta, 0, 3)
    else:
        vol_idx = random.randint(0, 3)

    volunteer_proficiency = PROFICIENCY_LEVELS[vol_idx]
    proficiency_gap = LEVEL_TO_INT[required_proficiency] - vol_idx

    endorsement_count = int(clamp(random.gauss(6.5, 4.5), 0, 20))
    is_verified = random.random() < (0.62 if has_skill else 0.25)
    volunteer_skills_count = int(clamp(random.gauss(5.5, 2.8), 1, 16))
    days_until_opportunity = int(clamp(random.gauss(14, 9), 1, 60))

    # --- NEW LOGIC (Properly Indented) ---
    score = -1.5  
    if skill_match:
        score += 0.10  
    if is_verified:
        score += 0.05  

    score += 0.15 * endorsement_count      
    score += 0.10 * volunteer_skills_count  
    score -= 0.10 * max(0, proficiency_gap)
    score -= 0.015 * days_until_opportunity
    # -------------------------------------

    acceptance_probability = 1 / (1 + pow(2.718281828, -score))
    status = "ACCEPTED" if random.random() < acceptance_probability else "REJECTED"

    return {
        "volunteer_id": volunteer_id,
        "opportunity_id": opportunity_id,
        "required_proficiency": required_proficiency,
        "volunteer_proficiency": volunteer_proficiency,
        "skill_match": skill_match,
        "proficiency_gap": proficiency_gap,
        "endorsement_count": endorsement_count,
        "is_verified": is_verified,
        "volunteer_skills_count": volunteer_skills_count,
        "days_until_opportunity": days_until_opportunity,
        "status": status,
    }

def main():
    random.seed(42)
    output_path = Path("applications.csv")
    row_count = 2500

    with output_path.open("w", newline="", encoding="utf-8") as file:
        writer = csv.DictWriter(
            file,
            fieldnames=[
                "volunteer_id",
                "opportunity_id",
                "required_proficiency",
                "volunteer_proficiency",
                "skill_match",
                "proficiency_gap",
                "endorsement_count",
                "is_verified",
                "volunteer_skills_count",
                "days_until_opportunity",
                "status",
            ],
        )
        writer.writeheader()
        for _ in range(row_count):
            writer.writerow(generate_row())

    print(f"Generated {row_count} rows at {output_path.resolve()}")


if __name__ == "__main__":
    main()
