CREATE TABLE traffic_data (
    id SERIAL PRIMARY KEY,
    idvia INTEGER,
    nome VARCHAR(255),
    tipo VARCHAR(50) NOT NULL,
    hora TIMESTAMP,
    clima VARCHAR(50),
    volume INTEGER,
    capacidade INTEGER,
    nivel DOUBLE PRECISION,
    status VARCHAR(50),
    alerta VARCHAR(50),
    geom GEOMETRY(Point, 4326),
    CONSTRAINT uk_idvia_hora UNIQUE (idvia, hora) -- Importante para bater com a Entity
);
CREATE TABLE role (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(50) NOT NULL UNIQUE
);