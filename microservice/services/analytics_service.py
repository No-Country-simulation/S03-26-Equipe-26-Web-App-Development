import pandas as pd
import numpy as np
from loaders.gtfs_loader import carregar_arquivos
from core.config import BASE_PATHS

class AnalyticsService:
    # Cache global que carrega ao iniciar o Uvicorn
    _DADOS_CARREGADOS = {
        "sao_paulo": carregar_arquivos(BASE_PATHS["sao_paulo"])
    }

    @staticmethod
    def gerar_analise_cidade(cidade: str):
        dados = AnalyticsService._DADOS_CARREGADOS.get(cidade)
        if not dados or not isinstance(dados.get("stops"), pd.DataFrame):
            return {"erro": f"Dados de {cidade} não encontrados."}

        return {
            "cidade": cidade,
            "total_paradas": len(dados["stops"]),
            "total_rotas": len(dados["routes"]),
            "total_viagens": len(dados["trips"]),
            "status": "online"
        }

    @staticmethod
    def get_all_cities_analytics():
        return {
            "cidades_disponiveis": list(AnalyticsService._DADOS_CARREGADOS.keys()),
            "total_cidades": len(AnalyticsService._DADOS_CARREGADOS)
        }

    @staticmethod
    def get_traffic_heatmap(cidade: str, hora_inicio: int = None, hora_fim: int = None):
        dados = AnalyticsService._DADOS_CARREGADOS.get(cidade)
        if not dados or "stops" not in dados:
            return []
        
        stops = dados["stops"]
        return stops[['stop_lat', 'stop_lon']].head(1000).to_dict(orient="records")