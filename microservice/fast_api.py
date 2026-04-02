from fastapi import FastAPI
from microservice.routes.routing_service import router


def create_app() -> FastAPI:
    app = FastAPI(
        title="Smart Traffic Flow API",
        version="1.0.0"
    )

    app.include_router(router)

    return app