import React, { createContext, useContext, useState, useEffect } from "react";
import { authService } from "../services/api";

const AuthContext = createContext();

export const AuthProvider = ({ children }) => {
  const [authenticated, setAuthenticated] = useState(false);
  const [loading, setLoading] = useState(true);
  const [user, setUser] = useState(null);

  // Verifica se o usuário já está logado ao carregar a página
  useEffect(() => {
    const checkAuth = () => {
      const token = localStorage.getItem("token");
      
      if (token && token !== "undefined" && token !== "null" && token.trim() !== "") {
        setAuthenticated(true);
        const savedUser = localStorage.getItem("user");
        if (savedUser) {
          try {
            setUser(JSON.parse(savedUser));
          } catch (e) {
            console.error("Erro ao recuperar dados do usuário:", e);
          }
        }
      } else {
        // Se o token for inválido, limpa tudo
        logout();
      }
      setLoading(false);
    };

    checkAuth();
  }, []);

  const login = async (email, password) => {
    try {
      // O objeto enviado deve bater com o record DadosLogin(String email, String senha)
      const response = await authService.login({
        email: email,
        senha: password 
      });

      if (response && response.token) {
        localStorage.setItem("token", response.token);
        
        if (response.user) {
          localStorage.setItem("user", JSON.stringify(response.user));
          setUser(response.user);
        }
        
        setAuthenticated(true);
        return { success: true };
      } else {
        throw new Error("Resposta do servidor sem token");
      }
    } catch (error) {
      console.error("Erro no login:", error);
      // Retorna a mensagem vinda do Map.of("message", ...) do Java
      return { 
        success: false, 
        error: error.response?.data?.message || "E-mail ou senha incorretos." 
      };
    }
  };

  const register = async (userData) => {
    try {
      // 1. Desestruturação para remover o que o Java não reconhece (telefone e confirmSenha)
      // 2. Mantemos apenas nome, email e senha para o record RegisterDTO
      const { nome, email, senha } = userData;
      
      const dadosParaEnviar = { nome, email, senha };

      const response = await authService.register(dadosParaEnviar);

      if (response && response.token) {
        localStorage.setItem("token", response.token);
        
        if (response.user) {
          localStorage.setItem("user", JSON.stringify(response.user));
          setUser(response.user);
        }
        
        setAuthenticated(true);
        return { success: true };
      } else {
        throw new Error("Falha ao processar registro");
      }
    } catch (error) {
      console.error("Erro no registro:", error);
      return { 
        success: false, 
        error: error.response?.data?.message || "Erro ao criar conta. Tente novamente." 
      };
    }
  };

  const logout = () => {
    localStorage.removeItem("token");
    localStorage.removeItem("user");
    setAuthenticated(false);
    setUser(null);
  };

  return (
    <AuthContext.Provider value={{ 
      authenticated, 
      login,
      register,
      logout, 
      loading,
      user
    }}>
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = () => {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error("useAuth deve ser usado dentro de AuthProvider");
  }
  return context;
};