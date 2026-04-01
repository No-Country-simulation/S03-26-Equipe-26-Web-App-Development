import pandas as pd
import os

def carregar_arquivos(pasta_cidade):
    # Garante que o caminho seja tratado corretamente independente do SO
    # Se a pasta estiver dentro de outra (como sp/cittamobi_gtfs), passe o caminho completo
    
    arquivos_obrigatorios = ["stops.txt", "routes.txt", "trips.txt", "stop_times.txt"]
    dados = {}
    
    for arquivo in arquivos_obrigatorios:
        caminho_completo = os.path.join(pasta_cidade, arquivo)
        if os.path.exists(caminho_completo):
            dados[arquivo.replace(".txt", "")] = pd.read_csv(caminho_completo)
        else:
            print(f"Aviso: {arquivo} não encontrado em {pasta_cidade}")
            dados[arquivo.replace(".txt", "")] = pd.DataFrame() # Retorna vazio para não quebrar
            
    return dados