CREATE TABLE public.veiculo
(
	id SERIAL NOT NULL,
    placa VARCHAR(10),
    descricao VARCHAR(100),
    anofabricacao smallint
)

TABLESPACE pg_default;

ALTER TABLE public.veiculo
    OWNER to postgres;