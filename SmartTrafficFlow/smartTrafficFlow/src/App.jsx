import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import { useAuth } from './context/AuthContext'; 
import Login from './pages/Login/Login';
import Dashboard from './pages/Dashboard/Dashboard';

function App() {
  const { authenticated, loading } = useAuth();

  // Enquanto o useEffect do AuthContext checa o token, mostramos um vazio ou loading
  if (loading) {
    return <div style={{ background: '#0b0e14', height: '100vh' }} />;
  }

  return (
    <Router>
      <Routes>
        {/* Se autenticado, redireciona o /login para o /dashboard */}
        <Route 
          path="/login" 
          element={authenticated ? <Navigate to="/dashboard" replace /> : <Login />} 
        />
        
        {/* Rota Protegida */}
        <Route 
          path="/dashboard" 
          element={authenticated ? <Dashboard /> : <Navigate to="/login" replace />} 
        />

        {/* Redirecionamento da Raiz */}
        <Route 
          path="/" 
          element={authenticated ? <Navigate to="/dashboard" replace /> : <Navigate to="/login" replace />} 
        />

        {/* Catch-all */}
        <Route path="*" element={<Navigate to="/login" replace />} />
      </Routes>
    </Router>
  );
}

export default App;