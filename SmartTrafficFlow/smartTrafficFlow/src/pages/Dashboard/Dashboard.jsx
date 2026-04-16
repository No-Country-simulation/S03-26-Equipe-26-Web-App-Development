
import React, { useState, useEffect } from "react";
import { BarChart, Bar, XAxis, YAxis, Tooltip, ResponsiveContainer, Cell } from 'recharts';
import { Car, Cloud, Wind, Users, Navigation, Bus, Train, MapPin } from 'lucide-react';
import { 
  geocodeService, 
  transporteService, 
  trafficService, 
  weatherService 
} from "../../services/api";
import { useAuth } from "../../context/AuthContext";
import DashboardMap from "./DashboardMap";

export default function Dashboard() {
  const { logout } = useAuth();

  // Estados de busca e localização
  const [origemTexto, setOrigemTexto] = useState("Terminal Sao Mateus, SP");
  const [destinoTexto, setDestinoTexto] = useState("Terminal Sapopemba, SP");
  const [pontos, setPontos] = useState({ origem: null, destino: null });
  const [rotaData, setRotaData] = useState(null);
  const [loading, setLoading] = useState(false);
  const [usePythonAPI, setUsePythonAPI] = useState(true); // Priorizar Python/GTFS

  // Estados de Telemetria e Clima
  const [stats, setStats] = useState({ velocidade: 0, clima: "--", vento: "--", congestionamento: "--" });
  const [infoViagem, setInfoViagem] = useState({ onibus: "Aguardando...", previsao: "--", tipo: "🚌" });

  // Estados SPTRANS
  const [busPositions, setBusPositions] = useState([]);
  const [lastUpdate, setLastUpdate] = useState("");

  // Estado para volume da TomTom
  const [volumeDados, setVolumeDados] = useState([]);

  // --- ALTERAÇÃO 1: ESTADO PARA O FLUXO DE PESSOAS (JAVA) ---
  const [heatPoints, setHeatPoints] = useState([]);

  // Carregar ônibus
  const fetchBuses = async () => {
    try {
      console.log('🚌 Buscando ônibus...');
      const data = await transporteService.getRealTimeBuses();
      
      if (data && data.vs && Array.isArray(data.vs)) {
        setBusPositions(data.vs);
        setLastUpdate(new Date().toLocaleTimeString());
      } else if (data && data.l && Array.isArray(data.l)) {
        setBusPositions(data.l);
        setLastUpdate(new Date().toLocaleTimeString());
      }
    } catch (err) {
      console.error("Erro ao buscar ônibus:", err);
    }
  };

  // --- ALTERAÇÃO 2: FETCH PARA BUSCAR DADOS DO SEU BACKEND JAVA ---
  const fetchHeatmapData = async () => {
    try {
      // Ajuste a URL se o seu Spring Boot estiver em outra porta/rota
      const response = await fetch("http://localhost:8080/api/analytics/crowd-flow");
      const data = await response.json();
      // Converte o DTO Java para o formato [lat, lng, intensidade] esperado pelo Leaflet
      const formatted = data.map(p => [p.lat, p.lng, p.intensity]);
      setHeatPoints(formatted);
    } catch (err) {
      console.error("Erro ao carregar mapa de calor Java:", err);
    }
  };

  useEffect(() => {
    fetchBuses();
    fetchHeatmapData(); // Chamada inicial
    const busInterval = setInterval(fetchBuses, 20000);
    const heatInterval = setInterval(fetchHeatmapData, 60000); // Atualiza fluxo a cada 1 min
    return () => {
      clearInterval(busInterval);
      clearInterval(heatInterval);
    };
  }, []);

  const handleCalcularRota = async (e) => {
    if (e) e.preventDefault();
    setLoading(true);

    try {
      const resA = await geocodeService.searchAddress(origemTexto);
      const resB = await geocodeService.searchAddress(destinoTexto);

      if (resA && resB) {
        setPontos({ origem: resA, destino: resB });

        // Clima
        weatherService.getCurrentWeather(resB.lat, resB.lon)
          .then(climaRes => {
            if (climaRes) {
              const temp = climaRes.temperature_2m || 0;
              const vento = climaRes.wind_speed_10m || 0;
              setStats(prev => ({
                ...prev,
                clima: `${Math.round(temp)}°C`,
                vento: `${vento} km/h`
              }));
            }
          }).catch(() => console.error("Clima indisponível"));

        // ✅ ROTA - PRIORIZAR PYTHON (GTFS) DEPOIS JAVA (FALLBACK)
        try {
          let routeData = null;
          
          if (usePythonAPI) {
            const pythonRoute = await transporteService.getPublicTransitRoute(
              resA.lat, resA.lon, resB.lat, resB.lon
            );
            
            if (pythonRoute && pythonRoute.routes && pythonRoute.routes.length > 0) {
              routeData = pythonRoute;
              const primeiraRota = pythonRoute.routes[0];
              setRotaData(pythonRoute.geometry || pythonRoute);
              
              let tipoTransporte = "🚌";
              let nomeTransporte = "";
              
              if (primeiraRota.route_type === 1) { tipoTransporte = "🚇"; nomeTransporte = "Metrô"; }
              else if (primeiraRota.route_type === 2) { tipoTransporte = "🚆"; nomeTransporte = "Trem"; }
              else { tipoTransporte = "🚌"; nomeTransporte = "Ônibus"; }
              
              setInfoViagem({
                onibus: `${tipoTransporte} ${primeiraRota.route_name || nomeTransporte} ${primeiraRota.route_short_name || ''}`,
                previsao: pythonRoute.duration_minutes ? `${pythonRoute.duration_minutes} min` : "-- min",
                tipo: tipoTransporte
              });
              
              if (pythonRoute.duration_minutes && pythonRoute.distance_km) {
                const velocidadeMedia = (pythonRoute.distance_km / (pythonRoute.duration_minutes / 60));
                setStats(prev => ({ ...prev, velocidade: Math.round(velocidadeMedia) }));
              }
            }
          }
          
          if (!routeData) {
            const rotaRes = await transporteService.calculateRoute('sao_paulo', resA, resB, 'transit');
            if (rotaRes) {
              setRotaData(rotaRes.geometria || rotaRes);
              const publicTransport = rotaRes.options?.find(opt => opt.type === 'transporte_publico');
              if (publicTransport) {
                setInfoViagem({
                  onibus: `${publicTransport.vehicle} ${publicTransport.line || ''}`,
                  previsao: publicTransport.duration || "-- min",
                  tipo: publicTransport.vehicle === "Metrô" ? "🚇" : "🚌"
                });
              }
            }
          }
          
        } catch (err) {
          console.error("Erro na rota:", err);
        }

        // ✅ VOLUME DE TRÁFEGO
        try {
          const [volumeOrigem, volumeDestino] = await Promise.all([
            trafficService.getTrafficVolume(resA.lat, resA.lon),
            trafficService.getTrafficVolume(resB.lat, resB.lon)
          ]);
          
          const dadosGrafico = [
            {
              name: `📍 ${origemTexto.split(',')[0].substring(0, 25)}`,
              volume: volumeOrigem?.[0]?.volume || 70,
              status: volumeOrigem?.[0]?.status || 'MODERADO',
              currentSpeed: volumeOrigem?.[0]?.currentSpeed || 0
            },
            {
              name: `🎯 ${destinoTexto.split(',')[0].substring(0, 25)}`,
              volume: volumeDestino?.[0]?.volume || 70,
              status: volumeDestino?.[0]?.status || 'MODERADO',
              currentSpeed: volumeDestino?.[0]?.currentSpeed || 0
            }
          ];
          
          setVolumeDados(dadosGrafico);
          
          if (volumeDestino && volumeDestino.length > 0) {
            setStats(prev => ({ 
              ...prev, 
              velocidade: volumeDestino[0].currentSpeed || prev.velocidade,
              congestionamento: volumeDestino[0].status || 'NORMAL'
            }));
          }
          
        } catch (err) {
          console.error("❌ Erro ao buscar volume:", err);
        }
      }
    } catch (err) {
      console.error("Erro geral:", err);
    } finally {
      setLoading(false);
    }
  };

  const getStatusColor = (status) => {
    switch(status) {
      case 'LIVRE': return '#10b981';
      case 'MODERADO': return '#f59e0b';
      case 'CONGESTIONADO': return '#ef4444';
      case 'PARADO': return '#dc2626';
      default: return '#10b981';
    }
  };

  return (
    <div className="flex h-screen bg-[#0b0e14] text-slate-300 font-sans p-6 gap-6">
      {/* Barra Lateral */}
      <aside className="w-96 flex flex-col gap-6">
        <div className="bg-[#161b22] border border-slate-800 rounded-[2rem] p-6 flex justify-between items-center">
          <div className="flex items-center gap-2">
            <div className="bg-emerald-500 p-1.5 rounded-lg"><Car size={20} className="text-[#0b0e14]" /></div>
            <span className="font-black text-white uppercase tracking-tighter">Smart Traffic</span>
          </div>
          <div className="flex gap-2">
            <button 
              onClick={() => setUsePythonAPI(!usePythonAPI)}
              className={`text-xs px-2 py-1 rounded ${usePythonAPI ? 'bg-emerald-500/20 text-emerald-400' : 'bg-slate-700 text-slate-400'}`}
            >
              {usePythonAPI ? '🐍 GTFS' : '☕ OSRM'}
            </button>
            <button onClick={logout} className="text-xs font-bold text-red-500 hover:opacity-80">SAIR</button>
          </div>
        </div>

        <div className="bg-[#161b22] border border-slate-800 rounded-[2rem] p-8">
          <h3 className="text-emerald-500 font-black mb-4 text-xs uppercase tracking-widest">Otimização de Rota</h3>
          <form onSubmit={handleCalcularRota} className="flex flex-col gap-3">
            <input 
              value={origemTexto} 
              onChange={(e) => setOrigemTexto(e.target.value)} 
              className="bg-[#0d1117] border border-slate-800 p-3 rounded-xl text-sm text-white focus:border-emerald-500 outline-none" 
              placeholder="Origem"
            />
            <input 
              value={destinoTexto} 
              onChange={(e) => setDestinoTexto(e.target.value)} 
              className="bg-[#0d1117] border border-emerald-500/30 p-3 rounded-xl text-sm text-white focus:border-emerald-500 outline-none" 
              placeholder="Destino"
            />
            <button type="submit" className="bg-emerald-500 text-black font-black py-3 rounded-xl hover:bg-emerald-400 transition-all text-xs">
              {loading ? 'PROCESSANDO...' : 'CALCULAR MELHOR CAMINHO'}
            </button>
          </form>
        </div>

        <div className="bg-[#161b22] border border-slate-800 rounded-[2rem] p-6 flex flex-col gap-4">
          <div className="flex justify-between items-center">
            <div className="flex items-center gap-2">
              <Bus size={16} className="text-emerald-500" />
              <h3 className="text-white font-bold text-sm">Status da Frota</h3>
            </div>
            <span className="text-[9px] text-slate-500">{lastUpdate}</span>
          </div>
          <div className="flex items-end gap-2">
            <span className="text-3xl font-black text-white">{busPositions.length}</span>
            <span className="text-xs text-slate-500 pb-1">Ônibus ativos</span>
          </div>
        </div>

        {/* GRÁFICO DE VOLUME */}
        <div className="bg-[#161b22] border border-slate-800 rounded-[2rem] p-6 flex-1 overflow-hidden">
          <div className="flex items-center gap-2 mb-4">
            <Users size={16} className="text-emerald-500" />
            <h3 className="text-white font-bold text-sm">Volume de Tráfego</h3>
          </div>
          {volumeDados.length > 0 ? (
            <div className="h-64">
              <ResponsiveContainer width="100%" height="100%">
                <BarChart data={volumeDados} layout="vertical" margin={{ left: 80 }}>
                  <XAxis type="number" stroke="#64748b" fontSize={10} tickLine={false} axisLine={false} />
                  <YAxis type="category" dataKey="name" stroke="#64748b" fontSize={10} tickLine={false} axisLine={false} width={80} />
                  <Tooltip 
                    cursor={{fill: '#2d333b'}}
                    contentStyle={{ background: "#0d1117", border: "1px solid #30363d", borderRadius: "8px", fontSize: "10px" }} 
                  />
                  <Bar dataKey="volume" radius={[0, 4, 4, 0]}>
                    {volumeDados.map((entry, index) => (
                      <Cell key={`cell-${index}`} fill={getStatusColor(entry.status)} />
                    ))}
                  </Bar>
                </BarChart>
              </ResponsiveContainer>
            </div>
          ) : (
            <div className="h-64 flex items-center justify-center text-slate-500 text-sm">
              Calcule uma rota para ver o volume de tráfego
            </div>
          )}
        </div>
      </aside>

      {/* Área Principal */}
      <main className="flex-1 flex flex-col gap-6">
        <div className="grid grid-cols-4 gap-6">
          <Card title="Velocidade Média" value={stats.velocidade} unit="km/h" Icon={Navigation} />
          <Card title="Temperatura" value={stats.clima} Icon={Cloud} />
          <Card title="Veloc. Vento" value={stats.vento} Icon={Wind} />
          <Card title="Congestionamento" value={stats.congestionamento} Icon={Car} />
        </div>

        <div className="flex-1 bg-[#161b22] border border-slate-800 rounded-[3rem] overflow-hidden relative">
          {/* --- ALTERAÇÃO 3: PASSANDO O heatPoints PARA O MAPA --- */}
          <DashboardMap 
            pontos={pontos} 
            rota={rotaData} 
            busPositions={busPositions} 
            heatPoints={heatPoints} 
          />
          
          <div className="absolute bottom-8 left-8 bg-[#0d1117]/90 backdrop-blur-md border border-emerald-500/30 p-6 rounded-[2rem] z-[1000] min-w-[280px]">
            <span className="text-[9px] font-black text-emerald-400 uppercase tracking-widest italic">
              {usePythonAPI ? '🐍 Live Suggestion (GTFS)' : '☕ Live Suggestion (OSRM)'}
            </span>
            <div className="flex items-center gap-2 mt-1">
              <span className="text-2xl">{infoViagem.tipo || "🚌"}</span>
              <h4 className="text-white font-black text-xl uppercase">{infoViagem.onibus}</h4>
            </div>
            <div className="flex items-center gap-2 mt-2">
              <div className="h-2 w-2 rounded-full bg-emerald-500 animate-pulse" />
              <span className="text-xs text-slate-400">Tempo Estimado: <b className="text-white">{infoViagem.previsao}</b></span>
            </div>
          </div>
        </div>
      </main>
    </div>
  );
}

function Card({ title, value, unit, Icon }) {
  return (
    <div className="bg-[#161b22] border border-slate-800 rounded-[2rem] p-6">
      <div className="flex items-center gap-2 text-slate-500 mb-2">
        <Icon size={14} /> <span className="text-[10px] font-bold uppercase tracking-widest">{title}</span>
      </div>
      <h2 className="text-3xl font-black text-white">{value} <span className="text-sm text-emerald-500">{unit}</span></h2>
    </div>
  );
}