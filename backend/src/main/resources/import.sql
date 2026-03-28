-- Ativa as funções espaciais no H2
CREATE ALIAS IF NOT EXISTS H2GIS_SPATIAL FOR "org.h2gis.functions.factory.H2GISFunctions.load";
CALL H2GIS_SPATIAL();

-- Inserções Corrigidas com a coluna 'tipo' (Obrigatória no seu Schema)

-- 00:00
INSERT INTO traffic_data (idvia, nome, hora, volume, capacidade, nivel, alerta, geom, tipo) VALUES (1, 'Av. Central', '2026-03-28 00:00:00', 202, 1000, 20.2, 'NORMAL', ST_GeomFromText('POINT(-46.6333 -23.5505)', 4326), 'ARTERIAL');
INSERT INTO traffic_data (idvia, nome, hora, volume, capacidade, nivel, alerta, geom, tipo) VALUES (2, 'Rodovia do Aeroporto', '2026-03-28 00:00:00', 507, 2000, 25.35, 'NORMAL', ST_GeomFromText('POINT(-46.4731 -23.4356)', 4326), 'RODOVIA');
INSERT INTO traffic_data (idvia, nome, hora, volume, capacidade, nivel, alerta, geom, tipo) VALUES (3, 'Rua das Flores', '2026-03-28 00:00:00', 84, 300, 28.0, 'NORMAL', ST_GeomFromText('POINT(-46.6765 -23.5855)', 4326), 'RESIDENCIAL');

-- 01:00
INSERT INTO traffic_data (idvia, nome, hora, volume, capacidade, nivel, alerta, geom, tipo) VALUES (1, 'Av. Central', '2026-03-28 01:00:00', 55, 1000, 5.5, 'NORMAL', ST_GeomFromText('POINT(-46.6333 -23.5505)', 4326), 'ARTERIAL');
INSERT INTO traffic_data (idvia, nome, hora, volume, capacidade, nivel, alerta, geom, tipo) VALUES (2, 'Rodovia do Aeroporto', '2026-03-28 01:00:00', 243, 2000, 12.15, 'NORMAL', ST_GeomFromText('POINT(-46.4731 -23.4356)', 4326), 'RODOVIA');
INSERT INTO traffic_data (idvia, nome, hora, volume, capacidade, nivel, alerta, geom, tipo) VALUES (3, 'Rua das Flores', '2026-03-28 01:00:00', 90, 300, 30.0, 'NORMAL', ST_GeomFromText('POINT(-46.6765 -23.5855)', 4326), 'RESIDENCIAL');

-- 02:00
INSERT INTO traffic_data (idvia, nome, hora, volume, capacidade, nivel, alerta, geom, tipo) VALUES (1, 'Av. Central', '2026-03-28 02:00:00', 58, 1000, 5.8, 'NORMAL', ST_GeomFromText('POINT(-46.6333 -23.5505)', 4326), 'ARTERIAL');
INSERT INTO traffic_data (idvia, nome, hora, volume, capacidade, nivel, alerta, geom, tipo) VALUES (2, 'Rodovia do Aeroporto', '2026-03-28 02:00:00', 729, 2000, 36.45, 'ANOMALIA', ST_GeomFromText('POINT(-46.4731 -23.4356)', 4326), 'RODOVIA');
INSERT INTO traffic_data (idvia, nome, hora, volume, capacidade, nivel, alerta, geom, tipo) VALUES (3, 'Rua das Flores', '2026-03-28 02:00:00', 109, 300, 36.33, 'ANOMALIA', ST_GeomFromText('POINT(-46.6765 -23.5855)', 4326), 'RESIDENCIAL');

-- 03:00
INSERT INTO traffic_data (idvia, nome, hora, volume, capacidade, nivel, alerta, geom, tipo) VALUES (1, 'Av. Central', '2026-03-28 03:00:00', 450, 1000, 45.0, 'ANOMALIA', ST_GeomFromText('POINT(-46.6333 -23.5505)', 4326), 'ARTERIAL');
INSERT INTO traffic_data (idvia, nome, hora, volume, capacidade, nivel, alerta, geom, tipo) VALUES (2, 'Rodovia do Aeroporto', '2026-03-28 03:00:00', 879, 2000, 43.95, 'ANOMALIA', ST_GeomFromText('POINT(-46.4731 -23.4356)', 4326), 'RODOVIA');
INSERT INTO traffic_data (idvia, nome, hora, volume, capacidade, nivel, alerta, geom, tipo) VALUES (3, 'Rua das Flores', '2026-03-28 03:00:00', 104, 300, 34.67, 'ANOMALIA', ST_GeomFromText('POINT(-46.6765 -23.5855)', 4326), 'RESIDENCIAL');

-- 04:00
INSERT INTO traffic_data (idvia, nome, hora, volume, capacidade, nivel, alerta, geom, tipo) VALUES (1, 'Av. Central', '2026-03-28 04:00:00', 167, 1000, 16.7, 'NORMAL', ST_GeomFromText('POINT(-46.6333 -23.5505)', 4326), 'ARTERIAL');
INSERT INTO traffic_data (idvia, nome, hora, volume, capacidade, nivel, alerta, geom, tipo) VALUES (2, 'Rodovia do Aeroporto', '2026-03-28 04:00:00', 501, 2000, 25.05, 'NORMAL', ST_GeomFromText('POINT(-46.4731 -23.4356)', 4326), 'RODOVIA');
INSERT INTO traffic_data (idvia, nome, hora, volume, capacidade, nivel, alerta, geom, tipo) VALUES (3, 'Rua das Flores', '2026-03-28 04:00:00', 137, 300, 45.67, 'ANOMALIA', ST_GeomFromText('POINT(-46.6765 -23.5855)', 4326), 'RESIDENCIAL');

-- 05:00
INSERT INTO traffic_data (idvia, nome, hora, volume, capacidade, nivel, alerta, geom, tipo) VALUES (1, 'Av. Central', '2026-03-28 05:00:00', 460, 1000, 46.0, 'NORMAL', ST_GeomFromText('POINT(-46.6333 -23.5505)', 4326), 'ARTERIAL');
INSERT INTO traffic_data (idvia, nome, hora, volume, capacidade, nivel, alerta, geom, tipo) VALUES (2, 'Rodovia do Aeroporto', '2026-03-28 05:00:00', 386, 2000, 19.3, 'NORMAL', ST_GeomFromText('POINT(-46.4731 -23.4356)', 4326), 'RODOVIA');
INSERT INTO traffic_data (idvia, nome, hora, volume, capacidade, nivel, alerta, geom, tipo) VALUES (3, 'Rua das Flores', '2026-03-28 05:00:00', 74, 300, 24.67, 'NORMAL', ST_GeomFromText('POINT(-46.6765 -23.5855)', 4326), 'RESIDENCIAL');

-- 06:00
INSERT INTO traffic_data (idvia, nome, hora, volume, capacidade, nivel, alerta, geom, tipo) VALUES (1, 'Av. Central', '2026-03-28 06:00:00', 302, 1000, 30.2, 'NORMAL', ST_GeomFromText('POINT(-46.6333 -23.5505)', 4326), 'ARTERIAL');
INSERT INTO traffic_data (idvia, nome, hora, volume, capacidade, nivel, alerta, geom, tipo) VALUES (2, 'Rodovia do Aeroporto', '2026-03-28 06:00:00', 847, 2000, 42.35, 'NORMAL', ST_GeomFromText('POINT(-46.4731 -23.4356)', 4326), 'RODOVIA');
INSERT INTO traffic_data (idvia, nome, hora, volume, capacidade, nivel, alerta, geom, tipo) VALUES (3, 'Rua das Flores', '2026-03-28 06:00:00', 83, 300, 27.67, 'NORMAL', ST_GeomFromText('POINT(-46.6765 -23.5855)', 4326), 'RESIDENCIAL');

-- 07:00
INSERT INTO traffic_data (idvia, nome, hora, volume, capacidade, nivel, alerta, geom, tipo) VALUES (1, 'Av. Central', '2026-03-28 07:00:00', 223, 1000, 22.3, 'NORMAL', ST_GeomFromText('POINT(-46.6333 -23.5505)', 4326), 'ARTERIAL');
INSERT INTO traffic_data (idvia, nome, hora, volume, capacidade, nivel, alerta, geom, tipo) VALUES (2, 'Rodovia do Aeroporto', '2026-03-28 07:00:00', 75, 2000, 3.75, 'NORMAL', ST_GeomFromText('POINT(-46.4731 -23.4356)', 4326), 'RODOVIA');
INSERT INTO traffic_data (idvia, nome, hora, volume, capacidade, nivel, alerta, geom, tipo) VALUES (3, 'Rua das Flores', '2026-03-28 07:00:00', 91, 300, 30.33, 'NORMAL', ST_GeomFromText('POINT(-46.6765 -23.5855)', 4326), 'RESIDENCIAL');

-- 08:00
INSERT INTO traffic_data (idvia, nome, hora, volume, capacidade, nivel, alerta, geom, tipo) VALUES (1, 'Av. Central', '2026-03-28 08:00:00', 854, 1000, 85.4, 'NORMAL', ST_GeomFromText('POINT(-46.6333 -23.5505)', 4326), 'ARTERIAL');
INSERT INTO traffic_data (idvia, nome, hora, volume, capacidade, nivel, alerta, geom, tipo) VALUES (2, 'Rodovia do Aeroporto', '2026-03-28 08:00:00', 384, 2000, 19.2, 'NORMAL', ST_GeomFromText('POINT(-46.4731 -23.4356)', 4326), 'RODOVIA');
INSERT INTO traffic_data (idvia, nome, hora, volume, capacidade, nivel, alerta, geom, tipo) VALUES (3, 'Rua das Flores', '2026-03-28 08:00:00', 125, 300, 41.67, 'NORMAL', ST_GeomFromText('POINT(-46.6765 -23.5855)', 4326), 'RESIDENCIAL');

-- 09:00
INSERT INTO traffic_data (idvia, nome, hora, volume, capacidade, nivel, alerta, geom, tipo) VALUES (1, 'Av. Central', '2026-03-28 09:00:00', 254, 1000, 25.4, 'NORMAL', ST_GeomFromText('POINT(-46.6333 -23.5505)', 4326), 'ARTERIAL');
INSERT INTO traffic_data (idvia, nome, hora, volume, capacidade, nivel, alerta, geom, tipo) VALUES (2, 'Rodovia do Aeroporto', '2026-03-28 09:00:00', 389, 2000, 19.45, 'NORMAL', ST_GeomFromText('POINT(-46.4731 -23.4356)', 4326), 'RODOVIA');
INSERT INTO traffic_data (idvia, nome, hora, volume, capacidade, nivel, alerta, geom, tipo) VALUES (3, 'Rua das Flores', '2026-03-28 09:00:00', 64, 300, 21.33, 'NORMAL', ST_GeomFromText('POINT(-46.6765 -23.5855)', 4326), 'RESIDENCIAL');

-- 10:00
INSERT INTO traffic_data (idvia, nome, hora, volume, capacidade, nivel, alerta, geom, tipo) VALUES (1, 'Av. Central', '2026-03-28 10:00:00', 308, 1000, 30.8, 'NORMAL', ST_GeomFromText('POINT(-46.6333 -23.5505)', 4326), 'ARTERIAL');
INSERT INTO traffic_data (idvia, nome, hora, volume, capacidade, nivel, alerta, geom, tipo) VALUES (2, 'Rodovia do Aeroporto', '2026-03-28 10:00:00', 224, 2000, 11.2, 'NORMAL', ST_GeomFromText('POINT(-46.4731 -23.4356)', 4326), 'RODOVIA');
INSERT INTO traffic_data (idvia, nome, hora, volume, capacidade, nivel, alerta, geom, tipo) VALUES (3, 'Rua das Flores', '2026-03-28 10:00:00', 71, 300, 23.67, 'NORMAL', ST_GeomFromText('POINT(-46.6765 -23.5855)', 4326), 'RESIDENCIAL');

-- 11:00
INSERT INTO traffic_data (idvia, nome, hora, volume, capacidade, nivel, alerta, geom, tipo) VALUES (1, 'Av. Central', '2026-03-28 11:00:00', 387, 1000, 38.7, 'NORMAL', ST_GeomFromText('POINT(-46.6333 -23.5505)', 4326), 'ARTERIAL');
INSERT INTO traffic_data (idvia, nome, hora, volume, capacidade, nivel, alerta, geom, tipo) VALUES (2, 'Rodovia do Aeroporto', '2026-03-28 11:00:00', 521, 2000, 26.05, 'NORMAL', ST_GeomFromText('POINT(-46.4731 -23.4356)', 4326), 'RODOVIA');
INSERT INTO traffic_data (idvia, nome, hora, volume, capacidade, nivel, alerta, geom, tipo) VALUES (3, 'Rua das Flores', '2026-03-28 11:00:00', 150, 300, 50.0, 'NORMAL', ST_GeomFromText('POINT(-46.6765 -23.5855)', 4326), 'RESIDENCIAL');

-- 12:00
INSERT INTO traffic_data (idvia, nome, hora, volume, capacidade, nivel, alerta, geom, tipo) VALUES (1, 'Av. Central', '2026-03-28 12:00:00', 474, 1000, 47.4, 'NORMAL', ST_GeomFromText('POINT(-46.6333 -23.5505)', 4326), 'ARTERIAL');
INSERT INTO traffic_data (idvia, nome, hora, volume, capacidade, nivel, alerta, geom, tipo) VALUES (2, 'Rodovia do Aeroporto', '2026-03-28 12:00:00', 570, 2000, 28.5, 'NORMAL', ST_GeomFromText('POINT(-46.4731 -23.4356)', 4326), 'RODOVIA');
INSERT INTO traffic_data (idvia, nome, hora, volume, capacidade, nivel, alerta, geom, tipo) VALUES (3, 'Rua das Flores', '2026-03-28 12:00:00', 87, 300, 29.0, 'NORMAL', ST_GeomFromText('POINT(-46.6765 -23.5855)', 4326), 'RESIDENCIAL');

-- 13:00 a 17:00 (Seguindo o mesmo padrão)
INSERT INTO traffic_data (idvia, nome, hora, volume, capacidade, nivel, alerta, geom, tipo) VALUES (1, 'Av. Central', '2026-03-28 13:00:00', 170, 1000, 17.0, 'NORMAL', ST_GeomFromText('POINT(-46.6333 -23.5505)', 4326), 'ARTERIAL');
INSERT INTO traffic_data (idvia, nome, hora, volume, capacidade, nivel, alerta, geom, tipo) VALUES (2, 'Rodovia do Aeroporto', '2026-03-28 13:00:00', 666, 2000, 33.3, 'NORMAL', ST_GeomFromText('POINT(-46.4731 -23.4356)', 4326), 'RODOVIA');
INSERT INTO traffic_data (idvia, nome, hora, volume, capacidade, nivel, alerta, geom, tipo) VALUES (3, 'Rua das Flores', '2026-03-28 13:00:00', 78, 300, 26.0, 'NORMAL', ST_GeomFromText('POINT(-46.6765 -23.5855)', 4326), 'RESIDENCIAL');

INSERT INTO traffic_data (idvia, nome, hora, volume, capacidade, nivel, alerta, geom, tipo) VALUES (1, 'Av. Central', '2026-03-28 14:00:00', 248, 1000, 24.8, 'NORMAL', ST_GeomFromText('POINT(-46.6333 -23.5505)', 4326), 'ARTERIAL');
INSERT INTO traffic_data (idvia, nome, hora, volume, capacidade, nivel, alerta, geom, tipo) VALUES (2, 'Rodovia do Aeroporto', '2026-03-28 14:00:00', 852, 2000, 42.6, 'NORMAL', ST_GeomFromText('POINT(-46.4731 -23.4356)', 4326), 'RODOVIA');
INSERT INTO traffic_data (idvia, nome, hora, volume, capacidade, nivel, alerta, geom, tipo) VALUES (3, 'Rua das Flores', '2026-03-28 14:00:00', 92, 300, 30.67, 'NORMAL', ST_GeomFromText('POINT(-46.6765 -23.5855)', 4326), 'RESIDENCIAL');

INSERT INTO traffic_data (idvia, nome, hora, volume, capacidade, nivel, alerta, geom, tipo) VALUES (1, 'Av. Central', '2026-03-28 15:00:00', 154, 1000, 15.4, 'NORMAL', ST_GeomFromText('POINT(-46.6333 -23.5505)', 4326), 'ARTERIAL');
INSERT INTO traffic_data (idvia, nome, hora, volume, capacidade, nivel, alerta, geom, tipo) VALUES (2, 'Rodovia do Aeroporto', '2026-03-28 15:00:00', 425, 2000, 21.25, 'NORMAL', ST_GeomFromText('POINT(-46.4731 -23.4356)', 4326), 'RODOVIA');
INSERT INTO traffic_data (idvia, nome, hora, volume, capacidade, nivel, alerta, geom, tipo) VALUES (3, 'Rua das Flores', '2026-03-28 15:00:00', 126, 300, 42.0, 'NORMAL', ST_GeomFromText('POINT(-46.6765 -23.5855)', 4326), 'RESIDENCIAL');

INSERT INTO traffic_data (idvia, nome, hora, volume, capacidade, nivel, alerta, geom, tipo) VALUES (1, 'Av. Central', '2026-03-28 16:00:00', 107, 1000, 10.7, 'NORMAL', ST_GeomFromText('POINT(-46.6333 -23.5505)', 4326), 'ARTERIAL');
INSERT INTO traffic_data (idvia, nome, hora, volume, capacidade, nivel, alerta, geom, tipo) VALUES (2, 'Rodovia do Aeroporto', '2026-03-28 16:00:00', 852, 2000, 42.6, 'NORMAL', ST_GeomFromText('POINT(-46.4731 -23.4356)', 4326), 'RODOVIA');
INSERT INTO traffic_data (idvia, nome, hora, volume, capacidade, nivel, alerta, geom, tipo) VALUES (3, 'Rua das Flores', '2026-03-28 16:00:00', 145, 300, 48.33, 'NORMAL', ST_GeomFromText('POINT(-46.6765 -23.5855)', 4326), 'RESIDENCIAL');

INSERT INTO traffic_data (idvia, nome, hora, volume, capacidade, nivel, alerta, geom, tipo) VALUES (1, 'Av. Central', '2026-03-28 17:00:00', 172, 1000, 17.2, 'NORMAL', ST_GeomFromText('POINT(-46.6333 -23.5505)', 4326), 'ARTERIAL');
INSERT INTO traffic_data (idvia, nome, hora, volume, capacidade, nivel, alerta, geom, tipo) VALUES (2, 'Rodovia do Aeroporto', '2026-03-28 17:00:00', 880, 2000, 44.0, 'NORMAL', ST_GeomFromText('POINT(-46.4731 -23.4356)', 4326), 'RODOVIA');
INSERT INTO traffic_data (idvia, nome, hora, volume, capacidade, nivel, alerta, geom, tipo) VALUES (3, 'Rua das Flores', '2026-03-28 17:00:00', 142, 300, 47.33, 'NORMAL', ST_GeomFromText('POINT(-46.6765 -23.5855)', 4326), 'RESIDENCIAL');

-- 18:00
INSERT INTO traffic_data (idvia, nome, hora, volume, capacidade, nivel, alerta, geom, tipo) VALUES (1, 'Av. Central', '2026-03-28 18:00:00', 889, 1000, 88.9, 'NORMAL', ST_GeomFromText('POINT(-46.6333 -23.5505)', 4326), 'ARTERIAL');
INSERT INTO traffic_data (idvia, nome, hora, volume, capacidade, nivel, alerta, geom, tipo) VALUES (2, 'Rodovia do Aeroporto', '2026-03-28 18:00:00', 967, 2000, 48.35, 'NORMAL', ST_GeomFromText('POINT(-46.4731 -23.4356)', 4326), 'RODOVIA');
INSERT INTO traffic_data (idvia, nome, hora, volume, capacidade, nivel, alerta, geom, tipo) VALUES (3, 'Rua das Flores', '2026-03-28 18:00:00', 63, 300, 21.0, 'NORMAL', ST_GeomFromText('POINT(-46.6765 -23.5855)', 4326), 'RESIDENCIAL');

-- 19:00 a 22:00
INSERT INTO traffic_data (idvia, nome, hora, volume, capacidade, nivel, alerta, geom, tipo) VALUES (1, 'Av. Central', '2026-03-28 19:00:00', 269, 1000, 26.9, 'NORMAL', ST_GeomFromText('POINT(-46.6333 -23.5505)', 4326), 'ARTERIAL');
INSERT INTO traffic_data (idvia, nome, hora, volume, capacidade, nivel, alerta, geom, tipo) VALUES (2, 'Rodovia do Aeroporto', '2026-03-28 19:00:00', 260, 2000, 13.0, 'NORMAL', ST_GeomFromText('POINT(-46.4731 -23.4356)', 4326), 'RODOVIA');
INSERT INTO traffic_data (idvia, nome, hora, volume, capacidade, nivel, alerta, geom, tipo) VALUES (3, 'Rua das Flores', '2026-03-28 19:00:00', 146, 300, 48.67, 'NORMAL', ST_GeomFromText('POINT(-46.6765 -23.5855)', 4326), 'RESIDENCIAL');

INSERT INTO traffic_data (idvia, nome, hora, volume, capacidade, nivel, alerta, geom, tipo) VALUES (1, 'Av. Central', '2026-03-28 20:00:00', 115, 1000, 11.5, 'NORMAL', ST_GeomFromText('POINT(-46.6333 -23.5505)', 4326), 'ARTERIAL');
INSERT INTO traffic_data (idvia, nome, hora, volume, capacidade, nivel, alerta, geom, tipo) VALUES (2, 'Rodovia do Aeroporto', '2026-03-28 20:00:00', 302, 2000, 15.1, 'NORMAL', ST_GeomFromText('POINT(-46.4731 -23.4356)', 4326), 'RODOVIA');
INSERT INTO traffic_data (idvia, nome, hora, volume, capacidade, nivel, alerta, geom, tipo) VALUES (3, 'Rua das Flores', '2026-03-28 20:00:00', 62, 300, 20.67, 'NORMAL', ST_GeomFromText('POINT(-46.6765 -23.5855)', 4326), 'RESIDENCIAL');

INSERT INTO traffic_data (idvia, nome, hora, volume, capacidade, nivel, alerta, geom, tipo) VALUES (1, 'Av. Central', '2026-03-28 21:00:00', 105, 1000, 10.5, 'NORMAL', ST_GeomFromText('POINT(-46.6333 -23.5505)', 4326), 'ARTERIAL');
INSERT INTO traffic_data (idvia, nome, hora, volume, capacidade, nivel, alerta, geom, tipo) VALUES (2, 'Rodovia do Aeroporto', '2026-03-28 21:00:00', 803, 2000, 40.15, 'NORMAL', ST_GeomFromText('POINT(-46.4731 -23.4356)', 4326), 'RODOVIA');
INSERT INTO traffic_data (idvia, nome, hora, volume, capacidade, nivel, alerta, geom, tipo) VALUES (3, 'Rua das Flores', '2026-03-28 21:00:00', 53, 300, 17.67, 'NORMAL', ST_GeomFromText('POINT(-46.6765 -23.5855)', 4326), 'RESIDENCIAL');

INSERT INTO traffic_data (idvia, nome, hora, volume, capacidade, nivel, alerta, geom, tipo) VALUES (1, 'Av. Central', '2026-03-28 22:00:00', 231, 1000, 23.1, 'NORMAL', ST_GeomFromText('POINT(-46.6333 -23.5505)', 4326), 'ARTERIAL');
INSERT INTO traffic_data (idvia, nome, hora, volume, capacidade, nivel, alerta, geom, tipo) VALUES (2, 'Rodovia do Aeroporto', '2026-03-28 22:00:00', 758, 2000, 37.9, 'NORMAL', ST_GeomFromText('POINT(-46.4731 -23.4356)', 4326), 'RODOVIA');
INSERT INTO traffic_data (idvia, nome, hora, volume, capacidade, nivel, alerta, geom, tipo) VALUES (3, 'Rua das Flores', '2026-03-28 22:00:00', 89, 300, 29.67, 'NORMAL', ST_GeomFromText('POINT(-46.6765 -23.5855)', 4326), 'RESIDENCIAL');

-- 23:00
INSERT INTO traffic_data (idvia, nome, hora, volume, capacidade, nivel, alerta, geom, tipo) VALUES (1, 'Av. Central', '2026-03-28 23:00:00', 383, 1000, 38.3, 'NORMAL', ST_GeomFromText('POINT(-46.6333 -23.5505)', 4326), 'ARTERIAL');
INSERT INTO traffic_data (idvia, nome, hora, volume, capacidade, nivel, alerta, geom, tipo) VALUES (2, 'Rodovia do Aeroporto', '2026-03-28 23:00:00', 623, 2000, 31.15, 'NORMAL', ST_GeomFromText('POINT(-46.4731 -23.4356)', 4326), 'RODOVIA');
INSERT INTO traffic_data (idvia, nome, hora, volume, capacidade, nivel, alerta, geom, tipo) VALUES (3, 'Rua das Flores', '2026-03-28 23:00:00', 71, 300, 23.67, 'NORMAL', ST_GeomFromText('POINT(-46.6765 -23.5855)', 4326), 'RESIDENCIAL');