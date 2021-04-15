CREATE TABLE public.veiculo
(
	id serial NOT NULL,
	status smallint NOT NULL DEFAULT 1,
    placa VARCHAR(10),
    descricao VARCHAR(100),
    anofabricacao VARCHAR(5),
    PRIMARY KEY (id)
)

TABLESPACE pg_default;

ALTER TABLE public.veiculo
    OWNER to postgres;

CREATE TABLE public.categoriafinanceira
(
    id serial NOT NULL,
    status smallint NOT NULL DEFAULT 1,
    descricao VARCHAR(50),
    tipo smallint NOT NULL,
    PRIMARY KEY (id)
)

TABLESPACE pg_default;

ALTER TABLE public.categoriafinanceira
    OWNER to postgres;

COMMENT ON COLUMN public.categoriafinanceira.tipo
    IS '1 - Receita
        2 - Despesa';

CREATE TABLE public.lancfinanceiro
(
    id serial NOT NULL,
    status smallint NOT NULL DEFAULT 1,
    data date NOT NULL,
    idveiculo integer NOT NULL,
    idcategoria integer NOT NULL,
    valor double precision NOT NULL,
    descricao VARCHAR(100),
    PRIMARY KEY (id),
    CONSTRAINT fk_veiculo FOREIGN KEY (idveiculo)
        REFERENCES public.veiculo (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
        NOT VALID,
    CONSTRAINT fk_categoria FOREIGN KEY (idcategoria)
        REFERENCES public.categoriafinanceira (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
        NOT VALID
);

ALTER TABLE public.lancfinanceiro
    OWNER to postgres;

CREATE TABLE public.auditoria
(
    id serial NOT NULL,
    status smallint NOT NULL DEFAULT 1,
    tabela VARCHAR(50) NOT NULL,
    idRegistro integer NOT NULL,
    data timestamp without time zone NOT NULL,
    PRIMARY KEY (id)
);

ALTER TABLE public.auditoria
    OWNER to postgres;