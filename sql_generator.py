import json

with open('traffic_data.json', 'r', encoding='utf-8') as f:
    data = json.load(f)

with open('import.sql', 'w', encoding='utf-8') as f:
    for item in data:
        # A Mágica do PostGIS: Criando um ponto geográfico (Point)
        geom = f"ST_GeomFromText('POINT({item['lng']} {item['lat']})', 4326)"
        
        sql = (f"INSERT INTO traffic_data (idvia, nome, hora, volume, capacidade, nivel, alerta, geom) "
               f"VALUES ({item['id_via']}, '{item['nome']}', '{item['hora']}', {item['volume']}, "
               f"{item['capacidade']}, {item['nivel']}, '{item['alerta']}', {geom});\n")
        f.write(sql)

print("🚀 SQL PARA POSTGIS GERADO COM SUCESSO!")