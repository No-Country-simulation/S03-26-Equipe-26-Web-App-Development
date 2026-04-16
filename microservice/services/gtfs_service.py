from loaders.gtfs_loader import carregar_arquivos
from core.config import BASE_PATHS
import math
from typing import Optional, List, Dict, Any


class GTFSService:

    @staticmethod
    def get_cidades():
        """Retorna a lista de cidades disponíveis"""
        return list(BASE_PATHS.keys())

    @staticmethod
    def _load(cidade: str):
        """Carrega os dados GTFS de uma cidade"""
        caminho = BASE_PATHS.get(cidade.lower())
        if not caminho:
            return None
        dados = carregar_arquivos(caminho)
        
        # ✅ CONVERTER DataFrames para listas de dicionários
        if dados:
            for key in ['stops', 'routes', 'trips', 'stop_times', 'calendar', 'shapes', 'agency']:
                if key in dados and hasattr(dados[key], 'to_dict'):
                    dados[key] = dados[key].to_dict('records')
        
        return dados

    @staticmethod
    def get_stops(cidade: str):
        """Retorna as paradas (stops) de uma cidade específica"""
        dados = GTFSService._load(cidade)
        if dados and 'stops' in dados:
            return dados['stops']
        return None

    @staticmethod
    def get_routes(cidade: str):
        """Retorna as linhas (routes) de uma cidade específica"""
        dados = GTFSService._load(cidade)
        if dados and 'routes' in dados:
            return dados['routes']
        return None

    @staticmethod
    def get_trips(cidade: str):
        """Retorna as trips (viagens) de uma cidade específica"""
        dados = GTFSService._load(cidade)
        if dados and 'trips' in dados:
            return dados['trips']
        return None

    @staticmethod
    def get_stop_times(cidade: str):
        """Retorna os stop_times (horários nas paradas) de uma cidade específica"""
        dados = GTFSService._load(cidade)
        if dados and 'stop_times' in dados:
            return dados['stop_times']
        return None

    @staticmethod
    def get_calendar(cidade: str):
        """Retorna o calendário de serviços de uma cidade específica"""
        dados = GTFSService._load(cidade)
        if dados and 'calendar' in dados:
            return dados['calendar']
        return None

    @staticmethod
    def get_shapes(cidade: str):
        """Retorna as shapes (geometria das rotas) de uma cidade específica"""
        dados = GTFSService._load(cidade)
        if dados and 'shapes' in dados:
            return dados['shapes']
        return None

    @staticmethod
    def get_agency(cidade: str):
        """Retorna a agência de transporte de uma cidade específica"""
        dados = GTFSService._load(cidade)
        if dados and 'agency' in dados:
            return dados['agency']
        return None

    # =====================================================
    # MÉTODOS AUXILIARES PARA BUSCA
    # =====================================================

    @staticmethod
    def find_nearest_stops(cidade: str, lat: float, lon: float, raio_km: float = 1.0, limit: int = 5):
        """Encontra as paradas mais próximas de uma localização"""
        stops = GTFSService.get_stops(cidade)
        
        if not stops or len(stops) == 0:
            return []
        
        def distance(lat1, lon1, lat2, lon2):
            R = 6371  # Raio da Terra em km
            try:
                lat1, lon1, lat2, lon2 = map(float, [lat1, lon1, lat2, lon2])
                lat1, lon1, lat2, lon2 = map(math.radians, [lat1, lon1, lat2, lon2])
                dlat = lat2 - lat1
                dlon = lon2 - lon1
                a = math.sin(dlat/2)**2 + math.cos(lat1) * math.cos(lat2) * math.sin(dlon/2)**2
                return R * 2 * math.asin(math.sqrt(a))
            except (ValueError, TypeError):
                return float('inf')
        
        nearby = []
        for stop in stops:
            # Verificar se stop é dicionário
            if isinstance(stop, dict):
                stop_lat = stop.get('stop_lat')
                stop_lon = stop.get('stop_lon')
            else:
                continue
            
            if stop_lat is not None and stop_lon is not None:
                try:
                    dist = distance(lat, lon, float(stop_lat), float(stop_lon))
                    if dist <= raio_km:
                        nearby.append({
                            **stop,
                            'distancia_km': round(dist, 2)
                        })
                except (ValueError, TypeError):
                    continue
        
        nearby.sort(key=lambda x: x['distancia_km'])
        return nearby[:limit]

    @staticmethod
    def find_routes_for_stops(cidade: str, origin_stop_id: str, dest_stop_id: str):
        """Encontra rotas que conectam duas paradas"""
        routes = GTFSService.get_routes(cidade)
        trips = GTFSService.get_trips(cidade)
        stop_times = GTFSService.get_stop_times(cidade)
        
        if not routes or not trips or not stop_times:
            return []
        
        # Mapear trips para routes
        trip_to_route = {}
        for trip in trips:
            if isinstance(trip, dict):
                trip_id = trip.get('trip_id')
                route_id = trip.get('route_id')
                if trip_id and route_id:
                    trip_to_route[trip_id] = route_id
        
        # Encontrar trips que passam pelas duas paradas
        stops_in_trips = {}
        for st in stop_times:
            if isinstance(st, dict):
                trip_id = st.get('trip_id')
                stop_id = st.get('stop_id')
                if trip_id and stop_id:
                    if trip_id not in stops_in_trips:
                        stops_in_trips[trip_id] = set()
                    stops_in_trips[trip_id].add(stop_id)
        
        # Encontrar trips que contêm ambas as paradas
        connecting_trips = []
        for trip_id, stops_set in stops_in_trips.items():
            if origin_stop_id in stops_set and dest_stop_id in stops_set:
                connecting_trips.append(trip_id)
        
        # Mapear para rotas
        route_ids = set()
        for trip_id in connecting_trips:
            route_id = trip_to_route.get(trip_id)
            if route_id:
                route_ids.add(route_id)
        
        # Retornar informações das rotas
        result = []
        for route in routes:
            if isinstance(route, dict) and route.get('route_id') in route_ids:
                result.append(route)
        
        return result

    @staticmethod
    def get_route_info(cidade: str, route_id: str):
        """Retorna informações detalhadas de uma rota específica"""
        routes = GTFSService.get_routes(cidade)
        
        if not routes:
            return None
        
        for route in routes:
            if isinstance(route, dict) and str(route.get('route_id')) == str(route_id):
                return route
        
        return None

    @staticmethod
    def get_trips_for_route(cidade: str, route_id: str):
        """Retorna todas as trips de uma rota específica"""
        trips = GTFSService.get_trips(cidade)
        
        if not trips:
            return []
        
        result = []
        for trip in trips:
            if isinstance(trip, dict) and trip.get('route_id') == route_id:
                result.append(trip)
        
        return result

    @staticmethod
    def get_stop_times_for_trip(cidade: str, trip_id: str):
        """Retorna os stop_times de uma trip específica"""
        stop_times = GTFSService.get_stop_times(cidade)
        
        if not stop_times:
            return []
        
        result = []
        for st in stop_times:
            if isinstance(st, dict) and st.get('trip_id') == trip_id:
                result.append(st)
        
        return result

    @staticmethod
    def calculate_route_between_points(cidade: str, origin_lat: float, origin_lon: float, 
                                        destination_lat: float, destination_lon: float):
        """Calcula rota entre dois pontos usando GTFS"""
        # Encontrar paradas mais próximas
        origin_stops = GTFSService.find_nearest_stops(cidade, origin_lat, origin_lon, 1.0, 3)
        dest_stops = GTFSService.find_nearest_stops(cidade, destination_lat, destination_lon, 1.0, 3)
        
        if not origin_stops or not dest_stops:
            return None
        
        routes_result = []
        
        for origin_stop in origin_stops:
            for dest_stop in dest_stops:
                origin_id = origin_stop.get('stop_id')
                dest_id = dest_stop.get('stop_id')
                
                if origin_id and dest_id:
                    connecting_routes = GTFSService.find_routes_for_stops(cidade, origin_id, dest_id)
                    
                    for route in connecting_routes:
                        route_id = route.get('route_id')
                        route_name = route.get('route_long_name') or route.get('route_short_name', 'Linha')
                        route_short_name = route.get('route_short_name', '')
                        route_type = route.get('route_type', 3)
                        
                        # Determinar tipo de veículo
                        if route_type == 1:
                            vehicle = "Metrô"
                        elif route_type == 2:
                            vehicle = "Trem"
                        elif route_type == 3:
                            vehicle = "Ônibus"
                        elif route_type == 4:
                            vehicle = "Barca"
                        else:
                            vehicle = "Transporte"
                        
                        # Calcular distância e duração aproximadas
                        lat1 = origin_stop.get('stop_lat', 0)
                        lon1 = origin_stop.get('stop_lon', 0)
                        lat2 = dest_stop.get('stop_lat', 0)
                        lon2 = dest_stop.get('stop_lon', 0)
                        
                        if lat1 and lat2:
                            try:
                                distance = math.sqrt((float(lat2) - float(lat1))**2 + (float(lon2) - float(lon1))**2) * 111
                                distance = round(max(3, min(distance, 30)), 1)
                                speed = 20 if route_type == 3 else 30
                                duration = round(distance / speed * 60)
                                duration = max(10, min(duration, 120))
                            except (ValueError, TypeError):
                                distance = 8.5
                                duration = 35
                        else:
                            distance = 8.5
                            duration = 35
                        
                        routes_result.append({
                            "route_id": route_id,
                            "route_name": route_name,
                            "route_short_name": route_short_name,
                            "route_type": route_type,
                            "vehicle": vehicle,
                            "duration": duration,
                            "distance": distance,
                            "transfers": 0,
                            "origin_stop": origin_stop,
                            "destination_stop": dest_stop
                        })
        
        # Remover duplicatas e ordenar
        seen = set()
        unique_routes = []
        for route in routes_result:
            route_key = route['route_id']
            if route_key not in seen:
                seen.add(route_key)
                unique_routes.append(route)
        
        unique_routes.sort(key=lambda x: x['duration'])
        
        return unique_routes[:5]