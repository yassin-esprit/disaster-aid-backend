# Exemples de requêtes cURL - Disaster Aid Backend
# Copiez-collez directement dans PowerShell

$BASE_URL = "http://localhost:8081"

Write-Host "╔════════════════════════════════════════════════════════════════╗" -ForegroundColor Cyan
Write-Host "║   📡 EXEMPLES DE REQUÊTES - DISASTER AID BACKEND             ║" -ForegroundColor Cyan
Write-Host "╚════════════════════════════════════════════════════════════════╝" -ForegroundColor Cyan
Write-Host ""

# ═══════════════════════════════════════════════════════════════════════════════
# M13 - DISASTER ANALYTICS
# ═══════════════════════════════════════════════════════════════════════════════

Write-Host "🚨 M13 - DISASTER ANALYTICS" -ForegroundColor Magenta
Write-Host "════════════════════════════════════════════════════════════════" -ForegroundColor Magenta
Write-Host ""

# Endpoint 1
Write-Host "1️⃣  RAPPORT GLOBAL DES CATASTROPHES" -ForegroundColor Yellow
Write-Host "GET /api/analytics/disaster/report" -ForegroundColor Cyan
Write-Host ""
$result1 = Invoke-RestMethod -Uri "$BASE_URL/api/analytics/disaster/report" -Method GET
Write-Host ($result1 | ConvertTo-Json -Depth 5) -ForegroundColor White
Write-Host ""

# Endpoint 2
Write-Host "2️⃣  LISTE DE TOUTES LES STATISTIQUES" -ForegroundColor Yellow
Write-Host "GET /api/analytics/disaster/stats" -ForegroundColor Cyan
Write-Host ""
$result2 = Invoke-RestMethod -Uri "$BASE_URL/api/analytics/disaster/stats" -Method GET
Write-Host ($result2 | ConvertTo-Json -Depth 5) -ForegroundColor White
Write-Host ""

# Endpoint 3
Write-Host "3️⃣  STATISTIQUES D'UNE ALERTE SPÉCIFIQUE" -ForegroundColor Yellow
Write-Host "GET /api/analytics/disaster/stats/alert/101" -ForegroundColor Cyan
Write-Host ""
try {
    $result3 = Invoke-RestMethod -Uri "$BASE_URL/api/analytics/disaster/stats/alert/101" -Method GET
    Write-Host ($result3 | ConvertTo-Json -Depth 5) -ForegroundColor White
} catch {
    Write-Host "Alert 101 not found - Trying alert 1..." -ForegroundColor Yellow
    try {
        $result3 = Invoke-RestMethod -Uri "$BASE_URL/api/analytics/disaster/stats/alert/1" -Method GET
        Write-Host ($result3 | ConvertTo-Json -Depth 5) -ForegroundColor White
    } catch {
        Write-Host "No alerts found in database" -ForegroundColor Gray
    }
}
Write-Host ""

# Endpoint 4
Write-Host "4️⃣  DERNIÈRE STATISTIQUE D'UNE ALERTE" -ForegroundColor Yellow
Write-Host "GET /api/analytics/disaster/stats/alert/101/latest" -ForegroundColor Cyan
Write-Host ""
try {
    $result4 = Invoke-RestMethod -Uri "$BASE_URL/api/analytics/disaster/stats/alert/101/latest" -Method GET
    Write-Host ($result4 | ConvertTo-Json -Depth 5) -ForegroundColor White
} catch {
    Write-Host "(Endpoint optionnel - peut ne pas exister)" -ForegroundColor Gray
}
Write-Host ""

# Endpoint 5
Write-Host "5️⃣  LOGS D'AUDIT (TRAÇABILITÉ)" -ForegroundColor Yellow
Write-Host "GET /api/analytics/disaster/audit-logs" -ForegroundColor Cyan
Write-Host ""
try {
    $result5 = Invoke-RestMethod -Uri "$BASE_URL/api/analytics/disaster/audit-logs" -Method GET
    Write-Host ($result5 | ConvertTo-Json -Depth 3) -ForegroundColor White
} catch {
    Write-Host "No audit logs found" -ForegroundColor Gray
}
Write-Host ""

# Endpoint 6
Write-Host "6️⃣  MODÈLES PRÉDICTIFS ACTIFS" -ForegroundColor Yellow
Write-Host "GET /api/analytics/disaster/predictive-models?activeOnly=true" -ForegroundColor Cyan
Write-Host ""
try {
    $result6 = Invoke-RestMethod -Uri "$BASE_URL/api/analytics/disaster/predictive-models?activeOnly=true" -Method GET
    Write-Host ($result6 | ConvertTo-Json -Depth 5) -ForegroundColor White
} catch {
    Write-Host "No predictive models found" -ForegroundColor Gray
}
Write-Host ""

# ═══════════════════════════════════════════════════════════════════════════════
# M14 - VOLUNTEER ANALYTICS
# ═══════════════════════════════════════════════════════════════════════════════

Write-Host "👥 M14 - VOLUNTEER ANALYTICS" -ForegroundColor Magenta
Write-Host "════════════════════════════════════════════════════════════════" -ForegroundColor Magenta
Write-Host ""

# Endpoint 1
Write-Host "1️⃣  RAPPORT GLOBAL DES VOLONTAIRES" -ForegroundColor Yellow
Write-Host "GET /api/analytics/volunteer/report?highRiskThreshold=0.70" -ForegroundColor Cyan
Write-Host ""
$result7 = Invoke-RestMethod -Uri "$BASE_URL/api/analytics/volunteer/report?highRiskThreshold=0.70" -Method GET
Write-Host ($result7 | ConvertTo-Json -Depth 5) -ForegroundColor White
Write-Host ""

# Endpoint 2
Write-Host "2️⃣  STATISTIQUES D'UN VOLONTAIRE" -ForegroundColor Yellow
Write-Host "GET /api/analytics/volunteer/stats/1" -ForegroundColor Cyan
Write-Host ""
try {
    $result8 = Invoke-RestMethod -Uri "$BASE_URL/api/analytics/volunteer/stats/1" -Method GET
    Write-Host ($result8 | ConvertTo-Json -Depth 5) -ForegroundColor White
} catch {
    Write-Host "Volunteer 1 not found" -ForegroundColor Gray
}
Write-Host ""

# Endpoint 3
Write-Host "3️⃣  STATISTIQUES D'UNE ORGANISATION" -ForegroundColor Yellow
Write-Host "GET /api/analytics/volunteer/organisations/1" -ForegroundColor Cyan
Write-Host ""
try {
    $result9 = Invoke-RestMethod -Uri "$BASE_URL/api/analytics/volunteer/organisations/1" -Method GET
    Write-Host ($result9 | ConvertTo-Json -Depth 5) -ForegroundColor White
} catch {
    Write-Host "Organisation 1 not found" -ForegroundColor Gray
}
Write-Host ""

# Endpoint 4
Write-Host "4️⃣  VOLONTAIRES À HAUT RISQUE DE DÉPART" -ForegroundColor Yellow
Write-Host "GET /api/analytics/volunteer/churn/high-risk?threshold=0.70" -ForegroundColor Cyan
Write-Host ""
try {
    $result10 = Invoke-RestMethod -Uri "$BASE_URL/api/analytics/volunteer/churn/high-risk?threshold=0.70" -Method GET
    Write-Host ($result10 | ConvertTo-Json -Depth 5) -ForegroundColor White
} catch {
    Write-Host "No high-risk predictions found" -ForegroundColor Gray
}
Write-Host ""

# Endpoint 5
Write-Host "5️⃣  PRÉDICTION DE CHURN POUR UN VOLONTAIRE" -ForegroundColor Yellow
Write-Host "GET /api/analytics/volunteer/churn/volunteer/1" -ForegroundColor Cyan
Write-Host ""
try {
    $result11 = Invoke-RestMethod -Uri "$BASE_URL/api/analytics/volunteer/churn/volunteer/1" -Method GET
    Write-Host ($result11 | ConvertTo-Json -Depth 5) -ForegroundColor White
} catch {
    Write-Host "No churn prediction found for volunteer 1" -ForegroundColor Gray
}
Write-Host ""

# ═══════════════════════════════════════════════════════════════════════════════
# BONUS - INFORMATION SYSTÈME
# ═══════════════════════════════════════════════════════════════════════════════

Write-Host "ℹ️  INFORMATION SYSTÈME" -ForegroundColor Cyan
Write-Host "════════════════════════════════════════════════════════════════" -ForegroundColor Cyan
Write-Host ""

Write-Host "Health Check:" -ForegroundColor Yellow
try {
    $health = Invoke-RestMethod -Uri "$BASE_URL/actuator/health" -Method GET
    Write-Host ($health | ConvertTo-Json) -ForegroundColor White
} catch {
    Write-Host "Health endpoint not available" -ForegroundColor Gray
}

Write-Host ""
Write-Host "╔════════════════════════════════════════════════════════════════╗" -ForegroundColor Green
Write-Host "║   ✅ Tous les endpoints testés ! Prêt pour la présentation !  ║" -ForegroundColor Green
Write-Host "╚════════════════════════════════════════════════════════════════╝" -ForegroundColor Green

