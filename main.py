import json
from pathlib import Path
from typing import Literal

import joblib
import pandas as pd
from fastapi import FastAPI, HTTPException
from fastapi.middleware.cors import CORSMiddleware
from pydantic import BaseModel


PROF_MAP = {
    "BEGINNER": 0,
    "INTERMEDIATE": 1,
    "ADVANCED": 2,
    "EXPERT": 3,
}

MODEL_PATH = Path("model.pkl")
FEATURES_PATH = Path("feature_columns.json")

app = FastAPI(title="Volunteer Application Prediction API")

app.add_middleware(
    CORSMiddleware,
    allow_origins=["http://localhost:4200"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)


class PredictionRequest(BaseModel):
    volunteer_id: int
    opportunity_id: int
    required_proficiency: Literal["BEGINNER", "INTERMEDIATE", "ADVANCED", "EXPERT"]
    volunteer_proficiency: Literal["BEGINNER", "INTERMEDIATE", "ADVANCED", "EXPERT"]
    skill_match: bool
    proficiency_gap: int
    endorsement_count: int
    is_verified: bool
    volunteer_skills_count: int
    days_until_opportunity: int


class PredictionResponse(BaseModel):
    prediction: Literal["ACCEPTED", "REJECTED"]
    probability: float


model = None
feature_columns = []


@app.on_event("startup")
def startup_event():
    global model
    global feature_columns

    if not MODEL_PATH.exists() or not FEATURES_PATH.exists():
        raise RuntimeError(
            "model.pkl or feature_columns.json missing. Run generate_dataset.py and train_model.py first."
        )

    model = joblib.load(MODEL_PATH)
    with FEATURES_PATH.open("r", encoding="utf-8") as file:
        feature_columns = json.load(file)


@app.get("/health")
def health():
    return {"status": "ok"}


@app.post("/predict", response_model=PredictionResponse)
def predict(payload: PredictionRequest):
    if model is None or not feature_columns:
        raise HTTPException(status_code=500, detail="Model is not loaded.")

    data = payload.model_dump()
    data["required_proficiency"] = PROF_MAP[data["required_proficiency"]]
    data["volunteer_proficiency"] = PROF_MAP[data["volunteer_proficiency"]]
    data["skill_match"] = int(data["skill_match"])
    data["is_verified"] = int(data["is_verified"])

    try:
        input_df = pd.DataFrame([[data[column] for column in feature_columns]], columns=feature_columns)
        prediction = model.predict(input_df)[0]
        probabilities = model.predict_proba(input_df)[0]
        classes = list(model.classes_)
        predicted_index = classes.index(prediction)
        probability = float(probabilities[predicted_index])
    except Exception as exc:
        raise HTTPException(status_code=400, detail=f"Prediction failed: {exc}") from exc

    return PredictionResponse(prediction=prediction, probability=probability)
