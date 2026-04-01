from fastapi import FastAPI, HTTPException
from gtfs_loader import carregar_arquivos
import uvicorn
import unidecode

app = FastAPI(title="Microserviço Transporte Público")

# Mapeamento dos arquivos GTFS 
# Certifique-se de que as pastas existam no seu diretório 'micro'
try:
    GTFS = {
        "sao_paulo": carregar_arquivos(r"sp/cittamobi_gtfs"),
        "rio_de_janeiro": carregar_arquivos(r"rj"),
        "porto_alegre": carregar_arquivos(r"porto/arquivo-gtfs")
    }
    print("✅ Arquivos GTFS carregados com sucesso!")
except Exception as e:
    print(f"❌ Erro ao carregar arquivos GTFS: {e}")
    GTFS = {}

@app.get("/linhas/{cidade}")
def get_linhas(cidade: str):
    if cidade not in GTFS:
        raise HTTPException(status_code=404, detail="Cidade não encontrada ou não carregada.")
    
    df_routes = GTFS[cidade]["routes"]
    # Retorna ID, nome curto (ex: 9500) e nome longo
    return df_routes[["route_id", "route_short_name", "route_long_name"]].to_dict(orient="records")

@app.get("/paradas/{cidade}")
def get_paradas(cidade: str):
    if cidade not in GTFS:
        raise HTTPException(status_code=404, detail="Cidade não encontrada.")
    
    df_stops = GTFS[cidade]["stops"]
    return df_stops[["stop_id", "stop_name", "stop_lat", "stop_lon"]].to_dict(orient="records")

@app.get("/linhas/{cidade}/{route_id}/paradas")
def get_paradas_linha(cidade: str, route_id: str):
    if cidade not in GTFS:
        raise HTTPException(status_code=404, detail="Cidade não encontrada.")
    
    data = GTFS[cidade]
    
    # Filtra as trips (viagens) que pertencem àquela rota
    trips_ids = data["trips"][data["trips"]["route_id"] == route_id]["trip_id"]
    
    # Filtra os horários/sequência de paradas dessas viagens
    st_times = data["stop_times"][data["stop_times"]["trip_id"].isin(trips_ids)]
    
    # Faz o merge com a tabela de paradas para pegar os nomes e coordenadas
    resultado = st_times.merge(data["stops"], on="stop_id")
    
    # Remove duplicatas (uma linha passa na mesma parada em horários diferentes)
    resultado = resultado[["stop_id", "stop_name", "stop_lat", "stop_lon"]].drop_duplicates()
    
    return resultado.to_dict(orient="records")

@app.get("/buscar_rota/{cidade}/{termo}")
def buscar_por_local(cidade: str, termo: str):
    if cidade not in GTFS:
        raise HTTPException(status_code=404, detail="Cidade não carregada.")

    df_stops = GTFS[cidade]["stops"]
    
    # 1. Normaliza o termo de busca (remove acentos e deixa em minúsculo)
    termo_busca = unidecode.unidecode(termo).lower()

    # 2. Cria máscara de busca ignorando acentos nos nomes das paradas
    # Usamos o .apply() para garantir que cada nome passe pelo unidecode
    nomes_normalizados = df_stops['stop_name'].apply(lambda x: unidecode.unidecode(str(x)).lower())
    
    # 3. Aplica o filtro (contém o termo)
    mask = nomes_normalizados.str.contains(termo_busca, na=False)
    resultados = df_stops[mask]

    if resultados.empty:
        return {"mensagem": "Nenhum local encontrado.", "locais": []}

    return {
        "mensagem": f"Encontrado {len(resultados)} locais.",
        "locais": resultados[["stop_name", "stop_lat", "stop_lon"]].to_dict(orient="records")
    }

@app.get("/linhas/{cidade}/{route_id}/paradas")
def get_paradas_linha(cidade: str, route_id: str):
    if cidade not in GTFS:
        raise HTTPException(status_code=404, detail="Cidade não encontrada")
    
    data = GTFS[cidade]
    
    # Garantimos que o route_id seja tratado como string e sem espaços
    route_id = str(route_id).strip()
    
    # 1. Busca as viagens (trips) associadas à rota
    trips = data["trips"][data["trips"]["route_id"].astype(str) == route_id]
    
    if trips.empty:
        # Se cair aqui, o route_id passado não existe na tabela trips
        return {"erro": f"Nenhuma viagem encontrada para a rota {route_id}", "trips_count": 0}

    trips_ids = trips["trip_id"]
    
    # 2. Busca os tempos de parada
    st_times = data["stop_times"][data["stop_times"]["trip_id"].isin(trips_ids)]
    
    if st_times.empty:
        return {"erro": "Rota encontrada, mas não há paradas registradas para estas viagens."}
    
    # 3. Merge com a tabela de paradas (stops)
    resultado = st_times.merge(data["stops"], on="stop_id")
    
    # 4. Limpeza e retorno
    resultado = resultado[["stop_id", "stop_name", "stop_lat", "stop_lon"]].drop_duplicates()
    
    return resultado.to_dict(orient="records")

if __name__ == "__main__":
    # Inicia o servidor Uvicorn
    uvicorn.run(app, host="127.0.0.1", port=8000)