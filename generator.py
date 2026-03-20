import json
import random

def generate_geo_traffic_data():
    # Adicionando Coordenadas Reais (Latitude, Longitude)
    vias = [
        {"id": 1, "nome": "Av. Central", "tipo": "Arterial", "cap": 1000, "lat": -23.5505, "lng": -46.6333},
        {"id": 2, "nome": "Rodovia do Aeroporto", "tipo": "Rodovia", "cap": 2000, "lat": -23.4356, "lng": -46.4731},
        {"id": 3, "nome": "Rua das Flores", "tipo": "Residencial", "cap": 300, "lat": -23.5855, "lng": -46.6765}
    ]
    
    dataset = []

    for hora in range(24):
        # SIMULAÇÃO DE CLIMA: Crucial para o treinamento da IA
        # Vamos adicionar uma condição climática aleatória para cada hora
        clima_atual = random.choice(["Limpo", "Chuva Leve", "Chuva Forte"])
        fator_clima = 1.0
        if clima_atual == "Chuva Leve": fator_clima = 1.2
        if clima_atual == "Chuva Forte": fator_clima = 1.5

        for via in vias:
            # Lógica de volume baseada no tipo de via e hora
            if via["tipo"] == "Arterial" and (hora == 8 or hora == 18):
                vol_base = random.randint(850, 980)
            elif via["id"] == 1 and hora == 3: # SIMULAÇÃO DE ANOMALIA (Acidente/Obra)
                vol_base = 450
            else:
                vol_base = random.randint(50, via["cap"] // 2)

            # Aplica o fator clima no volume final
            vol_final = int(vol_base * fator_clima)
            
            # Garante que o volume não ultrapasse absurdamente a capacidade
            if vol_final > via["cap"] * 1.2: vol_final = int(via["cap"] * 1.2)

            nivel = round((vol_final / via["cap"]) * 100, 2)
            alerta = "🚨 ANOMALIA" if (hora < 5 and nivel > 30) else "Normal"
            if nivel > 90: alerta = "⚠️ CONGESTIONAMENTO CRÍTICO"

            dataset.append({
                "id_via": via["id"],
                "nome": via["nome"],
                "lat": via["lat"],
                "lng": via["lng"],
                "hora": f"{hora:02d}:00",
                "clima": clima_atual, # NOVO: Campo para a IA aprender
                "volume": vol_final,
                "capacidade": via["cap"],
                "nivel": nivel,
                "alerta": alerta
            })

    return dataset

# Gerar e Salvar JSON
try:
    data = generate_geo_traffic_data()
    with open('traffic_data.json', 'w', encoding='utf-8') as f:
        json.dump(data, f, indent=4, ensure_ascii=False)
    print("🌍 MOTOR V3 GEO-SPATIAL ATUALIZADO COM SUCESSO!")
    print("🧠 DADOS PRONTOS PARA O TREINAMENTO DE IA.")
except Exception as e:
    print(f"❌ Erro ao gerar os dados: {e}")