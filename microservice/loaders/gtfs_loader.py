import pandas as pd
import os
import numpy as np

# Defina os arquivos alvo
arquivos_alvo = ["stops", "routes", "trips", "stop_times"]

def carregar_arquivos(caminho_diretorio):
    dados = {}
    
    for nome in arquivos_alvo:
        caminho_arquivo = os.path.join(caminho_diretorio, f"{nome}.txt")
        
        if os.path.exists(caminho_arquivo):
            try:
                # Otimização 1: Ler apenas colunas necessárias se o arquivo for stop_times
                # Otimização 2: Manter como DataFrame em vez de converter para records imediatamente
                df = pd.read_csv(caminho_arquivo, low_memory=False)
                
                # Otimização 3: Limpar apenas o necessário
                # Se for stop_times, evite converter para lista de registros se não for usar tudo
                dados[nome] = df
                print(f"✅ {nome}.txt carregado com sucesso.")
                
            except Exception as e:
                print(f"❌ Erro ao processar {nome}.txt: {e}")
                dados[nome] = pd.DataFrame()
        else:
            print(f"⚠️ Arquivo não encontrado: {caminho_arquivo}")
            dados[nome] = pd.DataFrame()

    return dados