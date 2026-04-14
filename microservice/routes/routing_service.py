from fastapi import APIRouter, HTTPException
from microservice.services.gtfs_service import (
    get_cidades,
    get_stops,
    get_routes
)

router = APIRouter(prefix="/gtfs")


@router.get("/cidades")
def listar_cidades():
    """Retorna as cidades configuradas no sistema."""
    return get_cidades()


@router.get("/stops/{cidade}")
def listar_stops(cidade: str):
    """Retorna as paradas (stops) de uma cidade específica."""
    
    dados_stops = get_stops(cidade)

    if dados_stops is None:
        raise HTTPException(
            status_code=404,
            detail="Cidade ou arquivos não encontrados"
        )

    return dados_stops


@router.get("/routes/{cidade}")
def listar_routes(cidade: str):
    """Retorna as linhas (routes) de uma cidade específica."""

    dados_routes = get_routes(cidade)

    if dados_routes is None:
        raise HTTPException(
            status_code=404,
            detail="Linhas não encontradas para esta cidade"
        )

    return dados_routes