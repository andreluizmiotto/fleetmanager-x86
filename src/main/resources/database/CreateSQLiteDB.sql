CREATE TABLE IF NOT EXISTS veiculo
(
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    status INTEGER DEFAULT 1,
    placa TEXT NOT NULL,
    descricao TEXT NOT NULL,
    anofabricacao TEXT
);

CREATE TABLE IF NOT EXISTS categoriafinanceira
(
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    status INTEGER DEFAULT 1,
    descricao TEXT NOT NULL,
    tipo INTEGER NOT NULL
);

CREATE TABLE IF NOT EXISTS lancfinanceiro
(
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    status INTEGER DEFAULT 1,
    data date DEFAULT current_timestamp,
    idveiculo integer,
    idcategoria integer,
    valor double precision NOT NULL,
    descricao VARCHAR(100),
    FOREIGN KEY (idveiculo)
        REFERENCES veiculo (id)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION,
    FOREIGN KEY (idcategoria)
        REFERENCES categoriafinanceira (id)
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);

CREATE TABLE IF NOT EXISTS auditoria
(
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    status INTEGER DEFAULT 1,
    tabela TEXT NOT NULL,
    idregistro INTEGER NOT NULL,
    data datetime DEFAULT current_timestamp
);