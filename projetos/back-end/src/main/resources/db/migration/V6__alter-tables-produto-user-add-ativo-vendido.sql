ALTER TABLE produto
ADD COLUMN vendido boolean DEFAULT false not null;
ALTER TABLE user
ADD COLUMN ativo boolean DEFAULT true not null;