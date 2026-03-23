import { useState, useEffect, useRef } from 'react'
import L from 'leaflet'
import 'leaflet/dist/leaflet.css'
import './Home.css'

function Home() {

    const [optionRoute, setOptionRoute] = useState(true)
    const mapRef = useRef(null); // Referência para o container do mapa
    const mapInstanceRef = useRef(null); // Referência para a instância do mapa

    useEffect(() => {
        // Verifica se o mapa já foi inicializado
        if (mapInstanceRef.current) return;

        // Inicializa o mapa
        const map = L.map(mapRef.current).setView([-6.376193, -35.133796], 14); // Coordenadas exemplo (São Paulo)

        // Adiciona o tile layer (fundo do mapa)
        L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
            attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
        }).addTo(map);

        // Guarda a instância do mapa
        mapInstanceRef.current = map;

        // Função de cleanup quando o componente for desmontado
        return () => {
            if (mapInstanceRef.current) {
                mapInstanceRef.current.remove();
                mapInstanceRef.current = null;
            }
        };
    }, []);

    return (
        <>
            <section className="home-header">
                <h1>SmartTrafficFlow</h1>
                <p>SISTEMA DE FLUXO DINÂMICO</p>
            </section>

            <section className="main">
                <p>SELECIONAR LOCAL/TRAJETO</p>

                <div className='main-option'>
                    <p onClick={() => setOptionRoute(true)} className={optionRoute ? 'option-mark' : ''}>LOCAL</p>
                    <p onClick={() => setOptionRoute(false)} className={!optionRoute ? 'option-mark' : ''}>TRAJETO</p>
                </div>

                <div className='main-local'>
                    {optionRoute ?
                        <input type="text" placeholder='Localização' />
                        :
                        <p>
                            <input type="text" placeholder='Ponto de Partida' />
                            <input type="text" placeholder='Ponto de Chegada' />
                        </p>
                    }
                </div>

            </section>

            <div id="map" ref={mapRef}></div>
        </>
    )
}

export default Home