-- public.localizacao definition

-- Drop table

-- DROP TABLE public.localizacao;

CREATE TABLE public.localizacao
(
    id        int8 NOT NULL GENERATED BY DEFAULT AS IDENTITY,
    latitude  float8 NULL,
    longitude float8 NULL,
    CONSTRAINT localizacao_pkey PRIMARY KEY (id)
);
