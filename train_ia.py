# -*- coding: utf-8 -*-
import json

def train_traffic_ia():
    print("--- ACORDANDO A INTELIGENCIA ARTIFICIAL ---")
    try:
        # Abrindo o JSON com encoding utf-8 explícito
        with open('traffic_data.json', 'r', encoding='utf-8') as f:
            dados = json.load(f)
        
        memoria_ia = {}
        for registro in dados:
            clima = registro.get('clima', 'Limpo')
            nivel = registro['nivel']
            if clima not in memoria_ia:
                memoria_ia[clima] = []
            memoria_ia[clima].append(nivel)
        
        print("\n--- RESULTADO DO TREINAMENTO (MEDIAS APRENDIDAS) ---")
        for clima, niveis in memoria_ia.items():
            media = sum(niveis) / len(niveis)
            print(f"Clima [{clima}] -> Nivel medio de transito: {media:.2f}%")
        
        return memoria_ia

    except Exception as e:
        print(f"Erro no treinamento: {e}")

if __name__ == "__main__":
    train_traffic_ia()
    print("\nIA TREINADA E PRONTA PARA O SMART TRAFFIC FLOW!")