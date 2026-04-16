export const Indicator = ({ label, value, subtext, icon: Icon, colorClass, darkMode }) => (
  <div className={`${darkMode ? 'bg-slate-800' : 'bg-slate-900'} text-white p-4 rounded-2xl flex-1 transition-colors shadow-lg`}>
    <p className="text-[10px] font-bold text-slate-400 uppercase mb-1 tracking-wider">{label}</p>
    <div className="flex items-baseline gap-2">
      <span className={`text-2xl font-black ${colorClass}`}>{value}</span>
      {Icon && <Icon size={14} className="text-slate-500" />}
    </div>
    <p className="text-[9px] text-slate-500 mt-1 leading-tight font-medium">{subtext}</p>
  </div>
);