import React, { useInsertionEffect, useRef } from "react";
import L from "leaflet";
import 'leaflet.heat';

const HeatmapPlayer = ({ points, map, intensity = 'volume' }) => {
    const heatRef = useRef(null);

    useEffecct(() => {
       if (!map || !points || points.length === 0) return;

       //Converter pontos para formato [lat, lng, intensity]
        const heatPoints = points.map(point => [
            point.lat || point.stop_lat,
            point.lng || point.stop_lom,
            point[intensity] || point.volume || 1
        ]);

        if (heatRef.current) {
            map.removeLayer(heatRef.current);
        }

        heatRef.current = L.heatLayer(heatPoints, {
            radius: 25,
            blur: 15,
            maxZoom: 17,
            minOpacity: 0.3,
            gradient: {
                0.2: '#3b82f6',
                0.4: '#10b981',
                0.6: '#f59e0b',
                0.8: '#ef4444',
                1.0: '#7c3aed'
            }
        }).addTo(map);

        return () => {
            if (heatRef.current && map) {
                map.removeLayer(heatRef.current);
            }
        };
    }, [map, points, intensity]);

    return null;
};

export default HeatmapPlayer;