import { useState } from 'react'
import './App.css'
import Home from './pages/home/Home'
import Login from './pages/login/Login'

function App() {

  const [ statusLogin, setStatusLogin] = useState(false)

  return (
    <>    
      {statusLogin ? <Login /> : <Home />}      
    </>
  )
}

export default App
