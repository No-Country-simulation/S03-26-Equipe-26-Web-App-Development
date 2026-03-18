import json
import random

def generate_smart_traffic_data():
    vias = [
        {"id": 1, "nome": "Av. Central", "tipo": "Arterial", "capacidade": 1000},
        {"id": 2, "nome": "Rodovia do Aeroporto", "tipo": "Rodovia", "capacidade": 2000},
        {"id": 3, "nome": "Rua das Flores", "tipo": "Residencial", "capacidade": 300}
    ]
    
    dataset = []

    for hora in range(24):
        for via in vias:
            # --- Lógica de Volume Base ---
            if via["tipo"] == "Arterial" and (hora == 8 or hora == 18):
                volume = random.randint(850, 980) # Pico comercial
            elif via["nome"] == "Rodovia do Aeroporto" and (hora in [5, 6, 22, 23]):
                volume = random.randint(1600, 1950) # Pico voos
            elif hora < 5:
                volume = random.randint(20, 80) # Madrugada vazia
            else:
                volume = random.randint(150, via["capacidade"] // 2)

            # --- Lógica de ANOMALIA ---
            # Vamos simular um acidente aleatório às 03:00 na Av. Central
            if via["id"] == 1 and hora == 3:
                volume = 450 # Volume altíssimo para 3h da manhã!
            
            # Cálculo de Congestionamento (%)
            nivel = round((volume / via["capacidade"]) * 100, 2)
            
            # Status Simples
            status = "Livre" if nivel < 50 else "Moderado" if nivel < 85 else "Crítico"

            # --- Inteligência de Alertas ---
            alerta = "Fluxo Normal"
            if nivel > 90:
                alerta = "⚠️ CRÍTICO: Via Saturada"
            elif hora < 5 and nivel > 30:
                alerta = "🚨 ANOMALIA: Possível Incidente/Acidente"
            elif via["tipo"] == "Arterial" and nivel > 80:
                alerta = "🟡 ALERTA: Lentidão Elevada"

            dataset.append({
                "id_via": via["id"],
                "nome": via["nome"],
                "tipo": via["tipo"],
                "hora": f"{hora:02d}:00",
                "volume": volume,
                "capacidade": via["capacidade"],
                "nivel_congestionamento": nivel,
                "status": status,
                "alerta": alerta
            })

    return dataset

# Gerar e Salvar
data = generate_smart_traffic_data()
with open('traffic_data.json', 'w', encoding='utf-8') as f:
    json.dump(data, f, indent=4, ensure_ascii=False)

print("🚀 V2 GERADA COM SUCESSO!")
print("✅ Inteligência de Anomalias adicionada ao arquivo 'traffic_data.json'.")