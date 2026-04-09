import os

CAMINHO_ATUAL = os.path.dirname(os.path.abspath(__file__))
BASE_DIR = os.path.dirname(CAMINHO_ATUAL)

BASE_PATHS = {
    "sp": os.path.join(BASE_DIR, "data", "sp", "cittamobi_gtfs"),
    "rj": os.path.join(BASE_DIR, "data", "rj"),
    "porto": os.path.join(BASE_DIR, "data", "porto", "arquivo-gtfs")
}

def get_cidades():
    """Retorna a lista de cidades (chaves do dicionário)."""
    return list(BASE_PATHS.keys())

def get_stops(cidade: str):
    """Busca as paradas da cidade informada."""
    cidade_key = cidade.lower()
    
    if cidade_key not in BASE_PATHS:
        print(f"⚠️ Cidade '{cidade}' não encontrada no mapeamento.")
        return None
    
    caminho = BASE_PATHS[cidade_key]
    
    try:
        dados = carregar_arquivos(caminho)
    except Exception as e:
        print(f"❌ Erro ao carregar paradas de {cidade.upper()}: {e}")
        return None

def get_routes(cidade: str):
    """Busca as linhas/rotas da cidade informada."""
    cidade_key = cidade.lower()
    if cidade_key not in BASE_PATHS:
        return None
    
    try:
        dados = carregar_arquivos(caminho)
    except Exception as e:
        print(f"❌ Erro ao carregar rotas de {cidade.upper()}: {e}")
        return None