# Script de démo rapide - Disaster Aid Backend
# Usage: .\demo-quick.ps1

Write-Host "╔════════════════════════════════════════════════════════════════╗" -ForegroundColor Cyan
Write-Host "║   🎯 DISASTER AID BACKEND - DÉMO                             ║" -ForegroundColor Cyan
Write-Host "╚════════════════════════════════════════════════════════════════╝" -ForegroundColor Cyan

$BASE_URL = "http://localhost:8081"

# Vérification application
try {
    Invoke-RestMethod -Uri "$BASE_URL/actuator/health" | Out-Null
    Write-Host "✅ Application running on port 8081" -ForegroundColor Green
} catch {
    Write-Host "❌ Application not running. Start with: .\mvnw.cmd spring-boot:run" -ForegroundColor Red
    exit
}

Write-Host ""
Write-Host "═══════════════════════════════════════════════════════════════" -ForegroundColor Cyan
Write-Host "M13 ENDPOINTS - DISASTER ANALYTICS" -ForegroundColor Magenta
Write-Host "═══════════════════════════════════════════════════════════════" -ForegroundColor Cyan
Write-Host ""

Write-Host "1️⃣  GET /api/analytics/disaster/report" -ForegroundColor Yellow
Invoke-RestMethod "$BASE_URL/api/analytics/disaster/report" | ConvertTo-Json

Write-Host ""
Write-Host "2️⃣  GET /api/analytics/disaster/stats" -ForegroundColor Yellow
Invoke-RestMethod "$BASE_URL/api/analytics/disaster/stats" | ConvertTo-Json

Write-Host ""
Write-Host "═══════════════════════════════════════════════════════════════" -ForegroundColor Cyan
Write-Host "M14 ENDPOINTS - VOLUNTEER ANALYTICS" -ForegroundColor Magenta
Write-Host "═══════════════════════════════════════════════════════════════" -ForegroundColor Cyan
Write-Host ""

Write-Host "3️⃣  GET /api/analytics/volunteer/report?highRiskThreshold=0.70" -ForegroundColor Yellow
Invoke-RestMethod "$BASE_URL/api/analytics/volunteer/report?highRiskThreshold=0.70" | ConvertTo-Json

Write-Host ""
Write-Host "4️⃣  GET /api/analytics/volunteer/churn/high-risk?threshold=0.70" -ForegroundColor Yellow
Invoke-RestMethod "$BASE_URL/api/analytics/volunteer/churn/high-risk?threshold=0.70" | ConvertTo-Json

Write-Host ""
Write-Host "═══════════════════════════════════════════════════════════════" -ForegroundColor Green
Write-Host "✨ Demo completed successfully! ✨" -ForegroundColor Green
Write-Host "═══════════════════════════════════════════════════════════════" -ForegroundColor Green

