import React, { useEffect } from 'react';
import { MapContainer, TileLayer, Marker, Polyline, useMap, Popup } from 'react-leaflet';
import L from 'leaflet';
import 'leaflet/dist/leaflet.css';

// Ícone personalizado para o Ônibus
const busIcon = new L.Icon({
  iconUrl: 'https://cdn-icons-png.flaticon.com/512/3448/3448339.png',
  iconSize: [25, 25],
  iconAnchor: [12, 12],
  popupAnchor: [0, -10]
});

// Ícone para origem
const originIcon = new L.Icon({
  iconUrl: 'https://cdn-icons-png.flaticon.com/512/684/684908.png',
  iconSize: [30, 30],
  iconAnchor: [15, 30],
  popupAnchor: [0, -15]
});

// Ícone para destino
const destIcon = new L.Icon({
  iconUrl: 'https://cdn-icons-png.flaticon.com/512/684/684809.png',
  iconSize: [30, 30],
  iconAnchor: [15, 30],
  popupAnchor: [0, -15]
});

function ZoomHandler({ pontos }) {
  const map = useMap();
  useEffect(() => {
    if (pontos?.origem && pontos?.destino) {
      const bounds = L.latLngBounds(
        [pontos.origem.lat, pontos.origem.lon],
        [pontos.destino.lat, pontos.destino.lon]
      );
      map.fitBounds(bounds, { padding: [50, 50] });
    }
  }, [pontos, map]);
  return null;
}

export default function DashboardMap({ pontos, rota, busPositions }) {
  const defaultPos = [-23.5505, -46.6333];
  
  // Processar coordenadas da rota (se existir)
  const polylineCoords = [];
  if (rota) {
    if (rota.coordinates && Array.isArray(rota.coordinates)) {
      // Formato: [[lng, lat], ...]
      rota.coordinates.forEach(coord => {
        polylineCoords.push([coord[1], coord[0]]);
      });
    } else if (rota.geometria && rota.geometria.coordinates) {
      // Formato GeoJSON
      rota.geometria.coordinates.forEach(coord => {
        polylineCoords.push([coord[1], coord[0]]);
      });
    }
  }

  console.log('🚌 BusPositions recebidas:', busPositions);
  console.log('📍 Pontos:', pontos);

  return (
    <MapContainer center={defaultPos} zoom={12} style={{ height: '100%', width: '100%' }}>
      <TileLayer url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png" />
      
      {/* Marcador de Origem */}
      {pontos?.origem && (
        <Marker 
          position={[pontos.origem.lat, pontos.origem.lon]} 
          icon={originIcon}
        >
          <Popup>
            <div className="text-sm">
              <strong>🚩 Origem</strong><br />
              {pontos.origem.nome?.substring(0, 50)}
            </div>
          </Popup>
        </Marker>
      )}
      
      {/* Marcador de Destino */}
      {pontos?.destino && (
        <Marker 
          position={[pontos.destino.lat, pontos.destino.lon]} 
          icon={destIcon}
        >
          <Popup>
            <div className="text-sm">
              <strong>🎯 Destino</strong><br />
              {pontos.destino.nome?.substring(0, 50)}
            </div>
          </Popup>
        </Marker>
      )}

      {/* Rota Calculada */}
      {polylineCoords.length > 0 && (
        <Polyline 
          positions={polylineCoords} 
          color="#10b981" 
          weight={6} 
          opacity={0.8} 
          dashArray="5, 10" 
        />
      )}

      {/* ✅ CORREÇÃO: Renderização dos ÔNIBUS da SPTrans */}
      {busPositions && Array.isArray(busPositions) && busPositions.map((veiculo, idx) => {
        // Verificar se o veículo tem as coordenadas corretas
        const lat = veiculo.p;
        const lng = veiculo.x;
        const line = veiculo.l;
        
        if (lat && lng) {
          return (
            <Marker 
              key={`bus-${idx}-${line}`} 
              position={[lat, lng]} 
              icon={busIcon}
            >
              <Popup>
                <div className="text-xs font-sans">
                  <p className="font-bold text-emerald-600">🚌 Linha: {line || 'N/A'}</p>
                  <p>Código: {veiculo.c || 'N/A'}</p>
                  <p>Status: {veiculo.a ? 'Ativo' : 'Inativo'}</p>
                  <p className="text-slate-500 text-[10px] mt-1">
                    Lat: {lat.toFixed(4)}<br />
                    Lng: {lng.toFixed(4)}
                  </p>
                </div>
              </Popup>
            </Marker>
          );
        }
        return null;
      })}

      <ZoomHandler pontos={pontos} />
    </MapContainer>
  );
}