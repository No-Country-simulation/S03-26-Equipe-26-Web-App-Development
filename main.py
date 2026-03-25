import os
import time

def rodar_etapa(comando, descricao):
    print(f"\n🚀 {descricao}...")
    os.system(comando)
    time.sleep(1) # Pequena pausa para leitura

print("=== 🚦 SMART TRAFFIC FLOW - SISTEMA INTEGRADO ===")

rodar_etapa("python generator.py", "Gerando dados de sensores (JSON)")
rodar_etapa("python sql_generator.py", "Sincronizando SQL para o Backend (Java Enum Ready)")
rodar_etapa("python train_ia.py", "Treinando o modelo de Inteligência Artificial")
rodar_etapa("python visualize_ia.py", "Gerando análise visual de performance")

print("\n✅ TUDO PRONTO! O sistema está atualizado e os gráficos gerados.")
print("📁 Arquivos para entrega: import.sql e resultado_ia.png")