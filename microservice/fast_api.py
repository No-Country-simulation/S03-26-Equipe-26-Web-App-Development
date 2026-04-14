from fastapi import FastAPI
from fastapi.middleware.cors import CORSMiddleware # IMPORTANTE: Verifique se essa linha existe
from routes.analytics_routes import router as analytics_router
from routes.gtfs_routes import router as gtfs_router

def create_app():
    app = FastAPI()

    # ========================================================
    # ADICIONE ESTE BLOCO EXATAMENTE AQUI:
    # ========================================================
    app.add_middleware(
        CORSMiddleware,
        allow_origins=["*"],  # Isso autoriza o localhost:5173 a ler os dados
        allow_credentials=True,
        allow_methods=["*"],
        allow_headers=["*"],
    )
    # ========================================================

    app.include_router(analytics_router)
    app.include_router(gtfs_router)

    return app

# Garanta que a variável 'app' seja criada fora da função para o 'run' funcionar
app = create_app()