from fastapi import APIRouter, HTTPException, Query
from typing import Optional
# IMPORTANTE: Importamos APENAS a classe. 
# Remova as funções get_all_cities_analytics e get_traffic_heatmap do import.
from services.analytics_service import AnalyticsService

router = APIRouter(prefix="/analytics")

@router.get("/analise/{cidade}")
def analise_cidade(cidade: str):
    """Retorna análise detalhada de uma cidade."""
    # Chamada via classe: Correto
    resultado = AnalyticsService.gerar_analise_cidade(cidade)
    
    if "erro" in resultado:
        raise HTTPException(status_code=404, detail=resultado["erro"])
    
    return resultado

@router.get("/analise/todas")
def analise_todas_cidades():
    """Retorna análise de todas as cidades."""
    # Chamada via classe: Correto
    return AnalyticsService.get_all_cities_analytics()

@router.get("/heatmap/{cidade}")
def heatmap_trafego(
    cidade: str,
    hora_inicio: Optional[int] = Query(None, ge=0, le=23),
    hora_fim: Optional[int] = Query(None, ge=0, le=23)
):
    """Retorna dados para heatmap de tráfego."""
    # Chamada via classe: Correto
    return AnalyticsService.get_traffic_heatmap(cidade, hora_inicio, hora_fim)