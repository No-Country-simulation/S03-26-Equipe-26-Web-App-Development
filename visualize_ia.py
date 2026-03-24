import json
import matplotlib.pyplot as plt

# 1. Carregar os dados que você gerou
try:
    with open('traffic_data.json', 'r', encoding='utf-8') as f:
        data = json.load(f)
except FileNotFoundError:
    print("❌ Erro: Arquivo traffic_data.json não encontrado! Rode o generator.py primeiro.")
    exit()

# 2. Agrupar dados por Alerta (Normal vs Anomalia)
stats = {}

for item in data:
    alerta = item['alerta']
    nivel = item['nivel_congestionamento']
    
    if alerta not in stats:
        stats[alerta] = []
    stats[alerta].append(nivel)

# 3. Calcular Médias
labels = list(stats.keys())
medias = [sum(v) / len(v) for v in stats.values()]

# 4. Criar o Gráfico
plt.figure(figsize=(8, 6))
# Cores: Verde para Normal, Vermelho para Anomalia
cores = ['#2ecc71' if 'Normal' in l else '#e74c3c' for l in labels]

bars = plt.bar(labels, medias, color=cores, edgecolor='black', alpha=0.8)

# Adicionar os números em cima das barras
for bar in bars:
    yval = bar.get_height()
    plt.text(bar.get_x() + bar.get_width()/2, yval + 1, f'{yval:.1f}%', 
             ha='center', va='bottom', fontweight='bold')

# Estilização
plt.title('Impacto das Anomalias no Tráfego', fontsize=14, fontweight='bold')
plt.ylabel('Nível de Congestionamento Médio (%)')
plt.ylim(0, 100) # Deixa o gráfico mais fácil de ler
plt.grid(axis='y', linestyle='--', alpha=0.6)

# 5. Salvar a Imagem
plt.savefig('resultado_ia.png', dpi=300)
print("\n✅ GRÁFICO GERADO: resultado_ia.png")
print("🚀 Média Normal:", f"{medias[0]:.2f}%" if len(medias) > 0 else "N/A")
if len(medias) > 1:
    print("🚨 Média Anomalia:", f"{medias[1]:.2f}%")

plt.show()