CREATE TABLE public.veiculo
(
	id SERIAL NOT NULL,
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
    id SERIAL NOT NULL,
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