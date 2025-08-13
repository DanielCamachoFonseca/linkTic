CREATE TABLE producto (
    id BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(255) NOT NULL,
    precio NUMERIC(10, 2) NOT NULL
);

CREATE TABLE inventario (
    id BIGSERIAL PRIMARY KEY,
    producto_id BIGINT, 
    cantidad INTEGER NOT NULL    
);
