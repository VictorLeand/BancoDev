-- Se ejecuta automaticamente al crear el contenedor de PostgreSQL

CREATE DATABASE clientes;
CREATE DATABASE cuentas;

-- Microservicio ms-clientes
\connect clientes

CREATE EXTENSION IF NOT EXISTS pgcrypto;

CREATE TABLE persona (
    id             BIGSERIAL    PRIMARY KEY,
    nombre         VARCHAR(100) NOT NULL,
    genero         VARCHAR(20)  NOT NULL,
    edad           INTEGER      NOT NULL,
    identificacion VARCHAR(20)  NOT NULL,
    direccion      VARCHAR(200) NOT NULL,
    telefono       VARCHAR(20)  NOT NULL,
    CONSTRAINT uk_persona_identificacion UNIQUE (identificacion),
    CONSTRAINT ck_persona_genero CHECK (genero IN ('MASCULINO', 'FEMENINO', 'OTRO')),
    CONSTRAINT ck_persona_edad CHECK (edad BETWEEN 0 AND 150)
);

-- Cliente hereda de Persona (estrategia JOINED): comparte la clave primaria.
CREATE TABLE cliente (
    persona_id BIGINT       PRIMARY KEY,
    cliente_id VARCHAR(36)  NOT NULL,
    contrasena VARCHAR(100) NOT NULL,
    estado     BOOLEAN      NOT NULL DEFAULT TRUE,
    CONSTRAINT uk_cliente_cliente_id UNIQUE (cliente_id),
    CONSTRAINT fk_cliente_persona FOREIGN KEY (persona_id) REFERENCES persona (id) ON DELETE CASCADE
);

-- Datos iniciales (caso de uso 1). Contraseñas cifradas con BCrypt.
WITH p AS (
    INSERT INTO persona (nombre, genero, edad, identificacion, direccion, telefono)
    VALUES ('José Lema', 'MASCULINO', 35, '1712345678', 'Otavalo sn y principal', '098254785')
    RETURNING id
)
INSERT INTO cliente (persona_id, cliente_id, contrasena, estado)
SELECT id, '8F3A2C1D9E4B4F6A8C7D1E2F3A4B5C6D', crypt('1234', gen_salt('bf', 10)), TRUE FROM p;

WITH p AS (
    INSERT INTO persona (nombre, genero, edad, identificacion, direccion, telefono)
    VALUES ('Marianela Montalvo', 'FEMENINO', 30, '1723456789', 'Amazonas y NNUU', '097548965')
    RETURNING id
)
INSERT INTO cliente (persona_id, cliente_id, contrasena, estado)
SELECT id, '1B2C3D4E5F6A47B8C9D0E1F2A3B4C5D6', crypt('5678', gen_salt('bf', 10)), TRUE FROM p;

WITH p AS (
    INSERT INTO persona (nombre, genero, edad, identificacion, direccion, telefono)
    VALUES ('Juan Osorio', 'MASCULINO', 40, '1734567890', '13 de junio y equinoccial', '098874587')
    RETURNING id
)
INSERT INTO cliente (persona_id, cliente_id, contrasena, estado)
SELECT id, '9A8B7C6D5E4F43A2B1C0D9E8F7A6B5C4', crypt('1245', gen_salt('bf', 10)), TRUE FROM p;

-- Microservicio ms-cuentas (Cuenta / Movimiento)
\connect cuentas

-- Replica local de clientes alimentada por eventos asincronos (RabbitMQ).
CREATE TABLE cliente_snapshot (
    cliente_id          VARCHAR(36)  PRIMARY KEY,
    nombre              VARCHAR(100) NOT NULL,
    estado              BOOLEAN      NOT NULL,
    fecha_actualizacion TIMESTAMP    NOT NULL
);

CREATE TABLE cuenta (
    id               BIGSERIAL     PRIMARY KEY,
    numero_cuenta    VARCHAR(20)   NOT NULL,
    tipo_cuenta      VARCHAR(20)   NOT NULL,
    saldo_inicial    NUMERIC(15,2) NOT NULL,
    saldo_disponible NUMERIC(15,2) NOT NULL,
    estado           BOOLEAN       NOT NULL DEFAULT TRUE,
    cliente_id       VARCHAR(36)   NOT NULL,
    CONSTRAINT uk_cuenta_numero_cuenta UNIQUE (numero_cuenta),
    CONSTRAINT ck_cuenta_tipo CHECK (tipo_cuenta IN ('AHORROS', 'CORRIENTE')),
    CONSTRAINT ck_cuenta_saldo_inicial CHECK (saldo_inicial >= 0),
    CONSTRAINT ck_cuenta_saldo_disponible CHECK (saldo_disponible >= 0)
);

CREATE INDEX idx_cuenta_cliente_id ON cuenta (cliente_id);

CREATE TABLE movimiento (
    id              BIGSERIAL     PRIMARY KEY,
    fecha           TIMESTAMP     NOT NULL,
    tipo_movimiento VARCHAR(20)   NOT NULL,
    valor           NUMERIC(15,2) NOT NULL,
    saldo           NUMERIC(15,2) NOT NULL,
    cuenta_id       BIGINT        NOT NULL,
    CONSTRAINT fk_movimiento_cuenta FOREIGN KEY (cuenta_id) REFERENCES cuenta (id),
    CONSTRAINT ck_movimiento_tipo CHECK (tipo_movimiento IN ('DEPOSITO', 'RETIRO')),
    CONSTRAINT ck_movimiento_valor CHECK (valor <> 0),
    CONSTRAINT ck_movimiento_saldo CHECK (saldo >= 0)
);

-- Soporta el reporte por rango de fechas y la busqueda del ultimo movimiento.
CREATE INDEX idx_movimiento_cuenta_fecha ON movimiento (cuenta_id, fecha);

-- Datos iniciales: replica de clientes (caso 1) y cuentas (caso 2).
INSERT INTO cliente_snapshot (cliente_id, nombre, estado, fecha_actualizacion) VALUES
    ('8F3A2C1D9E4B4F6A8C7D1E2F3A4B5C6D', 'José Lema',          TRUE, NOW()),
    ('1B2C3D4E5F6A47B8C9D0E1F2A3B4C5D6', 'Marianela Montalvo', TRUE, NOW()),
    ('9A8B7C6D5E4F43A2B1C0D9E8F7A6B5C4', 'Juan Osorio',        TRUE, NOW());

INSERT INTO cuenta (numero_cuenta, tipo_cuenta, saldo_inicial, saldo_disponible, estado, cliente_id) VALUES
    ('478758', 'AHORROS',   2000.00, 2000.00, TRUE, '8F3A2C1D9E4B4F6A8C7D1E2F3A4B5C6D'),
    ('225487', 'CORRIENTE',  100.00,  100.00, TRUE, '1B2C3D4E5F6A47B8C9D0E1F2A3B4C5D6'),
    ('495878', 'AHORROS',      0.00,    0.00, TRUE, '9A8B7C6D5E4F43A2B1C0D9E8F7A6B5C4'),
    ('496825', 'AHORROS',    540.00,  540.00, TRUE, '1B2C3D4E5F6A47B8C9D0E1F2A3B4C5D6');