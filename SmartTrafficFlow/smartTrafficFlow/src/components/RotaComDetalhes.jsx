RotaComDetalhes.jsx
import React, { useState, useEffect } from 'react';
import { Cloud, AlertTriangle, Clock, MapPin, Navigation, Wind, Droplets, Thermometer } from 'lucide-react';

export const RotaComDetalhes = ({ rota, origem, destino, weather, incidents, darkMode }) => {
  const [impacto, setImpacto] = useState(1.0);

  useEffect(() => {
    // Calcular impacto do clima na rota
    if (weather) {
      const fatorClima = weather.fatorImpacto || 1.0;
      const fatorAcidentes = incidents?.length > 0 ? 0.7 : 1.0;
      setImpacto(fatorClima * fatorAcidentes);
    }
  }, [weather, incidents]);

  const tempoAjustado = rota?.tempo ? 
    parseInt(rota.tempo) * (1 + (1 - impacto)) : 
    null;

  return (
    <div className={`p-5 rounded-3xl border ${darkMode ? 'bg-slate-800/50 border-slate-700' : 'bg-white border-slate-200 shadow-xl'}`}>
      <h4 className="text-sm font-black mb-4 flex items-center gap-2">
        <Navigation size={18} className="text-blue-500" />
        Detalhes da Rota
      </h4>

      {/* Origem e Destino */}
      <div className="space-y-3 mb-6">
        <div className="flex items-center gap-3">
          <div className="w-8 h-8 rounded-full bg-green-500/20 flex items-center justify-center">
            <MapPin size={14} className="text-green-500" />
          </div>
          <div>
            <p className="text-[10px] text-slate-500 font-bold uppercase">Origem</p>
            <p className="text-sm font-black">{origem}</p>
          </div>
        </div>
        <div className="flex items-center gap-3">
          <div className="w-8 h-8 rounded-full bg-red-500/20 flex items-center justify-center">
            <MapPin size={14} className="text-red-500" />
          </div>
          <div>
            <p className="text-[10px] text-slate-500 font-bold uppercase">Destino</p>
            <p className="text-sm font-black">{destino}</p>
          </div>
        </div>
      </div>

      {/* Previsão do Clima */}
      {weather && (
        <div className={`p-4 rounded-2xl mb-4 ${darkMode ? 'bg-slate-700/50' : 'bg-slate-50'}`}>
          <p className="text-[10px] font-bold text-slate-400 uppercase mb-3 flex items-center gap-2">
            <Cloud size={12} /> Condições Climáticas
          </p>
          <div className="grid grid-cols-4 gap-3 text-center">
            <div>
              <Thermometer size={16} className="mx-auto mb-1 text-orange-400" />
              <p className="text-lg font-black">{weather.temperatura}°C</p>
              <p className="text-[8px] text-slate-500">Temperatura</p>
            </div>
            <div>
              <Droplets size={16} className="mx-auto mb-1 text-blue-400" />
              <p className="text-lg font-black">{weather.umidade}%</p>
              <p className="text-[8px] text-slate-500">Umidade</p>
            </div>
            <div>
              <Wind size={16} className="mx-auto mb-1 text-slate-400" />
              <p className="text-lg font-black capitalize">{weather.condicao?.substring(0, 8)}</p>
              <p className="text-[8px] text-slate-500">Condição</p>
            </div>
            <div>
              <div className={`text-lg font-black ${impacto < 0.8 ? 'text-red-500' : 'text-emerald-500'}`}>
                {Math.round(impacto * 100)}%
              </div>
              <p className="text-[8px] text-slate-500">Eficiência</p>
            </div>
          </div>
        </div>
      )}

      {/* Alertas e Acidentes */}
      {incidents && incidents.length > 0 && (
        <div className="p-4 rounded-2xl mb-4 bg-red-500/10 border border-red-500/20">
          <p className="text-[10px] font-bold text-red-400 uppercase mb-2 flex items-center gap-2">
            <AlertTriangle size={12} /> Alertas na Rota
          </p>
          {incidents.map((incident, idx) => (
            <div key={idx} className="flex items-center gap-2 text-xs mb-1">
              <div className="w-1.5 h-1.5 rounded-full bg-red-500"></div>
              <span className="text-red-300">{incident.description}</span>
            </div>
          ))}
          <p className="text-[9px] text-red-400/70 mt-2">
            ⚠️ Impacto estimado: +{Math.round((1 - impacto) * 30)} min no trajeto
          </p>
        </div>
      )}

      {/* Tempo Estimado Ajustado */}
      <div className={`p-4 rounded-2xl ${darkMode ? 'bg-blue-500/10 border border-blue-500/20' : 'bg-blue-50'}`}>
        <div className="flex items-center justify-between">
          <div>
            <p className="text-[10px] text-slate-500 font-bold uppercase">Tempo Estimado</p>
            <div className="flex items-baseline gap-2">
              <span className="text-2xl font-black text-blue-500">
                {tempoAjustado ? Math.round(tempoAjustado) : rota?.tempo || '--'}
              </span>
              <span className="text-xs text-slate-500">min</span>
            </div>
          </div>
          <Clock size={32} className="text-blue-500 opacity-50" />
        </div>
        {impacto < 0.9 && (
          <p className="text-[9px] text-red-400 mt-2">
            +{Math.round((1 - impacto) * 100)}% devido às condições atuais
          </p>
        )}
      </div>
    </div>
  );
};
