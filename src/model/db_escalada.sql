CREATE DATABASE IF NOT EXISTS PillamEscalada;
USE PillamEscalada;

-- 1. Escaladors: Centraliza la información de los usuarios y creadores
CREATE TABLE Escaladors (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nom VARCHAR(100) NOT NULL,
    alias VARCHAR(50),
    edat INT,
    nivell_maxim VARCHAR(5) NOT NULL, -- Rango 4 a 9c+ [cite: 513]
    nom_via_max VARCHAR(100),
    estil_preferit ENUM('esportiva', 'clàssica', 'gel')
);

-- 2. Escoles: Áreas generales de escalada [cite: 297]
CREATE TABLE Escoles (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nom VARCHAR(100) NOT NULL UNIQUE, -- No pueden existir dos con el mismo nombre [cite: 517]
    poblacio VARCHAR(100) NOT NULL,
    aproximacio TEXT,
    num_vies INT DEFAULT 0,
    popularitat ENUM('baixa', 'mitjana', 'alta')
);

-- 3. Sectores: Subzonas dentro de una Escuela [cite: 308, 406]
CREATE TABLE Sectores (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_escola INT NOT NULL,
    nom VARCHAR(100) NOT NULL,
    latitud DECIMAL(10, 8),
    longitud DECIMAL(11, 8),
    aproximacio TEXT,
    num_vies INT DEFAULT 0,
    popularitat ENUM('baixa', 'mitjana', 'alta'),
    restriccions TEXT,
    tipus_sector ENUM('GEL', 'MIXTE_ROCA') NOT NULL, -- Restricción: Gel solo con Gel [cite: 452]
    CONSTRAINT fk_escola_sector FOREIGN KEY (id_escola) REFERENCES Escoles(id) ON DELETE CASCADE,
    UNIQUE(id_escola, nom) -- Nombres no repetidos dentro de la misma escuela [cite: 518]
);

-- 4. Vies (Tabla base): Atributos comunes a los 3 tipos [cite: 296]
CREATE TABLE Vies (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_sector INT NOT NULL,
    id_creador INT NOT NULL,
    nom VARCHAR(100) NOT NULL,
    grau_global VARCHAR(5) NOT NULL, -- Ej: 6a, 9c+, 8b [cite: 298]
    orientacio VARCHAR(2) NOT NULL, -- N, NE, NO... [cite: 305]
    estat ENUM('Apte', 'Construcció', 'Tancada') DEFAULT 'Apte',
    data_finalitzacio_estat DATE DEFAULT NULL, -- Para el cambio automático a Apte [cite: 306]
    tipus_roca ENUM('conglomerat', 'granit', 'calcaria', 'arenisca', 'altres'),
    tipus_via ENUM('Esportiva', 'Clàssica', 'Gel') NOT NULL,
    restriccions TEXT,
    CONSTRAINT fk_sector_via FOREIGN KEY (id_sector) REFERENCES Sectores(id) ON DELETE CASCADE,
    CONSTRAINT fk_creador_via FOREIGN KEY (id_creador) REFERENCES Escaladors(id),
    UNIQUE(id_sector, nom) -- Nom de via no repetit en sector [cite: 518]
);

-- 5. Detalls_Esportiva: Específico para vías de un solo largo [cite: 301]
CREATE TABLE Detalls_Esportiva (
    id_via INT PRIMARY KEY,
    llargada INT CHECK (llargada BETWEEN 5 AND 30),
    ancoratge ENUM('spits', 'parabolts', 'químics'),
    CONSTRAINT fk_esportiva_via FOREIGN KEY (id_via) REFERENCES Vies(id) ON DELETE CASCADE
);

-- 6. Llarggs: Para Clàssica y Gel (múltiples tramos) [cite: 343, 401]
CREATE TABLE Llarggs (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_via INT NOT NULL,
    ordre_llarg INT NOT NULL, -- L1, L2, L3...
    llargada INT CHECK (llargada BETWEEN 15 AND 30),
    grau VARCHAR(5),
    ancoratge VARCHAR(100), -- Dinámico según tipo de vía [cite: 356, 407]
    CONSTRAINT fk_llarg_via FOREIGN KEY (id_via) REFERENCES Vies(id) ON DELETE CASCADE
);

-- 7. Historial: Relación muchos a muchos para el historial del escalador [cite: 516]
CREATE TABLE Historial_Escaladors (
    id_escalador INT,
    id_via INT,
    data_ascensio DATE,
    PRIMARY KEY (id_escalador, id_via),
    FOREIGN KEY (id_escalador) REFERENCES Escaladors(id),
    FOREIGN KEY (id_via) REFERENCES Vies(id)
);