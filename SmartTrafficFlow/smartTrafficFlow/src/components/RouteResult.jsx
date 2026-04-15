import React from 'react';
import { Clock, MapPin, Navigation, Car, Bike, Walk, Bus, TrendingDown, Award } from 'lucide-react';

const RouteResult = ({ route, darkMode }) => {
  if (!route) return null;

  const melhorRota = route.melhor_opcao || route;
  
  const formatTime = (s) => s ? `${Math.floor(s / 60)} min` : '--';
  const formatDist = (m) => m ? `${(m / 1000).toFixed(1)} km` : '--';

  return (
    <div className={`p-6 rounded-[2rem] border ${darkMode ? 'bg-slate-900 border-slate-800' : 'bg-white border-slate-100 shadow-xl'}`}>
      <div className="flex items-center gap-2 mb-6 text-emerald-500 font-black text-xs uppercase tracking-widest">
        <Award size={16} /> Melhor Rota Encontrada
      </div>

      <div className="grid grid-cols-2 gap-4 mb-6">
        <div className={`p-4 rounded-2xl text-center ${darkMode ? 'bg-slate-800/50' : 'bg-slate-50'}`}>
          <Clock size={20} className="mx-auto mb-2 text-emerald-500" />
          <p className="text-2xl font-black">{formatTime(melhorRota.tempo_segundos)}</p>
          <p className="text-[9px] text-slate-500 font-bold uppercase">Estimativa</p>
        </div>
        <div className={`p-4 rounded-2xl text-center ${darkMode ? 'bg-slate-800/50' : 'bg-slate-50'}`}>
          <TrendingDown size={20} className="mx-auto mb-2 text-blue-500" />
          <p className="text-2xl font-black">{formatDist(melhorRota.distancia_metros)}</p>
          <p className="text-[9px] text-slate-500 font-bold uppercase">Distância</p>
        </div>
      </div>

      <div className="space-y-3">
        <div className="flex items-center gap-3">
          <div className="w-2 h-2 rounded-full bg-emerald-500" />
          <p className="text-xs font-bold truncate">{route.origemTexto || 'Sua Localização'}</p>
        </div>
        <div className="flex items-center gap-3">
          <div className="w-2 h-2 rounded-full bg-red-500" />
          <p className="text-xs font-bold truncate">{route.destinoTexto}</p>
        </div>
      </div>
    </div>
  );
};

export default RouteResult;
