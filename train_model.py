import json
from pathlib import Path

import joblib
import pandas as pd
from sklearn.ensemble import RandomForestClassifier
from sklearn.metrics import accuracy_score, classification_report
from sklearn.model_selection import train_test_split


PROF_MAP = {
    "BEGINNER": 0,
    "INTERMEDIATE": 1,
    "ADVANCED": 2,
    "EXPERT": 3,
}

FEATURE_COLUMNS = [
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
]


def preprocess(df: pd.DataFrame) -> pd.DataFrame:
    processed = df.copy()
    processed["required_proficiency"] = processed["required_proficiency"].map(PROF_MAP)
    processed["volunteer_proficiency"] = processed["volunteer_proficiency"].map(PROF_MAP)
    processed["skill_match"] = processed["skill_match"].astype(int)
    processed["is_verified"] = processed["is_verified"].astype(int)
    return processed


def main():
    data_path = Path("applications.csv")
    if not data_path.exists():
        raise FileNotFoundError("applications.csv not found. Run generate_dataset.py first.")

    df = pd.read_csv(data_path)
    df = preprocess(df)

    x = df[FEATURE_COLUMNS]
    y = df["status"]

    x_train, x_test, y_train, y_test = train_test_split(
        x,
        y,
        test_size=0.2,
        random_state=42,
        stratify=y,
    )

    model = RandomForestClassifier(
        n_estimators=300,
        max_depth=12,
        min_samples_leaf=2,
        random_state=42,
    )
    model.fit(x_train, y_train)

    y_pred = model.predict(x_test)
    accuracy = accuracy_score(y_test, y_pred)
    print(f"Accuracy: {accuracy:.4f}")
    print("Classification Report:")
    print(classification_report(y_test, y_pred))

    joblib.dump(model, "model.pkl")
    with Path("feature_columns.json").open("w", encoding="utf-8") as file:
        json.dump(FEATURE_COLUMNS, file, indent=2)

    print("Saved model.pkl and feature_columns.json")


if __name__ == "__main__":
    main()
