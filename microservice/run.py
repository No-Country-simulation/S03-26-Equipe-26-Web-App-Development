# run.py - Colocar na raiz do projeto (S03-26-Equipe-26-Web-App-Development/)
import uvicorn
import sys
import os

# Adiciona o diretório atual ao path
sys.path.insert(0, os.path.dirname(os.path.abspath(__file__)))

if __name__ == "__main__":
    uvicorn.run(
        "microservice.main:app",
        host="0.0.0.0",
        port=8000,
        reload=True,
        log_level="info"
    )