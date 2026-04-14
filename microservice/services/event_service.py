import requests
from core.config import TOMTOM_KEY


class EventService:

    @staticmethod
    def get_incidents(bbox: str):

        url = "https://api.tomtom.com/traffic/services/5/incidentDetails"

        params = {
            "bbox": bbox,
            "fields": "{incidents{type,geometry{coordinates},properties{iconCategory}}}",
            "language": "pt-BR",
            "key": TOMTOM_KEY
        }

        response = requests.get(url, params=params)

        if response.status_code != 200:
            return []

        incidents = response.json().get("incidents", [])

        eventos = []

        for i in incidents:
            eventos.append({
                "tipo": i["properties"]["iconCategory"],
                "coordenadas": i["geometry"]["coordinates"]
            })

        return eventos