import requests
from core.config import TOMTOM_KEY


class TrafficService:

    BASE_URL = "https://api.tomtom.com/traffic/services/4"

    @staticmethod
    def get_flow(lat: float, lon: float):

        url = f"{TrafficService.BASE_URL}/flowSegmentData/absolute/10/json"

        params = {
            "point": f"{lat},{lon}",
            "key": TOMTOM_KEY
        }

        response = requests.get(url, params=params)

        if response.status_code != 200:
            return {"erro": "Falha TomTom"}

        flow = response.json().get("flowSegmentData", {})

        return {
            "velocidade_atual": flow.get("currentSpeed"),
            "velocidade_livre": flow.get("freeFlowSpeed"),
            "tempo_viagem": flow.get("travelTime"),
            "confianca": flow.get("confidence")
        }