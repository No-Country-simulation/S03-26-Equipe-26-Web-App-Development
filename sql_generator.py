import json

# Lendo o JSON
with open('traffic_data.json', 'r', encoding='utf-8') as f:
    data = json.load(f)

with open('import.sql', 'w', encoding='utf-8') as f:
    for item in data:
        # LIMPANDO O ALERTA: Removendo o emoji e deixando em MAIÚSCULAS para o Enum do Everton
        alerta_limpo = item['alerta'].replace('🚨 ', '').upper()
        
        # Padronizando outros campos que podem ser Enums (Tipo de via e Status)
        status_limpo = item['status'].upper()
        tipo_via_limpo = item.get('tipo', 'URBANA').upper()

        sql = (f"INSERT INTO traffic_data (idvia, nome, tipo, hora, volume, capacidade, nivel_congestionamento, status, alerta) "
               f"VALUES ({item['idvia']}, '{item['nome']}', '{tipo_via_limpo}', '{item['hora']}', {item['volume']}, "
               f"{item['capacidade']}, {item['nivel_congestionamento']}, '{status_limpo}', '{alerta_limpo}');\n")
        f.write(sql)

print("✅ SQL ATUALIZADO (Sem emojis e pronto para os Enums do Everton)!")