import React, { useState } from 'react';
// Trocado Walk por Footprints para evitar erro de exportação do lucide-react
import { 
  Search, MapPin, Navigation, Bus, Car, Bike, 
  Footprints, Target, Crosshair, AlertCircle, 
  CheckCircle2, Loader2 
} from 'lucide-react';
import { transporteService } from '../services/api';

const RouteSearch = ({ userLocation, onRouteCalculated, darkMode, cidade = 'sao_paulo' }) => {
  const [origin, setOrigin] = useState('');
  const [destination, setDestination] = useState('');
  const [originCoords, setOriginCoords] = useState(null);
  const [destinationCoords, setDestinationCoords] = useState(null);
  const [modo, setModo] = useState('car');
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  const modosDisponiveis = [
    { id: 'car', nome: 'Carro', icon: Car },
    { id: 'bike', nome: 'Bike', icon: Bike },
    { id: 'walk', nome: 'A pé', icon: Footprints }, // Ícone atualizado aqui
    { id: 'bus', nome: 'Bus', icon: Bus }
  ];

  const useCurrentLocation = () => {
    if (userLocation) {
      // Ajuste para aceitar tanto array [lat, lon] quanto objeto {lat, lon}
      const coords = Array.isArray(userLocation) 
        ? { lat: userLocation[0], lon: userLocation[1] }
        : userLocation;
      
      setOriginCoords(coords);
      setOrigin('📍 Minha localização atual');
      setError('');
    } else {
      setError('GPS não disponível. Verifique as permissões.');
    }
  };

  const geocode = async (endereco, type) => {
    if (!endereco || endereco.includes('📍')) return;
    setLoading(true);
    setError('');
    try {
      const result = await transporteService.geocodeAddress(cidade, endereco);
      if (result) {
        const coords = { lat: result.lat, lon: result.lon };
        if (type === 'origin') {
          setOriginCoords(coords);
          setOrigin(result.nome);
        } else {
          setDestinationCoords(coords);
          setDestination(result.nome);
        }
      } else {
        setError(`Local não encontrado: "${endereco}"`);
      }
    } catch (err) {
      setError('Erro ao buscar endereço.');
    } finally {
      setLoading(false);
    }
  };

  const handleAction = async (isBestRoute = false) => {
    if (!originCoords || !destinationCoords) {
      setError('Confirme origem e destino (Aperte Enter nos campos).');
      return;
    }

    setLoading(true);
    setError('');
    try {
      const result = isBestRoute 
        ? await transporteService.getBestRoute(cidade, originCoords, destinationCoords)
        : await transporteService.calculateRoute(cidade, originCoords, destinationCoords, modo);

      if (result && !result.erro) {
        onRouteCalculated({
          ...result,
          origem: originCoords,
          destino: destinationCoords,
          origemTexto: origin,
          destinoTexto: destination,
          modo: isBestRoute ? result.modo : modo
        });
      } else {
        setError(result?.erro || 'Erro ao calcular rota.');
      }
    } catch (err) {
      setError('Erro na comunicação com o servidor.');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className={`p-6 rounded-[2.5rem] border shadow-2xl transition-all ${darkMode ? 'bg-slate-900/90 border-slate-700 backdrop-blur-xl' : 'bg-white border-slate-200'}`}>
      <div className="flex items-center justify-between mb-6">
        <h3 className="text-xs font-black uppercase tracking-widest flex items-center gap-2 text-emerald-500">
          <Navigation size={16} /> Planejamento Inteligente
        </h3>
        {loading && <Loader2 size={16} className="animate-spin text-emerald-500" />}
      </div>

      <div className="space-y-4 mb-6">
        {/* Origem */}
        <div className="relative">
          <MapPin size={16} className={`absolute left-3 top-1/2 -translate-y-1/2 ${originCoords ? 'text-emerald-500' : 'text-slate-500'}`} />
          <input
            type="text"
            value={origin}
            onChange={(e) => { setOrigin(e.target.value); setOriginCoords(null); }}
            onKeyDown={(e) => e.key === 'Enter' && geocode(origin, 'origin')}
            placeholder="De onde sair? (Enter)"
            className={`w-full pl-10 pr-12 py-3.5 rounded-2xl border text-sm outline-none transition-all ${
              darkMode ? 'bg-slate-800 border-slate-700 text-white focus:border-emerald-500' : 'bg-slate-50 border-slate-200 focus:border-emerald-500'
            }`}
          />
          <div className="absolute right-2 top-1/2 -translate-y-1/2 flex items-center gap-1">
            {originCoords && <CheckCircle2 size={16} className="text-emerald-500 mr-1" />}
            <button onClick={useCurrentLocation} className="p-2 hover:bg-emerald-500/10 rounded-xl text-emerald-500 transition-colors">
              <Crosshair size={16} />
            </button>
          </div>
        </div>

        {/* Destino */}
        <div className="relative">
          <Target size={16} className={`absolute left-3 top-1/2 -translate-y-1/2 ${destinationCoords ? 'text-red-500' : 'text-slate-500'}`} />
          <input
            type="text"
            value={destination}
            onChange={(e) => { setDestination(e.target.value); setDestinationCoords(null); }}
            onKeyDown={(e) => e.key === 'Enter' && geocode(destination, 'destination')}
            placeholder="Para onde vamos? (Enter)"
            className={`w-full pl-10 pr-12 py-3.5 rounded-2xl border text-sm outline-none transition-all ${
              darkMode ? 'bg-slate-800 border-slate-700 text-white focus:border-red-500' : 'bg-slate-50 border-slate-200 focus:border-red-500'
            }`}
          />
          {destinationCoords && <CheckCircle2 size={16} className="absolute right-4 top-1/2 -translate-y-1/2 text-emerald-500" />}
        </div>
      </div>

      {/* Modos */}
      <div className="flex bg-slate-800/50 p-1 rounded-2xl gap-1 mb-6">
        {modosDisponiveis.map((m) => {
          const Icon = m.icon;
          return (
            <button
              key={m.id}
              onClick={() => setModo(m.id)}
              className={`flex-1 py-2.5 rounded-xl flex flex-col items-center gap-1 transition-all ${
                modo === m.id ? 'bg-emerald-500 text-white shadow-lg' : 'text-slate-400 hover:text-slate-200'
              }`}
            >
              <Icon size={16} />
              <span className="text-[9px] font-bold uppercase">{m.nome}</span>
            </button>
          );
        })}
      </div>

      <div className="grid grid-cols-2 gap-3">
        <button
          onClick={() => handleAction(false)}
          disabled={loading || !originCoords || !destinationCoords}
          className="py-4 bg-slate-800 hover:bg-slate-700 text-white rounded-2xl font-black text-[10px] tracking-widest uppercase transition-all disabled:opacity-30"
        >
          Calcular Rota
        </button>
        <button
          onClick={() => handleAction(true)}
          disabled={loading || !originCoords || !destinationCoords}
          className="py-4 bg-emerald-500 hover:bg-emerald-600 text-white rounded-2xl font-black text-[10px] tracking-widest uppercase transition-all shadow-lg shadow-emerald-500/20 disabled:opacity-30"
        >
          Melhor Rota
        </button>
      </div>

      {error && (
        <div className="mt-4 p-3 rounded-xl bg-red-500/10 border border-red-500/20 flex items-center gap-2 text-red-500 text-[10px] font-bold uppercase">
          <AlertCircle size={14} /> {error}
        </div>
      )}
    </div>
  );
};

export default RouteSearch;