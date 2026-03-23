import json

# Lendo o JSON original
with open('traffic_data.json', 'r', encoding='utf-8') as f:
    data = json.load(f)

# Criando o SQL LIMPO e SEM CONFLITOS
with open('import.sql', 'w', encoding='utf-8') as f:
    for item in data:
        # Usando nivel_congestionamento e idvia (PADRÃO DA ENTITY JAVA)
        # Removendo ST_GeomFromText pois a Entity do Everton NÃO TEM esse campo
        sql = (f"INSERT INTO traffic_data (idvia, nome, hora, volume, capacidade, nivel_congestionamento, alerta) "
               f"VALUES ({item['idvia']}, '{item['nome']}', '{item['hora']}', {item['volume']}, "
               f"{item['capacidade']}, {item['nivel_congestionamento']}, '{item['alerta']}');\n")
        f.write(sql)

print("✅ SUCESSO: import.sql gerado sem conflitos e pronto para a Sprint!")