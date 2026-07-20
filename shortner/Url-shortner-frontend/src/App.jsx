import { useState } from 'react'
import reactLogo from './assets/react.svg'
import viteLogo from '/vite.svg'
import './App.css'
import NavBar from './Components/Navbar'
import { BrowserRouter, Routes ,Route} from 'react-router-dom'
import Landingpage from './Components/Landingpage'
import AboutPage from './Components/AboutPage'
import Footer from './Components/Footer'
import RegisterPage from './Components/RegisterPage'
import { Toaster } from 'react-hot-toast'
import LoginPage from './Components/LoginPage'
import DashboardLayout from './Dashboard/DashboardLayout'

function App() {
  const [count, setCount] = useState(0)

  return (
    <>
          <NavBar />
          <Toaster position='bottom-center' />
          <Routes>
            <Route path='/'element={<Landingpage />}/>
            <Route path='/aboutpage' element={<AboutPage />}/>
            <Route path='/register' element={<RegisterPage />}/>
            <Route path='/login' element={<LoginPage />}/>
            <Route path='/dashboard' element={<DashboardLayout />}/>
          </Routes>
          <Footer />
    </>
  )
}

export default App
