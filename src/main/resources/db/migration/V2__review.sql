CREATE SCHEMA IF NOT EXISTS public;

INSERT INTO image (id, name) VALUES ('a8d2c502-3d78-4a4c-b07a-d5f36c914545', '22222222_babc9f03-931e-4b46-97bd-bf8a6a788619.png');
INSERT INTO image (id, name) VALUES ('9f0fd551-ebb2-4c7c-a309-e2f43e8c9512', 'anonymous_304e03f4-ce8c-4f20-9214-3e077f76bb91.png');

DELETE FROM shopuser;

INSERT INTO shopuser (id, phone, password, email, first_name, last_name, role) VALUES ('6b718067-e1e4-4202-a7e2-7339ea0d6cb4', 'anonymous', '$2a$10$5rAOMKmVsh9.NlzXTLLbq.XwouGdg3dwohvb5/HDn692YfdrLthO2', 'anonymous@supershop.com', 'anonymous', 'anonymous', 'ROLE_CUSTOMER');
INSERT INTO shopuser (id, phone, password, email, first_name, last_name, role) VALUES ('fbe5a8e7-8555-4ee8-bff2-c572447e5f25', '11111111', '$2a$10$5rAOMKmVsh9.NlzXTLLbq.XwouGdg3dwohvb5/HDn692YfdrLthO2', 'admin@supershop.com', 'Admin', 'Admin','ROLE_ADMIN');
INSERT INTO shopuser (id, phone, password, email, first_name, last_name, role) VALUES ('04c8bd30-ba4e-4e82-b996-db907e37a2c6', '22222222', '$2a$10$5rAOMKmVsh9.NlzXTLLbq.XwouGdg3dwohvb5/HDn692YfdrLthO2', 'user@supershop.com', 'User', 'User', 'ROLE_CUSTOMER');

alter table review
	add approved boolean not null;

alter table review
	add image uuid;

alter table review
	add constraint fk_review_image
		foreign key (image) references image (id)
			on update cascade on delete cascade;

INSERT INTO review (id, commentary, shopuser, product, approved, image) VALUES ('8130f217-8a8b-4765-b358-4eb9b91a5939', 'Пополняет запас витаминов для организма',
 'fbe5a8e7-8555-4ee8-bff2-c572447e5f25', 'babc9f03-931e-4b46-97bd-bf8a6a788619', true, null);
INSERT INTO review (id, commentary, shopuser, product, approved, image) VALUES ('882a552c-80a0-4880-bc78-7c06d0468e56', 'Вкусный и сочный фрукт',
 '6b718067-e1e4-4202-a7e2-7339ea0d6cb4', 'babc9f03-931e-4b46-97bd-bf8a6a788619', true, null);
INSERT INTO review (id, commentary, shopuser, product, approved, image) VALUES ('0911f6fb-243a-444f-86df-959406163cd3', 'Кладезь железа для организма',
 '6b718067-e1e4-4202-a7e2-7339ea0d6cb4', 'babc9f03-931e-4b46-97bd-bf8a6a788619', true, null);
INSERT INTO review (id, commentary, shopuser, product, approved, image) VALUES ('7bb31a05-9ca1-492b-9637-8de656f4c3d3', 'Наша семья очень любит яблоки',
 '04c8bd30-ba4e-4e82-b996-db907e37a2c6', 'babc9f03-931e-4b46-97bd-bf8a6a788619', true, null);
INSERT INTO review (id, commentary, shopuser, product, approved, image) VALUES ('8cd1e1d8-73ea-4d7f-bc47-f695f00c2dab', 'Мы покупаем яблоки каждую неделю',
 '04c8bd30-ba4e-4e82-b996-db907e37a2c6', 'babc9f03-931e-4b46-97bd-bf8a6a788619', false, 'a8d2c502-3d78-4a4c-b07a-d5f36c914545');
INSERT INTO review (id, commentary, shopuser, product, approved, image) VALUES ('280fc264-ad6a-4878-a0c9-1520e05e82ff', 'Очень любим черняшечку с салом',
 '6b718067-e1e4-4202-a7e2-7339ea0d6cb4', '304e03f4-ce8c-4f20-9214-3e077f76bb91', false, '9f0fd551-ebb2-4c7c-a309-e2f43e8c9512');