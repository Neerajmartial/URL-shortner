import axios from "axios";

const baseURL =
  import.meta.env.VITE_BACKEND_URL || "http://localhost:8080";

console.log("BASE URL:", baseURL);

// ✅ Create instance
const api = axios.create({
  baseURL,
});

// ✅ Use interceptor on instance
api.interceptors.request.use((config) => {
  const token = localStorage.getItem("JWT_TOKEN") || null;

  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }

  return config;
});

// ✅ Export instance
export default api;