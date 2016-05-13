-- tables
-- Table: ROLES
CREATE TABLE ROLES (
    rol varchar(20) NOT NULL,
    PRIMARY KEY (rol)
)ENGINE = InnoDB;
-- Table: USUARIOS
CREATE TABLE USUARIOS (
    id int NOT NULL,
    nombre varchar(100) NOT NULL,
    correo varchar(100) NOT NULL,
    contrasena varchar(50) NOT NULL,
    PRIMARY KEY (id)
)ENGINE = InnoDB;
-- Table: ROLES_USUARIOS
CREATE TABLE ROLES_USUARIOS (
    USUARIOS_id int NOT NULL,
    ROLES_rol varchar(20) NOT NULL,
    PRIMARY KEY (USUARIOS_id,ROLES_rol),
    CONSTRAINT ROLES_USUARIOS_ROLES FOREIGN KEY (ROLES_rol) REFERENCES ROLES (rol),
    CONSTRAINT ROLES_USUARIOS_USUARIOS FOREIGN KEY (USUARIOS_id) REFERENCES USUARIOS (id)
)ENGINE = InnoDB;

--Table:MODELOS
CREATE TABLE MODELOS(
    nombre varchar(100) NOT NULL,
    clase varchar(100) NOT NULL,
    vidaUtil int NOT NULL,
    valor int NOT NULL,
    seguro bool NOT NULL,
    foto varchar(500) NULL,
    PRIMARY KEY (nombre)
)ENGINE=InnoDB;

-- Table: EQUIPOS
CREATE TABLE EQUIPOS (
    serial int NOT NULL,
    nombre varchar(100) NOT NULL,
    placa int NULL,
    marca varchar(100) NOT NULL,
    descripcion varchar(200) NOT NULL,
    -- SEVIDOR, en ves de varchar enum('activo','desactivo')
    estado varchar(50) NOT NULL,
    -- SEVIDOR, en ves de varchar enum('prestamo diario','prestamo 24 horas','mantenimiento','en almacen','prestamo indefinido','prestamo por semestre','dado de baja','en reparacion')
    subestados varchar(50) NOT NULL,
    proveedor varchar(100) NULL,
    Modelos_nombre varchar(100) NOT NULL,
    vidaInicial int not null,
    UNIQUE INDEX placa (placa),
    PRIMARY KEY (serial),
    CONSTRAINT EQUIPOS_MODELO FOREIGN KEY (Modelos_nombre) REFERENCES MODELOS (nombre)
)ENGINE=InnoDB;
    
-- Table: EQUIPOS_BASICOS
CREATE TABLE EQUIPOS_BASICOS (
    nombre varchar(100) NOT NULL,
    valor int NULL,
    foto varchar(500) NULL,
    descripcion varchar(200) NULL,
    cantidad int NOT NULL,
    PRIMARY KEY (nombre)
)ENGINE = InnoDB;

-- Table: PRESTAMOS
CREATE TABLE PRESTAMOS (
    USUARIOS_id int NOT NULL,
    EQUIPOS_serial int NOT NULL,
    fechaExpedicion datetime NOT NULL,
    fechaVencimiento datetime NULL,
    -- SEVIDOR, en ves de varchar enum('prestamo diario','prestamo 24 horas','mantenimiento','en almacen','prestamo indefinido','prestamo por semestre','dado de baja','en reparacion')
    tipoPrestamo varchar(50) NOT NULL,
    PRIMARY KEY (USUARIOS_id,EQUIPOS_serial),
    CONSTRAINT PRESTAMOS_USUARIOS FOREIGN KEY (USUARIOS_id) REFERENCES USUARIOS(id),
    CONSTRAINT PRESTAMOS_EQUIPOS FOREIGN KEY (EQUIPOS_serial) REFERENCES EQUIPOS(serial)
)ENGINE = InnoDB;

-- Table: PRESTAMOS_BASICOS
CREATE TABLE PRESTAMOS_BASICOS (
    EQUIPOS_BASICOS_nombre varchar(100) NOT NULL,
    USUARIOS_id int NOT NULL,
    fechaExpedicion datetime NOT NULL,
    fechaVencimiento datetime NULL,
    tipoPrestamo varchar(50) NOT NULL,
    cantidadPrestada int NOT NULL,
    PRIMARY KEY (EQUIPOS_BASICOS_nombre,USUARIOS_id),
    CONSTRAINT PRESTAMOS_BASICOS_USUARIOS FOREIGN KEY (USUARIOS_id) REFERENCES USUARIOS (id),
    CONSTRAINT PRESTAMOS_BASICOS_EQUIPOS_BASICOS FOREIGN KEY (EQUIPOS_BASICOS_nombre) REFERENCES EQUIPOS_BASICOS(nombre)
)ENGINE = InnoDB;