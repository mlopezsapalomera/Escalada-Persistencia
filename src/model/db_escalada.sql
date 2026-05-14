-- =====================================
-- CREACIÓ DE LA BASE DE DADES
-- =====================================
CREATE DATABASE IF NOT EXISTS db_escalada;
USE db_escalada;

-- =====================================
-- TAULA ESCOLES
-- Conté les zones principals d'escalada
-- =====================================
CREATE TABLE escoles (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nom VARCHAR(100) NOT NULL,
    lloc VARCHAR(100),
    aproximacio TEXT,
    num_vies INT DEFAULT 0,
    popularitat ENUM('baixa', 'mitjana', 'alta'),
    UNIQUE(nom) -- No poden existir dues escoles amb el mateix nom
);

-- =====================================
-- TAULA ESCALADORS
-- Usuaris i creadors de vies
-- =====================================
CREATE TABLE escaladors (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nom VARCHAR(100) NOT NULL,
    alias VARCHAR(50),
    edat INT,
    nivell VARCHAR(10),
    nom_via_nivell_maxim VARCHAR(100),
    estil_preferit ENUM('esportiva', 'classica', 'gel'),
    UNIQUE(nom) -- Segons l'enunciat, un escalador es dóna d'alta si no existeix pel nom
);

-- =====================================
-- TAULA SECTORS
-- Subzones dins d'una escola
-- =====================================
CREATE TABLE sectors (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_escola INT NOT NULL,
    nom VARCHAR(100) NOT NULL,
    latitud DECIMAL(10, 8),
    longitud DECIMAL(11, 8),
    aproximacio TEXT,
    num_vies INT DEFAULT 0,
    popularitat ENUM('baixa', 'mitjana', 'alta'),
    restriccions TEXT,
    tipus_sector ENUM('gel', 'mixte_roca') NOT NULL,

    -- relació amb escola
    FOREIGN KEY (id_escola) REFERENCES escoles(id) ON DELETE CASCADE,

    -- nom únic dins la mateixa escola
    UNIQUE (id_escola, nom)
);

-- =====================================
-- TAULA VIES
-- Taula principal de les rutes d'escalada
-- =====================================
CREATE TABLE vies (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_sector INT NOT NULL,
    id_escola INT NOT NULL,
    id_creador INT NOT NULL,

    nom VARCHAR(100) NOT NULL,
    grau_global VARCHAR(5) NOT NULL,

    orientacio ENUM('N','NE','NO','SE','SO','E','O','S') NOT NULL,
    estat ENUM('apte','construccio','tancada') DEFAULT 'apte',

    data_finalitzacio_estat DATE DEFAULT NULL, -- per control automàtic d'estat

    tipus_roca ENUM('conglomerat','granit','calcaria','arenisca','altres'),
    tipus_via ENUM('esportiva','classica','gel') NOT NULL,

    restriccions TEXT,

    -- relacions
    FOREIGN KEY (id_sector) REFERENCES sectors(id) ON DELETE CASCADE,
    FOREIGN KEY (id_escola) REFERENCES escoles(id),
    FOREIGN KEY (id_creador) REFERENCES escaladors(id),

    -- nom únic dins sector
    UNIQUE (id_sector, nom)
);

-- =====================================
-- DETALLS VIES ESPORTIVES
-- Només per vies esportives
-- =====================================
CREATE TABLE detalls_esportiva (
    id_via INT PRIMARY KEY,
    llargada INT CHECK (llargada BETWEEN 5 AND 30),
    ancoratge ENUM('spits','parabolts','químics'),

    FOREIGN KEY (id_via) REFERENCES vies(id) ON DELETE CASCADE
);

-- =====================================
-- TAULA LLARGS
-- Per vies clàssiques i de gel
-- =====================================
CREATE TABLE llargs (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_via INT NOT NULL,
    ordre_llarg INT NOT NULL, -- L1, L2, L3...
    llargada INT CHECK (llargada BETWEEN 15 AND 30),
    grau VARCHAR(5),
    ancoratge VARCHAR(100),

    FOREIGN KEY (id_via) REFERENCES vies(id) ON DELETE CASCADE
);

-- =====================================
-- HISTORIAL ESCALADORS
-- Relació entre escaladors i vies
-- =====================================
CREATE TABLE historial_escaladors (
    id_escalador INT,
    id_via INT,
    data_ascensio DATE,

    PRIMARY KEY (id_escalador, id_via),

    FOREIGN KEY (id_escalador) REFERENCES escaladors(id),
    FOREIGN KEY (id_via) REFERENCES vies(id)
);