import React, { useState } from 'react';
import { Mail, Lock, Car, AlertCircle, User, Phone } from 'lucide-react';
import { useAuth } from '../../context/AuthContext';
import { useTheme } from '../../context/ThemeContext';

const Login = () => {
  const { login, register, loading: authLoading } = useAuth();
  const { isDarkMode } = useTheme();
  
  // Estado para controlar qual formulário mostrar
  const [isRegistering, setIsRegistering] = useState(false);
  
  // Estados do formulário de login
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  
  // Estados do formulário de registro
  const [registerData, setRegisterData] = useState({
    nome: '',
    email: '',
    senha: '',
    confirmSenha: '',
    telefone: ''
  });
  
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);

  // Handler para login
  const handleSubmit = async (e) => {
    e.preventDefault();
    
    if (!email || !password) {
      setError('Preencha todos os campos');
      return;
    }

    setLoading(true);
    setError('');

    try {
      const result = await login(email, password);
      if (!result?.success) {
        setError(result?.error || 'Falha na autenticação. Verifique suas credenciais.');
      }
    } catch (err) {
      setError('Erro ao conectar com o servidor');
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  // Handler para registro
  const handleRegister = async (e) => {
    e.preventDefault();
    
    // Validações
    if (!registerData.nome || !registerData.email || !registerData.senha) {
      setError('Preencha todos os campos obrigatórios');
      return;
    }
    
    if (registerData.senha !== registerData.confirmSenha) {
      setError('As senhas não coincidem');
      return;
    }
    
    if (registerData.senha.length < 6) {
      setError('A senha deve ter pelo menos 6 caracteres');
      return;
    }

    setLoading(true);
    setError('');

    try {
      const { confirmSenha, ...dataToSend } = registerData;
      const result = await register(dataToSend);
      
      if (!result?.success) {
        setError(result?.error || 'Falha no registro. Tente novamente.');
      }
    } catch (err) {
      setError('Erro ao conectar com o servidor');
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  // Alternar entre login e registro
  const toggleMode = () => {
    setIsRegistering(!isRegistering);
    setError('');
    setEmail('');
    setPassword('');
    setRegisterData({
      nome: '',
      email: '',
      senha: '',
      confirmSenha: '',
      telefone: ''
    });
  };

  return (
    <div className={`min-h-screen flex items-center justify-center p-4 ${isDarkMode ? 'bg-slate-950' : 'bg-slate-100'}`}>
      <div className={`max-w-4xl w-full flex flex-col md:flex-row rounded-[2.5rem] shadow-2xl overflow-hidden ${isDarkMode ? 'bg-slate-900 border border-slate-800' : 'bg-white'}`}>
        
        {/* Lado Esquerdo - Branding */}
        <div className="md:w-1/2 bg-[#061a1a] p-12 flex flex-col justify-between text-white relative">
          <div className="z-10">
            <div className="flex items-center gap-2 mb-12">
              <div className="bg-emerald-500 p-2 rounded-xl">
                <Car size={24} className="text-[#061a1a]" />
              </div>
              <span className="text-xl font-black tracking-tighter">SMART TRAFFIC FLOW</span>
            </div>
            <h1 className="text-4xl font-bold leading-tight mb-6">
              O futuro da <span className="text-emerald-400">mobilidade urbana</span> começa aqui.
            </h1>
            <p className="text-slate-400 text-sm">
              Monitore, analise e otimize o tráfego da sua cidade em tempo real.
            </p>
          </div>
          <div className="z-10 flex items-center gap-3 text-[10px] font-bold text-slate-500 uppercase tracking-widest">
            <div className="w-2 h-2 rounded-full bg-emerald-500 animate-pulse" />
            SPTrans & Open-Meteo Integrated
          </div>
        </div>

        {/* Lado Direito - Formulário */}
        <div className="md:w-1/2 p-10 md:p-14 flex flex-col justify-center">
          <h2 className={`text-2xl font-black mb-2 ${isDarkMode ? 'text-white' : 'text-slate-800'}`}>
            {isRegistering ? 'Criar Conta' : 'Bem-vindo'}
          </h2>
          <p className="text-slate-500 text-sm mb-8">
            {isRegistering 
              ? 'Cadastre-se para acessar o sistema' 
              : 'Acesse sua conta para visualizar o dashboard'}
          </p>

          {error && (
            <div className="mb-4 p-3 rounded-xl bg-red-500/10 border border-red-500/20 flex items-center gap-2 text-red-500 text-xs">
              <AlertCircle size={14} />
              {error}
            </div>
          )}

          {/* Formulário de Login */}
          {!isRegistering && (
            <form onSubmit={handleSubmit} className="space-y-4 mb-6">
              <div className="relative">
                <Mail className="absolute left-4 top-1/2 -translate-y-1/2 text-slate-400" size={16} />
                <input 
                  type="email" 
                  placeholder="E-mail" 
                  value={email}
                  onChange={(e) => setEmail(e.target.value)}
                  className={`w-full pl-12 pr-4 py-3 rounded-xl border outline-none text-sm transition-all focus:border-emerald-500 ${
                    isDarkMode ? 'bg-slate-800 border-slate-700 text-white' : 'bg-slate-50 border-slate-200'
                  }`} 
                  required 
                />
              </div>
              
              <div className="relative">
                <Lock className="absolute left-4 top-1/2 -translate-y-1/2 text-slate-400" size={16} />
                <input 
                  type="password" 
                  placeholder="Senha" 
                  value={password}
                  onChange={(e) => setPassword(e.target.value)}
                  className={`w-full pl-12 pr-4 py-3 rounded-xl border outline-none text-sm transition-all focus:border-emerald-500 ${
                    isDarkMode ? 'bg-slate-800 border-slate-700 text-white' : 'bg-slate-50 border-slate-200'
                  }`} 
                  required 
                />
              </div>
              
              <button 
                type="submit" 
                disabled={loading || authLoading}
                className="w-full py-4 bg-emerald-500 hover:bg-emerald-600 text-white rounded-xl font-black text-sm transition-all disabled:opacity-50 disabled:cursor-not-allowed"
              >
                {(loading || authLoading) ? 'CARREGANDO...' : 'ENTRAR NO SISTEMA'}
              </button>
            </form>
          )}

          {/* Formulário de Registro */}
          {isRegistering && (
            <form onSubmit={handleRegister} className="space-y-4 mb-6">
              <div className="relative">
                <User className="absolute left-4 top-1/2 -translate-y-1/2 text-slate-400" size={16} />
                <input 
                  type="text" 
                  placeholder="Nome completo" 
                  value={registerData.nome}
                  onChange={(e) => setRegisterData({...registerData, nome: e.target.value})}
                  className={`w-full pl-12 pr-4 py-3 rounded-xl border outline-none text-sm transition-all focus:border-emerald-500 ${
                    isDarkMode ? 'bg-slate-800 border-slate-700 text-white' : 'bg-slate-50 border-slate-200'
                  }`} 
                  required 
                />
              </div>

              <div className="relative">
                <Mail className="absolute left-4 top-1/2 -translate-y-1/2 text-slate-400" size={16} />
                <input 
                  type="email" 
                  placeholder="E-mail" 
                  value={registerData.email}
                  onChange={(e) => setRegisterData({...registerData, email: e.target.value})}
                  className={`w-full pl-12 pr-4 py-3 rounded-xl border outline-none text-sm transition-all focus:border-emerald-500 ${
                    isDarkMode ? 'bg-slate-800 border-slate-700 text-white' : 'bg-slate-50 border-slate-200'
                  }`} 
                  required 
                />
              </div>

              <div className="relative">
                <Phone className="absolute left-4 top-1/2 -translate-y-1/2 text-slate-400" size={16} />
                <input 
                  type="tel" 
                  placeholder="Telefone (opcional)" 
                  value={registerData.telefone}
                  onChange={(e) => setRegisterData({...registerData, telefone: e.target.value})}
                  className={`w-full pl-12 pr-4 py-3 rounded-xl border outline-none text-sm transition-all focus:border-emerald-500 ${
                    isDarkMode ? 'bg-slate-800 border-slate-700 text-white' : 'bg-slate-50 border-slate-200'
                  }`} 
                />
              </div>
              
              <div className="relative">
                <Lock className="absolute left-4 top-1/2 -translate-y-1/2 text-slate-400" size={16} />
                <input 
                  type="password" 
                  placeholder="Senha (mínimo 6 caracteres)" 
                  value={registerData.senha}
                  onChange={(e) => setRegisterData({...registerData, senha: e.target.value})}
                  className={`w-full pl-12 pr-4 py-3 rounded-xl border outline-none text-sm transition-all focus:border-emerald-500 ${
                    isDarkMode ? 'bg-slate-800 border-slate-700 text-white' : 'bg-slate-50 border-slate-200'
                  }`} 
                  required 
                />
              </div>

              <div className="relative">
                <Lock className="absolute left-4 top-1/2 -translate-y-1/2 text-slate-400" size={16} />
                <input 
                  type="password" 
                  placeholder="Confirmar senha" 
                  value={registerData.confirmSenha}
                  onChange={(e) => setRegisterData({...registerData, confirmSenha: e.target.value})}
                  className={`w-full pl-12 pr-4 py-3 rounded-xl border outline-none text-sm transition-all focus:border-emerald-500 ${
                    isDarkMode ? 'bg-slate-800 border-slate-700 text-white' : 'bg-slate-50 border-slate-200'
                  }`} 
                  required 
                />
              </div>
              
              <button 
                type="submit" 
                disabled={loading || authLoading}
                className="w-full py-4 bg-emerald-500 hover:bg-emerald-600 text-white rounded-xl font-black text-sm transition-all disabled:opacity-50 disabled:cursor-not-allowed"
              >
                {(loading || authLoading) ? 'CADASTRANDO...' : 'CRIAR CONTA'}
              </button>
            </form>
          )}

          {/* Link para alternar entre login e registro */}
          <div className="text-center">
            <p className="text-xs text-slate-500">
              {isRegistering ? 'Já tem uma conta?' : 'Não tem uma conta?'}
              {' '}
              <button 
                onClick={toggleMode}
                className="text-emerald-500 hover:text-emerald-400 font-bold transition-colors"
                type="button"
              >
                {isRegistering ? 'Faça login' : 'Cadastre-se'}
              </button>
            </p>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Login;