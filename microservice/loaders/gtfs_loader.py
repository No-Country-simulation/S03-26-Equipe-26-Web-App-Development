import pandas as pd
import os
import numpy as np

def carregar_arquivos(caminho_diretorio):
    dados = {}
    arquivos_alvo = ["stops", "routes"]

    for nome in arquivos_alvo:
        caminho_arquivo = os.path.join(caminho_diretorio, f"{nome}.txt")
        
        if os.path.exists(caminho_arquivo):
            try:
                # 1. Lê o CSV
                df = pd.read_csv(caminho_arquivo)
                
                # 2. LIMPEZA PESADA:
                # Substitui NaN por None (que vira null no JSON)
                # Também trata valores infinitos que podem surgir em cálculos de distância
                df = df.replace({np.nan: None, np.inf: None, -np.inf: None})
                
                # 3. Garante que qualquer valor nulo residual seja limpo
                dados[nome] = df.where(pd.notnull(df), None).to_dict(orient="records")
                
                print(f"✅ {nome}.txt de {os.path.basename(caminho_diretorio)} carregado e limpo!")
                
            except Exception as e:
                print(f"❌ Erro ao processar {nome}.txt: {e}")
                dados[nome] = []
        else:
            dados[nome] = []

    return dados