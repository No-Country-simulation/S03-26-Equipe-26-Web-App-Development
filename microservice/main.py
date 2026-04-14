from fastapi import FastAPI
from fastapi.middleware.cors import CORSMiddleware
from routes.analytics_routes import router as analytics_router
from routes.gtfs_routes import router as gtfs_router

def create_app():
    app = FastAPI(title="Smart Traffic Flow API")

    app.add_middleware(
        CORSMiddleware,
        allow_origins=["*"],
        allow_credentials=True,
        allow_methods=["*"],
        allow_headers=["*"],
    )

    @app.get("/")
    def home():
        return {"status": "API rodando 🚀"}

    app.include_router(analytics_router)
    app.include_router(gtfs_router)

    return app

app = create_app()