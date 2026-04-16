import React from 'react';
import { GoogleLogin } from '@react-oauth/google';
import { Car, ShieldCheck } from 'lucide-react';

const Login = ({ onLoginSuccess }) => {
  return (
    <div className="min-h-screen bg-slate-100 flex items-center justify-center p-4 font-sans">
      <div className="max-w-4xl w-full bg-white rounded-3xl shadow-2xl overflow-hidden flex flex-col md:flex-row">
        {/* Lado Esquerdo - Branding */}
        <div className="md:w-1/2 bg-[#061a1a] p-12 flex flex-col justify-between text-white">
          <div>
            <div className="flex items-center gap-2 mb-8">
              <div className="bg-emerald-500 p-2 rounded-lg">
                <Car size={24} className="text-white" />
              </div>
              <span className="text-xl font-bold tracking-tight">SmartTrafficFlow</span>
            </div>
            <h1 className="text-4xl font-bold leading-tight mb-4">
              O futuro da <span className="text-emerald-400">mobilidade urbana</span> começa aqui.
            </h1>
            <p className="text-slate-400">Monitore, analise e otimize o tráfego da sua cidade em tempo real.</p>
          </div>
          <div className="flex items-center gap-2 text-xs text-slate-500">
            <div className="w-2 h-2 rounded-full bg-emerald-500 animate-pulse"></div>
            Sistemas integrados: SPTrans & Open-Meteo
          </div>
        </div>

        {/* Lado Direito - Login */}
        <div className="md:w-1/2 p-12 flex flex-col justify-center">
          <h2 className="text-2xl font-bold text-slate-800 mb-2">Bem-vindo</h2>
          <p className="text-slate-500 mb-8">Acesse sua conta para visualizar o dashboard.</p>
          
          <div className="space-y-4">
            <GoogleLogin 
              onSuccess={onLoginSuccess}
              onError={() => console.log('Login Failed')}
              useOneTap
              theme="filled_blue"
              shape="pill"
            />
            <button className="w-full py-3 px-4 border border-slate-200 rounded-xl font-medium text-slate-600 hover:bg-slate-50 transition-all flex items-center justify-center gap-2">
              Criar Conta
            </button>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Login;
