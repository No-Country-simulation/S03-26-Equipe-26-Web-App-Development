from microservice.services.gtfs_service import (
    get_stops, get_routes, get_trips, get_stop_times, BASE_PATHS
)

def gerar_analise_cidade(cidade: str):
    """Gera análise completa de uma cidade."""
    if cidade not in BASE_PATHS:
        return {"erro": f"Cidade '{cidade}' não encontrada no mapeamento"}

    caminho = BASE_PATHS[cidade]
    
    # Coletar dados
    stops = get_stops(cidade) or []
    routes = get_routes(cidade) or []
    trips = get_trips(cidade) or []
    stop_times = get_stop_times(cidade) or []

    # Análises
    total_paradas = len(stops)
    total_rotas = len(routes)
    total_viagens = len(trips)
    total_horarios = len(stop_times)

    # Calcular densidade de paradas (se tiver coordenadas)
    paradas_com_coords = sum(1 for s in stops if s.get("stop_lat") and s.get("stop_lon"))
    
    # Tipos de rota
    tipos_rota = {}
    for route in routes:
        route_type = route.get("route_type", "Desconhecido")
        tipos_rota[route_type] = tipos_rota.get(route_type, 0) + 1

    return {
        "cidade": cidade,
        "caminho_dados": caminho,
        "total_paradas": total_paradas,
        "paradas_com_coordenadas": paradas_com_coords,
        "total_rotas": total_rotas,
        "total_viagens": total_viagens,
        "total_horarios": total_horarios,
        "tipos_rota": tipos_rota,
        "status": "OK"
    }

def get_all_cities_analytics():
    """Retorna análise de todas as cidades."""
    resultados = {}
    for cidade in BASE_PATHS.keys():
        resultados[cidade] = gerar_analise_cidade(cidade)
    return resultados

def get_traffic_heatmap(cidade: str, hora_inicio: int = None, hora_fim: int = None):
    """Gera dados simulados para heatmap de tráfego."""
    # Simulação - você pode integrar com seus dados reais depois
    import random
    
    stops = get_stops(cidade) or []
    
    heatmap_data = []
    for stop in stops[:50]:  # Limitar para performance
        if stop.get("stop_lat") and stop.get("stop_lon"):
            # Simular congestionamento baseado na hora
            if hora_inicio and hora_fim:
                congestion = random.uniform(0.3, 0.9)
            else:
                congestion = random.uniform(0.1, 0.95)
            
            heatmap_data.append({
                "lat": stop["stop_lat"],
                "lng": stop["stop_lon"],
                "stop_name": stop.get("stop_name", "Parada"),
                "congestionamento": round(congestion, 2),
                "nivel": "ALTO" if congestion > 0.7 else "MEDIO" if congestion > 0.4 else "BAIXO"
            })
    
    return {
        "cidade": cidade,
        "total_pontos": len(heatmap_data),
        "dados": heatmap_data
    }