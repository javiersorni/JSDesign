DROP DATABASE IF EXISTS AppEvento;

CREATE DATABASE IF NOT EXISTS AppEvento;

USE AppEvento;

-- =========================
-- USUARIOS
-- =========================
CREATE TABLE usuarios (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(255),
    username VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    rol VARCHAR(20) NOT NULL
);

-- =========================
-- EVENTOS
-- =========================
CREATE TABLE eventos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(255) NOT NULL,
    fecha_evento DATE NOT NULL,
    cantidad_mesas INT NOT NULL DEFAULT 0
);

-- =========================
-- MESAS
-- =========================
CREATE TABLE mesas (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    evento_id BIGINT NOT NULL,
    numero_mesa INT NOT NULL,
    capacidad INT NOT NULL DEFAULT 6,
    estado VARCHAR(20) DEFAULT 'LIBRE',

    UNIQUE (evento_id, numero_mesa),

    FOREIGN KEY (evento_id)
        REFERENCES eventos(id)
        ON DELETE CASCADE
);

-- =========================
-- GRUPOS
-- =========================
CREATE TABLE grupos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    evento_id BIGINT NOT NULL,
    nombre_contacto VARCHAR(255) NOT NULL,
    numero_personas INT NOT NULL CHECK (numero_personas BETWEEN 1 AND 10),

    FOREIGN KEY (evento_id)
        REFERENCES eventos(id)
        ON DELETE CASCADE
);

-- =========================
-- ASIGNACIÓN DE MESAS
-- =========================
CREATE TABLE asignacion_mesas (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,

    grupo_id BIGINT NOT NULL,
    mesa_id BIGINT NOT NULL,

    personas_asignadas INT NOT NULL,

    FOREIGN KEY (grupo_id)
        REFERENCES grupos(id)
        ON DELETE CASCADE,

    FOREIGN KEY (mesa_id)
        REFERENCES mesas(id)
        ON DELETE CASCADE
);