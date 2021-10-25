CREATE TABLE localizacao
(
    id        int8 NOT NULL GENERATED BY DEFAULT AS IDENTITY,
    latitude  float8 NULL,
    longitude float8 NULL,
    CONSTRAINT localizacao_pkey PRIMARY KEY (id)
);


CREATE TABLE restaurante
(
    id             int8 NOT NULL GENERATED BY DEFAULT AS IDENTITY,
    nome           varchar(255) NULL,
    localizacao_id int8 NULL,
    CONSTRAINT restaurante_pkey PRIMARY KEY (id)
);


ALTER TABLE public.restaurante
    ADD CONSTRAINT fkdfbggt9ievc4ev74wl3tdnscl FOREIGN KEY (localizacao_id) REFERENCES localizacao (id);


CREATE TABLE prato
(
    id             int8 NOT NULL GENERATED BY DEFAULT AS IDENTITY,
    descricao      varchar(255) NULL,
    nome           varchar(255) NULL,
    preco          numeric(19, 2) NULL,
    restaurante_id int8 NULL,
    CONSTRAINT prato_pkey PRIMARY KEY (id)
);

CREATE TABLE carrinho
(
    id      int8 NOT NULL GENERATED BY DEFAULT AS IDENTITY,
    cliente varchar(255) NULL,
    CONSTRAINT carrinho_pkey PRIMARY KEY (id)
);

ALTER TABLE public.prato
    ADD CONSTRAINT fk43yo4ddilkvn6ebgfcx2vnqs7 FOREIGN KEY (restaurante_id) REFERENCES restaurante (id);

INSERT INTO localizacao (id, latitude, longitude)
VALUES (1000, -15.817759, -47.836959);

INSERT INTO restaurante (id, localizacao_id, nome)
VALUES (999, 1000, 'Manguai');

INSERT INTO prato
    (id, nome, descricao, restaurante_id, preco)
VALUES (9998, 'Cuscuz com Ovo', 'Bom demais no café da manhã', 999, 3.99);

INSERT INTO prato
    (id, nome, descricao, restaurante_id, preco)
VALUES (9997, 'Peixe frito', 'Agulhinha frita, excelente com Cerveja', 999, 99.99);