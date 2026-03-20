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
        for via in vias:
            # Lógica de volume (mesma da V2)
            if via["tipo"] == "Arterial" and (hora == 8 or hora == 18):
                vol = random.randint(850, 980)
            elif via["id"] == 1 and hora == 3: # ANOMALIA
                vol = 450
            else:
                vol = random.randint(50, via["cap"] // 2)

            nivel = round((vol / via["cap"]) * 100, 2)
            alerta = "🚨 ANOMALIA" if (hora < 5 and nivel > 30) else "Normal"

            dataset.append({
                "id_via": via["id"],
                "nome": via["nome"],
                "lat": via["lat"],
                "lng": via["lng"],
                "hora": f"{hora:02d}:00",
                "volume": vol,
                "capacidade": via["cap"],
                "nivel": nivel,
                "alerta": alerta
            })

    return dataset

# Gerar e Salvar JSON
data = generate_geo_traffic_data()
with open('traffic_data.json', 'w', encoding='utf-8') as f:
    json.dump(data, f, indent=4, ensure_ascii=False)

print("🌍 V3 GEO-SPATIAL GERADA!")