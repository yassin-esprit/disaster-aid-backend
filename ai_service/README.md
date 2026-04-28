# AI Service (local batch)

Ce projet genere un rapport PDF a partir des donnees PostgreSQL.

## Prerequis
- Python 3.10+
- Acces a la base PostgreSQL du backend

## Installation
```powershell
cd C:\Users\21658\Documents\GitHub\disaster-aid-backend\ai_service
python -m venv .venv
.\.venv\Scripts\Activate.ps1
python -m pip install --upgrade pip
pip install -r requirements.txt
```

## Configuration
Creez un fichier `.env` a cote de `requirements.txt`:
```
DB_HOST=localhost
DB_PORT=5432
DB_NAME=disaster_aid
DB_USER=postgres
DB_PASSWORD=postgres
OUTPUT_DIR=ai_reports
```

## Execution
```powershell
python -m app.main
```

## Tests
```powershell
python -m unittest discover -s tests
```

