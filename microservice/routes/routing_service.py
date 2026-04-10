router = APIRouter()

@router.get("/cidades")
def listar_cidades():
    """Retorna as cidades configuradas no sistema."""
    return get_cidades()

@router.get("/stops/{cidade}")
    """Retorna as paradas (stops) de uma cidade específica."""
    dados_stops = get_stops(cidade)
    
    if dados_stops is None:
        raise HTTPException(status_code=404, detail="Cidade ou arquivos não encontrados")
    

@router.get("/routes/{cidade}")
    """Retorna as linhas (routes) de uma cidade específica."""
    dados_routes = get_routes(cidade)
    
    if dados_routes is None:
        raise HTTPException(status_code=404, detail="Linhas não encontradas para esta cidade")
    