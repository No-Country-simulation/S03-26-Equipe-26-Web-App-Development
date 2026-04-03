import { useState, useEffect, useRef } from 'react'
// import L from 'leaflet'
// import 'leaflet/dist/leaflet.css'
import './Home.css'
import logo from '../../assets/logo.png'
import iconEnviar from '../../assets/enviar.png'
import iconPin from '../../assets/pin.png'

function Home() {

    const [optionRoute, setOptionRoute] = useState(true)
    // const mapRef = useRef(null); // Referência para o container do mapa
    // const mapInstanceRef = useRef(null); // Referência para a instância do mapa

    // useEffect(() => {
    //     // Verifica se o mapa já foi inicializado
    //     if (mapInstanceRef.current) return;

    //     // Inicializa o mapa
    //     const map = L.map(mapRef.current).setView([-6.376193, -35.133796], 14); // Coordenadas exemplo (São Paulo)

    //     // Adiciona o tile layer (fundo do mapa)
    //     L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
    //         attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
    //     }).addTo(map);

    //     // Guarda a instância do mapa
    //     mapInstanceRef.current = map;

    //     // Função de cleanup quando o componente for desmontado
    //     return () => {
    //         if (mapInstanceRef.current) {
    //             mapInstanceRef.current.remove();
    //             mapInstanceRef.current = null;
    //         }
    //     };
    // }, []);
    const [insights, setInsights] = useState(null);
    const [filter, setFilter] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState("");

    useEffect(() => {
        const fetchInsight = async () => {
            fetch("http://localhost:8080/traffic/insights")
                .then((response) => {
                    if (!response.ok) {
                        throw new Error(`Erro HTTP ${response.status}`);
                    }
                    return response.json();
                })
                .then((data) => {
                    setInsights(data);
                    setError("");
                })
                .catch((err) => {
                    console.error("Erro ao carregar insights:", err);
                    setError("Nao foi possivel carregar os insights.");
                })
                .finally(() => {
                    setLoading(false);
                });
        }

        const fetchFilter = async () => {
            fetch("http://localhost:8080/traffic")
                .then((response) => {
                    if (!response.ok) {
                        throw new Error(`Erro HTTP ${response.status}`);
                    }
                    return response.json();
                })
                .then((data) => {
                    setFilter(data);
                    setError("");
                })
                .catch((err) => {
                    console.error("Erro ao carregar filter:", err);
                    setError("Nao foi possivel carregar os filter.");
                })
                .finally(() => {
                    setLoading(false);
                });
        }


        fetchInsight();
        fetchFilter();
    }, []);

    if (loading) return <p>Carregando insights...</p>;
    if (error) return <p>{error}</p>;
    if (!insights) return <p>Nenhum insight disponivel.</p>;


    return (
        <>
            <section className="home-header">
                <div>
                    <img src={logo} alt="" />
                    <h1>SmartTrafficFlow</h1>
                </div>
                <p>SISTEMA DE FLUXO DINÂMICO</p>
            </section>

            <section className="main">
                <p className='main-title'>
                    <img src={iconPin} alt="" />
                    <label htmlFor="">SELECIONAR LOCAL/TRAJE</label>TO
                </p>

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

            {/* <p>INSIGHTS DO TRÁFEGO</p>
                <div className='datas-insights'>
                    <p>Total de registros: {insights.totalRegistros}</p>
                    <p>Horario de pico: {insights.horarioPico}</p>
                    <p>Volume no horario de pico: {insights.volumeHorarioPico}</p>
                    <p>Via mais movimentada: {insights.viaMaisMovimentada}</p>
                    <p>Media de volume da via mais movimentada: {insights.mediaVolumeViaMaisMovimentada}</p>
                </div> */}
            <section className="datas">
                {optionRoute ?
                    filter && filter.map((item, index) => (
                        <button key={index} className='datas-filter'>
                            <div className='datas-local'>
                                <img src={iconEnviar} alt="" className='icon-enviar' />
                                <p>{item.nome}</p>
                            </div>
                            <p>{item.tipo}</p>
                        </button>
                    ))
                    :
                    ""}
            </section>


            {/* <div id="map" ref={mapRef}></div> */}
        </>
    )
}

export default Home