from fastapi import APIRouter, HTTPException
from microservice.services.gtfs_service import get_cidades, get_stops, get_routes

router = APIRouter()

@router.get("/cidades")
def listar_cidades():
    """Retorna as cidades configuradas no sistema."""
    return get_cidades()

@router.get("/stops/{cidade}")
def stops(cidade: str):
    """Retorna as paradas (stops) de uma cidade específica."""
    # O get_stops já retorna uma LISTA limpa de NaN
    dados_stops = get_stops(cidade)
    
    if dados_stops is None:
        raise HTTPException(status_code=404, detail="Cidade ou arquivos não encontrados")
    
    # IMPORTANTE: Como dados_stops é uma LISTA, usamos [:100] em vez de .head()
    # Limitei em 100 para não travar o seu navegador com milhares de pontos
    return dados_stops[:100]

@router.get("/routes/{cidade}")
def routes(cidade: str):
    """Retorna as linhas (routes) de uma cidade específica."""
    dados_routes = get_routes(cidade)
    
    if dados_routes is None:
        raise HTTPException(status_code=404, detail="Linhas não encontradas para esta cidade")
    
    return dados_routes[:50]