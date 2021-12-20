INSERT INTO localizacao (id, latitude, longitude)
VALUES (1, -15.817759, -47.836959);

INSERT INTO localizacao (id, latitude, longitude)
VALUES (2, -15.817759, -47.836959);

INSERT INTO restaurante (id, localizacao_id, nome)
VALUES (1, 1, 'Barriguinha Cheia e Feliz');

INSERT INTO restaurante (id, localizacao_id, nome)
VALUES (2, 2, 'Quitanda do Gerson');

INSERT INTO prato
    (id, nome, descricao, restaurante_id, preco)
VALUES (1, 'Bifinho Gostoso', 'Feito com amor', 1, 12.99);

INSERT INTO prato
    (id, nome, descricao, restaurante_id, preco)
VALUES (2, 'Peixinho frito com açai', 'Deixa a barrigunha muito mais feliz', 1, 19.99);

INSERT INTO prato
    (id, nome, descricao, restaurante_id, preco)
VALUES (3, 'Litro do Açai', 'O açai mais grosso do Guamá', 2, 12.99);
