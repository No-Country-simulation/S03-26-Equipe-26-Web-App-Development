- =============================================================================
-- MIGRATION: SEED SMARTTRAFFICFLOW (POSTGRESQL + POSTGIS)
-- =============================================================================

-- Extensões espaciais
CREATE EXTENSION IF NOT EXISTS postgis;
CREATE EXTENSION IF NOT EXISTS postgis_topology;

-- Limpeza
TRUNCATE TABLE traffic_data RESTART IDENTITY CASCADE;

-- Madrugada
INSERT INTO traffic_data
(idvia, nome, hora, volume, capacidade, nivel, alerta, geom, tipo)
VALUES
(1, 'Av. Central', '2026-03-28 00:00:00', 202, 1000, 20.2, 'NORMAL',
 ST_SetSRID(ST_Point(-46.6333, -23.5505),4326), 'ARTERIAL'),

(2, 'Rodovia do Aeroporto', '2026-03-28 00:00:00', 507, 2000, 25.35, 'NORMAL',
 ST_SetSRID(ST_Point(-46.4731, -23.4356),4326), 'RODOVIA'),

(3, 'Rua das Flores', '2026-03-28 00:00:00', 84, 300, 28.0, 'NORMAL',
 ST_SetSRID(ST_Point(-46.6765, -23.5855),4326), 'RESIDENCIAL');