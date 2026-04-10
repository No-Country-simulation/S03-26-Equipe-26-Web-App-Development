import pandas as pd
import os
import numpy as np

def carregar_arquivos(caminho_diretorio):
    dados = {}
    
    for nome in arquivos_alvo:
        caminho_arquivo = os.path.join(caminho_diretorio, f"{nome}.txt")
        
        if os.path.exists(caminho_arquivo):
            try:
                df = pd.read_csv(caminho_arquivo)
                
                df = df.replace({np.nan: None, np.inf: None, -np.inf: None})
                
                dados[nome] = df.where(pd.notnull(df), None).to_dict(orient="records")
                
            except Exception as e:
                print(f"❌ Erro ao processar {nome}.txt: {e}")
                dados[nome] = []
        else:
            dados[nome] = []

    return dados