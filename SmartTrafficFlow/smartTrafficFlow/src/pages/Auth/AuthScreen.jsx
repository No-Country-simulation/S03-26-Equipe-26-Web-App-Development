import React, { useState } from 'react';
import { Mail, Lock, Car, AlertCircle } from 'lucide-react';
import { GoogleLogin } from '@react-oauth/google';
import { useAuth } from '../../context/AuthContext';

export const AuthScreen = ({ darkMode }) => {
  const { login, loginWithGoogle } = useAuth();
  const [isRegistering, setIsRegistering] = useState(false);
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    const result = await login(email, password);
    if (!result?.success) setError('Falha na autenticação');
    setLoading(false);
  };

  return (
    <div className={`min-h-screen flex items-center justify-center p-4 ${darkMode ? 'bg-slate-950' : 'bg-slate-100'}`}>
      <div className={`max-w-4xl w-full flex flex-col md:flex-row rounded-[2.5rem] shadow-2xl overflow-hidden ${darkMode ? 'bg-slate-900 border border-slate-800' : 'bg-white'}`}>
        
        {/* Lado Esquerdo - Branding */}
        <div className="md:w-1/2 bg-[#061a1a] p-12 flex flex-col justify-between text-white relative">
          <div className="z-10">
            <div className="flex items-center gap-2 mb-12">
              <div className="bg-emerald-500 p-2 rounded-xl"><Car size={24} className="text-[#061a1a]" /></div>
              <span className="text-xl font-black tracking-tighter">SMARTFLOW</span>
            </div>
            <h1 className="text-4xl font-bold leading-tight mb-6">O futuro da <span className="text-emerald-400">mobilidade urbana</span> começa aqui.</h1>
          </div>
          <div className="z-10 flex items-center gap-3 text-[10px] font-bold text-slate-500 uppercase tracking-widest">
            <div className="w-2 h-2 rounded-full bg-emerald-500 animate-pulse" /> SPTrans & Open-Meteo Integrated
          </div>
        </div>

        {/* Lado Direito - Form */}
        <div className="md:w-1/2 p-10 md:p-14 flex flex-col justify-center">
          <h2 className={`text-2xl font-black mb-8 ${darkMode ? 'text-white' : 'text-slate-800'}`}>
            {isRegistering ? 'Criar conta' : 'Acesse o Sistema'}
          </h2>

          <form onSubmit={handleSubmit} className="space-y-4 mb-6">
            <div className="relative">
              <Mail className="absolute left-4 top-1/2 -translate-y-1/2 text-slate-400" size={16} />
              <input type="email" placeholder="E-mail" value={email} onChange={(e) => setEmail(e.target.value)}
                className={`w-full pl-12 pr-4 py-3 rounded-xl border outline-none text-sm ${darkMode ? 'bg-slate-800 border-slate-700 text-white' : 'bg-slate-50 border-slate-200'}`} required />
            </div>
            <div className="relative">
              <Lock className="absolute left-4 top-1/2 -translate-y-1/2 text-slate-400" size={16} />
              <input type="password" placeholder="Senha" value={password} onChange={(e) => setPassword(e.target.value)}
                className={`w-full pl-12 pr-4 py-3 rounded-xl border outline-none text-sm ${darkMode ? 'bg-slate-800 border-slate-700 text-white' : 'bg-slate-50 border-slate-200'}`} required />
            </div>
            <button type="submit" className="w-full py-4 bg-emerald-500 hover:bg-emerald-600 text-[#061a1a] rounded-xl font-black text-xs transition-all">
              {loading ? 'CARREGANDO...' : 'ENTRAR NO SISTEMA'}
            </button>
          </form>

          <div className="relative flex items-center justify-center mb-6">
            <div className="absolute inset-0 flex items-center"><div className="w-full border-t border-slate-700"></div></div>
            <span className={`relative px-4 text-[10px] font-bold uppercase ${darkMode ? 'bg-slate-900 text-slate-500' : 'bg-white text-slate-400'}`}>Ou continue com</span>
          </div>

          <div className="flex justify-center">
            <GoogleLogin onSuccess={loginWithGoogle} onError={() => setError('Falha no Google Login')} theme={darkMode ? "filled_black" : "outline"} shape="pill" />
          </div>
        </div>
      </div>
    </div>
  );
};
