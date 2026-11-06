-- Tabela de materiais recicláveis
CREATE TABLE IF NOT EXISTS materiais (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(255) NOT NULL UNIQUE,
    tipo VARCHAR(255) NOT NULL
);

-- Tabela de moradores
CREATE TABLE IF NOT EXISTS moradores (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    endereco VARCHAR(255) NOT NULL,
    telefone VARCHAR(15),
    data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabela de pontos de coleta
CREATE TABLE IF NOT EXISTS pontos_coleta (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    endereco VARCHAR(255) NOT NULL,
    capacidade_maxima DOUBLE PRECISION
);

-- Tabela de solicitações de coleta
CREATE TABLE IF NOT EXISTS solicitacoes (
    id BIGSERIAL PRIMARY KEY,
    morador_id BIGINT NOT NULL REFERENCES moradores(id),
    material_id BIGINT NOT NULL REFERENCES materiais(id),
    ponto_coleta_id BIGINT NOT NULL REFERENCES pontos_coleta(id),
    quantidade_kg DOUBLE PRECISION NOT NULL,
    data_solicitacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    status VARCHAR(20)
);