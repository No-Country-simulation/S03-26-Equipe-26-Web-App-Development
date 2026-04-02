from loaders.gtfs_loader import carregar_arquivos
from services.gtfs_service import BASE_PATHS

def gerar_analise(cidade):
    if cidade not in BASE_PATHS:
        return {"erro": "Cidade não encontrada no mapeamento"}

    # Busca o caminho real configurado no gtfs_service
    caminho = BASE_PATHS[cidade]
    dados = carregar_arquivos(caminho)

    # ... sua lógica de contagem
    return {
        "cidade": cidade,
        "total_paradas": len(dados["stops"]),
        "total_viagens": len(dados["trips"]),
        "total_registros_stop_times": len(dados["stop_times"])
    }