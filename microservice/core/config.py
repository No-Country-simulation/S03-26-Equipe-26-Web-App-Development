import os
from dotenv import load_dotenv

load_dotenv()

TOMTOM_KEY = os.getenv("TOMTOM_API_KEY", "")

# Configuração dos paths dos dados GTFS
BASE_PATHS = {
    "rio_janeiro": "data/rj/",
    "sao_paulo": "data/sp/cittamobi_gtfs/"
}

# Se quiser adicionar Porto Alegre depois, descomente e ajuste o path
# "porto_alegre": "data/porto/arqquivo-gtfs/",
