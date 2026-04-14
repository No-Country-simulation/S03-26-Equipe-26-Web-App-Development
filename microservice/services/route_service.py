import requests
from services.traffic_service import TrafficService
from services.event_service import EventService
from core.config import TOMTOM_KEY


MODOS = {
    "car": "car",
    "bike": "bicycle",
    "walk": "pedestrian",
    "bus": "bus"
}


class RouteService:

    @staticmethod
    def calcular_rota(origem: str, destino: str, modo: str = "car"):
        """
        Calcula rota usando TomTom API + integra tráfego e eventos.
        """

        modo_tomtom = MODOS.get(modo, "car")

        url = "https://api.tomtom.com/routing/1/calculateRoute"
        endpoint = f"{origem}:{destino}/json"

        try:
            response = requests.get(
                f"{url}/{endpoint}",
                params={
                    "travelMode": modo_tomtom,
                    "key": TOMTOM_KEY
                },
                timeout=5
            )

            if response.status_code != 200:
                return {"erro": "Falha ao calcular rota na TomTom"}

            data = response.json()

            if not data.get("routes"):
                return {"erro": "Nenhuma rota encontrada"}

            rota = data["routes"][0]
            summary = rota["summary"]

            # pega ponto médio da rota
            points = rota["legs"][0]["points"]
            mid = len(points) // 2

            lat = points[mid]["latitude"]
            lon = points[mid]["longitude"]

            # integra serviços extras
            traffic = TrafficService.get_flow(lat, lon)

            eventos = EventService.get_incidents(
                f"{lon-0.01},{lat-0.01},{lon+0.01},{lat+0.01}"
            )

            return {
                "modo": modo,
                "distancia_metros": summary["lengthInMeters"],
                "tempo_segundos": summary["travelTimeInSeconds"],
                "trafego": traffic,
                "eventos": eventos,
                "origem": origem,
                "destino": destino
            }

        except Exception as e:
            return {"erro": str(e)}

    # ---------------------------------------------------------
    # 🔥 MELHOR ROTA (multi-modal)
    # ---------------------------------------------------------
    @staticmethod
    def melhor_rota(origem: str, destino: str):

        modos = ["car", "bike", "walk", "bus"]
        resultados = []

        for modo in modos:
            rota = RouteService.calcular_rota(origem, destino, modo)

            if "erro" not in rota:
                resultados.append(rota)

        if not resultados:
            return {"erro": "Nenhuma rota válida encontrada"}

        melhor = min(resultados, key=lambda r: r["tempo_segundos"])

        return {
            "melhor_opcao": melhor,
            "todas_rotas": resultados
        }