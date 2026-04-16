import axios from 'axios';

const JAVA_URL = 'http://localhost:8080';
const PYTHON_URL = 'http://localhost:8000';

// Instância para o Backend Java
export const javaApi = axios.create({
  baseURL: JAVA_URL,
  timeout: 15000,
  headers: { 'Content-Type': 'application/json' }
});

// Instância para o Backend Python
export const pythonApi = axios.create({
  baseURL: PYTHON_URL,
  timeout: 15000,
  headers: { 'Content-Type': 'application/json' }
});

// --- INTERCEPTORES (Segurança JWT) ---

javaApi.interceptors.request.use((config) => {
  const token = localStorage.getItem('token');
  if (token && token.trim() !== '') {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
}, (error) => Promise.reject(error));

javaApi.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 401) {
      localStorage.removeItem('token');
      window.location.href = '/login';
    }
    return Promise.reject(error);
  }
);

// --- SERVIÇOS ---

export const authService = {
  login: async (data) => (await javaApi.post('/auth/login', data)).data,
  register: async (data) => (await javaApi.post('/auth/register', data)).data,
  verifyToken: async () => (await javaApi.get('/auth/verify')).data
};

export const geocodeService = {
  searchAddress: async (endereco) => {
    if (!endereco?.trim()) return null;
    try {
      const response = await axios.get('https://nominatim.openstreetmap.org/search', {
        params: { q: `${endereco}, São Paulo, Brasil`, format: 'json', limit: 1, addressdetails: 1 }
      });
      if (response.data?.length > 0) {
        return {
          lat: parseFloat(response.data[0].lat),
          lon: parseFloat(response.data[0].lon),
          nome: response.data[0].display_name
        };
      }
      return null;
    } catch (e) { 
      console.error("Erro na geocodificação:", e);
      return null; 
    }
  }
};

export const transporteService = {
  // SPTrans (Java - Mock)
  getRealTimeBuses: async () => {
    try {
      const response = await javaApi.get('/traffic/sptrans/posicao');
      return response.data;
    } catch (e) {
      console.error("Erro ao buscar SPTrans:", e);
      return null;
    }
  },

  // Rota com Java (OSRM - Carro)
  calculateRoute: async (cidade, origem, destino, modo = 'car') => {
    try {
      const response = await javaApi.get('/traffic/route', {
        params: { 
          cidade: cidade || 'sao_paulo', 
          origem: `${origem.lat},${origem.lon}`, 
          destino: `${destino.lat},${destino.lon}`, 
          modo 
        }
      });
      return response.data;
    } catch (e) { 
      console.error("Erro calculateRoute (Java):", e);
      return null; 
    }
  },

  // ✅ Rota de Transporte Público com Python (GTFS)
  getPublicTransitRoute: async (origemLat, origemLon, destinoLat, destinoLon, cidade = 'sao_paulo') => {
    try {
      const response = await pythonApi.get('/gtfs/public-transit', {
        params: {
          origin_lat: origemLat,
          origin_lon: origemLon,
          destination_lat: destinoLat,
          destination_lon: destinoLon,
          cidade: cidade
        }
      });
      return response.data;
    } catch (e) {
      console.error("Erro ao buscar rota de transporte público (Python):", e);
      return null;
    }
  },

  // ✅ Buscar paradas próximas
  getNearbyStops: async (cidade, lat, lon, raioKm = 1.0) => {
    try {
      const response = await pythonApi.get(`/gtfs/stops/nearby/${cidade}`, {
        params: { lat, lon, raio_km: raioKm }
      });
      return response.data;
    } catch (e) {
      console.error("Erro ao buscar paradas próximas:", e);
      return null;
    }
  },

  // ✅ Buscar linhas de uma cidade
  getRoutes: async (cidade) => {
    try {
      const response = await pythonApi.get(`/gtfs/routes/${cidade}`);
      return response.data;
    } catch (e) {
      console.error("Erro ao buscar linhas:", e);
      return null;
    }
  },

  // ✅ Buscar rota de ônibus específica
  getBusRoute: async (busLine, origemLat, origemLon, destinoLat, destinoLon, cidade = 'sao_paulo') => {
    try {
      const response = await pythonApi.get('/gtfs/bus-route', {
        params: {
          bus_line: busLine,
          origin_lat: origemLat,
          origin_lon: origemLon,
          destination_lat: destinoLat,
          destination_lon: destinoLon,
          cidade: cidade
        }
      });
      return response.data;
    } catch (e) {
      console.error("Erro ao buscar rota de ônibus:", e);
      return null;
    }
  },

  saveHistory: async (data) => await javaApi.post('/traffic/history', data)
};

export const trafficService = {
  getDashboardComplete: async (query, clima = null, nivel = null) => {
    try {
      const response = await javaApi.get('/traffic/dashboard-complete', {
        params: { q: query, clima, nivel }
      });
      return response.data; 
    } catch (error) {
      console.error("Erro no fetch do dashboard completo:", error);
      return null;
    }
  },

  getTrafficVolume: async (lat, lon) => {
    try {
      const response = await javaApi.get('/traffic/traffic-volume', { 
        params: { lat, lon } 
      });
      return response.data;
    } catch (error) {
      console.error("Erro ao buscar volume de tráfego:", error);
      return null;
    }
  },

  getTrafficVolumeForRoute: async (origemLat, origemLon, destinoLat, destinoLon) => {
    try {
      const [volumeOrigem, volumeDestino] = await Promise.all([
        trafficService.getTrafficVolume(origemLat, origemLon),
        trafficService.getTrafficVolume(destinoLat, destinoLon)
      ]);
      
      return {
        origem: volumeOrigem,
        destino: volumeDestino
      };
    } catch (error) {
      console.error("Erro ao buscar volume para rota:", error);
      return null;
    }
  },

  getAreaTrafficVolume: async () => {
    try {
      const response = await javaApi.get('/traffic/traffic-volume-area');
      return response.data;
    } catch (error) {
      console.error("Erro ao buscar volume por área:", error);
      return [
        { location: "Av. Paulista", hour: "12h", volume: 85, currentSpeed: 25, status: "MODERADO" },
        { location: "Av. Faria Lima", hour: "12h", volume: 120, currentSpeed: 15, status: "CONGESTIONADO" },
        { location: "Marginal Tietê", hour: "12h", volume: 95, currentSpeed: 35, status: "MODERADO" },
        { location: "Av. Interlagos", hour: "12h", volume: 45, currentSpeed: 45, status: "LIVRE" }
      ];
    }
  },

  getVolumeStats: async () => {
    try {
        const response = await javaApi.get('/traffic');
        return response.data;
    } catch (e) { 
      console.error("Erro ao buscar volume stats:", e);
      return null; 
    }
  },

  getCityAnalytics: async (cidade) => {
    try {
      const response = await pythonApi.get(`/analytics/analise/${cidade}`);
      return response.data;
    } catch (e) { 
      console.error("Erro ao buscar analytics do Python:", e);
      return null; 
    }
  },

  getTrafficForecast: async (lat, lon, hours = 24) => {
    try {
      const response = await pythonApi.get('/analytics/forecast', {
        params: { lat, lon, hours }
      });
      return response.data;
    } catch (e) {
      console.error("Erro ao buscar previsão de tráfego:", e);
      return null;
    }
  }
};

export const weatherService = {
  getCurrentWeather: async (lat, lon) => {
    try {
      const response = await axios.get('https://api.open-meteo.com/v1/forecast', {
        params: { 
          latitude: lat, 
          longitude: lon, 
          current: 'temperature_2m,wind_speed_10m,precipitation', 
          timezone: 'America/Sao_Paulo' 
        }
      });
      return response.data.current;
    } catch (e) { 
      console.error("Erro ao buscar clima:", e);
      return null; 
    }
  },

  getWeatherForecast: async (lat, lon, days = 7) => {
    try {
      const response = await axios.get('https://api.open-meteo.com/v1/forecast', {
        params: {
          latitude: lat,
          longitude: lon,
          daily: 'temperature_2m_max,temperature_2m_min,precipitation_sum',
          timezone: 'America/Sao_Paulo',
          forecast_days: days
        }
      });
      return response.data.daily;
    } catch (e) {
      console.error("Erro ao buscar previsão do tempo:", e);
      return null;
    }
  }
};

export default {
  javaApi,
  pythonApi,
  authService,
  geocodeService,
  transporteService,
  trafficService,
  weatherService
};