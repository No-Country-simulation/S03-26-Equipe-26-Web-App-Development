import json
import random

def generate_traffic_data():
    vias = [
        {"id": 1, "nome": "Av. Central", "tipo": "Arterial", "capacidade": 1000},
        {"id": 2, "nome": "Rodovia do Aeroporto", "tipo": "Rodovia", "capacidade": 2000},
        {"id": 3, "nome": "Rua das Flores", "tipo": "Residencial", "capacidade": 300}
    ]
    
    dataset = []

    for hora in range(24):
        for via in vias:
            # Lógica de Pico (Arterial: 08h e 18h)
            if via["tipo"] == "Arterial" and (hora == 8 or hora == 18):
                volume = random.randint(800, 1000)
            
            # Lógica Aeroporto (Pico de madrugada e comercial)
            elif via["nome"] == "Rodovia do Aeroporto" and (hora in [5, 6, 11, 22, 23]):
                volume = random.randint(1500, 1900)
            
            # Fluxo Normal (Madrugada vazia)
            elif hora < 5:
                volume = random.randint(10, 50)
            
            # Fluxo padrão dia
            else:
                volume = random.randint(100, via["capacidade"] // 2)

            # Cálculo de Congestionamento (%)
            nivel = round((volume / via["capacidade"]) * 100, 2)
            
            # Status para o Frontend
            status = "Livre" if nivel < 50 else "Moderado" if nivel < 85 else "Crítico"

            dataset.append({
                "id_via": via["id"],
                "nome": via["nome"],
                "tipo": via["tipo"],
                "hora": f"{hora:02d}:00",
                "volume": volume,
                "capacidade": via["capacidade"],
                "nivel_congestionamento": nivel,
                "status": status
            })

    return dataset

# Salvar em JSON para o time
data = generate_traffic_data()
with open('traffic_data.json', 'w', encoding='utf-8') as f:
    json.dump(data, f, indent=4, ensure_ascii=False)

print("✅ Dataset 'traffic_data.json' gerado com sucesso!")