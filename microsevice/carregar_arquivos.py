import pandas as pd
import os
import requests  # necessário para SPTrans

# --- Função para carregar GTFS ---
def carregar_arquivos(cidade, pasta_base="gtfs_data"):
    folder = os.path.join(pasta_base, cidade)
    stops = pd.read_csv(os.path.join(folder, "stops.txt"))
    routes = pd.read_csv(os.path.join(folder, "routes.txt"))
    trips = pd.read_csv(os.path.join(folder, "trips.txt"))
    stop_times = pd.read_csv(os.path.join(folder, "stop_times.txt"))
    calendar = pd.read_csv(os.path.join(folder, "calendar.txt"))
    return {
        "stops": stops,
        "routes": routes,
        "trips": trips,
        "stop_times": stop_times,
        "calendar": calendar
    }

# --- Exemplo de carregamento ---
sp_gtfs = carregar_arquivos("sp", pasta_base="")
rio_gtfs = carregar_arquivos("rj", pasta_base="")
poa_gtfs = carregar_arquivos("porto", pasta_base="")

# --- Listar todas as linhas ---
def listar_linhas(gtfs):
    return gtfs["routes"][["route_id", "route_short_name", "route_long_name", "route_type"]]

print(listar_linhas(sp_gtfs).head())

# --- Listar todas as paradas ---
def listar_paradas(gtfs):
    return gtfs["stops"][["stop_id", "stop_name", "stop_lat", "stop_lon"]]

print(listar_paradas(rio_gtfs).head())

# --- Buscar paradas de uma linha específica ---
def paradas_de_linha(gtfs, route_id):
    trips_id = gtfs["trips"][gtfs["trips"]["route_id"] == route_id]["trip_id"].tolist()
    stops = gtfs["stop_times"][gtfs["stop_times"]["trip_id"].isin(trips_id)]
    stops = stops.merge(gtfs["stops"], on="stop_id")
    stops = stops[["stop_id","stop_name","stop_lat","stop_lon"]].drop_duplicates()
    return stops

print(paradas_de_linha(sp_gtfs, "9500").head())

# --- Exemplo de consulta SPTrans ---
SPTRANS_API = "http://api.olhovivo.sptrans.com.br/v2.1/"

def buscar_linhas_sptrans():
    url = SPTRANS_API + "Linhas/Buscar?termosBusca=correios"
    resp = requests.get(url)
    if resp.status_code == 200:
        linhas = resp.json()
        for linha in linhas:
            print(linha["CodigoLinha"], "-", linha["DenominacaoTPTS"])
    else:
        print("Erro:", resp.text)

# Se tiver a API e token válido, pode chamar:
# buscar_linhas_sptrans()