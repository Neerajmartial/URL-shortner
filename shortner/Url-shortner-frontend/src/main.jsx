import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import { BrowserRouter } from 'react-router-dom'
import './index.css'
import App from './App.jsx'
import { ContextProvider } from './contextApi/ContextApi' // ✅ add this

createRoot(document.getElementById('root')).render(
  <StrictMode>
    <ContextProvider>   {/* ✅ wrap here */}
      <BrowserRouter>   {/* also needed for routing */}
        <App />
      </BrowserRouter>
    </ContextProvider>
  </StrictMode>,
)