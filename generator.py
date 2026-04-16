import json
import random

def generate_geo_traffic_data():
    vias = [
        {"id": 1, "nome": "Av. Central", "tipo": "Arterial", "cap": 1000, "lat": -23.5505, "lng": -46.6333},
        {"id": 2, "nome": "Rodovia do Aeroporto", "tipo": "Rodovia", "cap": 2000, "lat": -23.4356, "lng": -46.4731},
        {"id": 3, "nome": "Rua das Flores", "tipo": "Residencial", "cap": 300, "lat": -23.5855, "lng": -46.6765}
    ]
    
    dataset = []

    for hora in range(24):
        clima_atual = random.choice(["Limpo", "Chuva Leve", "Chuva Forte"])
        fator_clima = 1.2 if clima_atual == "Chuva Leve" else (1.5 if clima_atual == "Chuva Forte" else 1.0)

        for via in vias:
            if via["tipo"] == "Arterial" and (hora == 8 or hora == 18):
                vol_base = random.randint(850, 980)
            else:
                vol_base = random.randint(50, via["cap"] // 2)

            vol_final = int(vol_base * fator_clima)
            if vol_final > via["cap"] * 1.2: vol_final = int(via["cap"] * 1.2)

            nivel = round((vol_final / via["cap"]) * 100, 2)
            
            status = "Fluido"
            alerta = "Normal"
            if nivel > 70: 
                status = "Lento"
                alerta = "Atencao"
            if nivel > 90: 
                status = "Congestionado"
                alerta = "Critico"

            dataset.append({
                "idvia": via["id"],
                "nome": via["nome"],
                "tipo": via["tipo"],
                "hora": f"{hora:02d}:00",
                "volume": vol_final,
                "capacidade": via["cap"],
                "nivel_congestionamento": nivel,
                "status": status,
                "alerta": alerta,
                "clima": clima_atual, 
                "lat": via["lat"],
                "lng": via["lng"]
            })
    return dataset

if __name__ == "__main__":
    try:
        data = generate_geo_traffic_data()
        with open('traffic_data.json', 'w', encoding='utf-8') as f:
            json.dump(data, f, indent=4, ensure_ascii=False)
        print("Sucesso: Dados gerados e sincronizados!")
    except Exception as e:
        print(f"Erro: {e}")