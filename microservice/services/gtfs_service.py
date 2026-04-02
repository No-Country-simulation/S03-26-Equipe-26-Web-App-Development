import os
from microservice.loaders.gtfs_loader import carregar_arquivos

# 1. Configuração Dinâmica de Caminhos
CAMINHO_ATUAL = os.path.dirname(os.path.abspath(__file__))
BASE_DIR = os.path.dirname(CAMINHO_ATUAL)

# 2. Caminhos Corrigidos (Removendo pastas intermediárias inexistentes)
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
    
    # Valida se a cidade existe no mapeamento
    if cidade_key not in BASE_PATHS:
        print(f"⚠️ Cidade '{cidade}' não encontrada no mapeamento.")
        return None
    
    caminho = BASE_PATHS[cidade_key]
    
    try:
        # O loader vai procurar stops.txt dentro de 'microservice/data/sp'
        dados = carregar_arquivos(caminho)
        return dados.get("stops")
    except Exception as e:
        # Este print aparecerá no terminal se o arquivo físico não estiver lá
        print(f"❌ Erro ao carregar paradas de {cidade.upper()}: {e}")
        return None

def get_routes(cidade: str):
    """Busca as linhas/rotas da cidade informada."""
    cidade_key = cidade.lower()
    if cidade_key not in BASE_PATHS:
        return None
    
    try:
        dados = carregar_arquivos(caminho)
        return dados.get("routes")
    except Exception as e:
        print(f"❌ Erro ao carregar rotas de {cidade.upper()}: {e}")
        return None