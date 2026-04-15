import React from 'react';
import { 
  Bus, 
  Navigation, 
  ChevronRight, 
  Users, 
  Activity, 
  LogOut 
} from 'lucide-react';
import { Indicator } from '../ui/Indicator';
import { TRANSPORT_LINES, PEAK_DEMAND_DATA } from '../data/transportData';

export const Sidebar = ({ selectedLine, setSelectedLine, darkMode, onLogout }) => {
  return (
    <aside className={`w-[400px] border-r h-full overflow-y-auto flex flex-col p-6 shadow-2xl z-20 transition-colors ${
      darkMode ? 'bg-slate-900 border-slate-800' : 'bg-white border-slate-200'
    }`}>
      
      {/* 1. Header com Logo */}
      <div className="flex items-center gap-3 mb-8">
        <div className="w-10 h-10 bg-blue-600 rounded-xl flex items-center justify-center text-white shadow-lg shadow-blue-500/20">
          <Activity size={22} />
        </div>
        <h1 className={`text-lg font-black tracking-tighter ${darkMode ? 'text-white' : 'text-slate-900'}`}>
          SMARTFLOW
        </h1>
      </div>

      {/* 2. Perfil do Usuário */}
      <div className={`p-4 rounded-2xl mb-8 flex items-center justify-between border ${
        darkMode ? 'bg-slate-800/50 border-slate-700' : 'bg-slate-50 border-slate-100'
      }`}>
        <div className="flex items-center gap-3">
          <div className="w-10 h-10 rounded-full bg-blue-600 flex items-center justify-center font-black text-white text-xs">
            ES
          </div>
          <div>
            <p className={`text-xs font-black ${darkMode ? 'text-white' : 'text-slate-900'}`}>Everton Souza</p>
            <p className="text-[9px] font-bold text-slate-500 uppercase tracking-widest">Admin Master</p>
          </div>
        </div>
        <button 
          onClick={onLogout}
          className="text-slate-400 hover:text-red-500 transition-colors"
        >
          <LogOut size={18} />
        </button>
      </div>

      {/* 3. Seleção de Linhas */}
      <div className="mb-8">
        <h3 className="text-[10px] font-black text-slate-400 uppercase tracking-widest mb-4 flex items-center gap-2 px-2">
          <Navigation size={12} /> LINHAS ATIVAS
        </h3>
        <div className="space-y-2">
          {TRANSPORT_LINES.map((line) => (
            <button 
              key={line.id} 
              onClick={() => setSelectedLine(line)}
              className={`w-full flex items-center justify-between p-4 rounded-2xl border transition-all text-left ${
                selectedLine.id === line.id 
                  ? (darkMode ? 'border-blue-500 bg-blue-500/10' : 'border-blue-600 bg-blue-50/50') 
                  : (darkMode ? 'border-transparent hover:bg-slate-800' : 'border-transparent hover:bg-slate-50')
              }`}
            >
              <div className="flex items-center gap-4">
                <div className={`p-2 rounded-lg transition-colors ${
                  selectedLine.id === line.id ? 'bg-blue-600 text-white' : 'bg-slate-100 text-slate-400'
                }`}>
                  <Bus size={18} />
                </div>
                <div>
                  <p className={`text-xs font-black ${selectedLine.id === line.id ? 'text-blue-500' : (darkMode ? 'text-slate-300' : 'text-slate-700')}`}>
                    {line.name}
                  </p>
                  <p className="text-[10px] text-slate-500 font-bold">{line.stops} Paradas — {line.distance}</p>
                </div>
              </div>
              <ChevronRight size={16} className={selectedLine.id === line.id ? 'text-blue-500' : 'text-slate-300'} />
            </button>
          ))}
        </div>
      </div>

      {/* 4. Indicadores Rápidos */}
      <div className="flex gap-3 mb-8">
        <Indicator 
          label="Ocupação" 
          value="37%" 
          subtext="Capacidade" 
          colorClass="text-blue-400" 
          darkMode={darkMode} 
        />
        <Indicator 
          label="Fluxo/H" 
          value="515" 
          subtext="Passageiros" 
          colorClass="text-emerald-400" 
          icon={Users} 
          darkMode={darkMode} 
        />
      </div>

      {/* 5. Gráfico de Demanda */}
      <div className="mt-auto">
        <h3 className="text-[10px] font-black text-slate-400 uppercase mb-4 px-2">DEMANDA 24H</h3>
        <div className={`rounded-3xl p-5 h-28 flex items-end justify-between gap-1 border ${
          darkMode ? 'bg-slate-800/50 border-slate-700' : 'bg-slate-50 border-slate-100'
        }`}>
          {PEAK_DEMAND_DATA.map((d, i) => (
            <div 
              key={i} 
              className={`flex-1 rounded-t-sm transition-all duration-500 ${
                d.val > 80 ? 'bg-red-500/60' : 'bg-blue-500/60'
              }`} 
              style={{ height: `${d.val}%` }}
              title={`${d.h}: ${d.val}%`}
            ></div>
          ))}
        </div>
      </div>

    </aside>
  );
};
