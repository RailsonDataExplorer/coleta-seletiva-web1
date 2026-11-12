-- ============================================================
-- LIMPEZA COMPLETA DO BANCO (ordem: filhas primeiro, pais depois)
-- ============================================================
DROP TABLE IF EXISTS historico CASCADE;
DROP TABLE IF EXISTS item_solicitacao CASCADE;
DROP TABLE IF EXISTS solicitacoes CASCADE;
DROP TABLE IF EXISTS operador_ponto CASCADE;
DROP TABLE IF EXISTS material_aceito CASCADE;
DROP TABLE IF EXISTS enderecos CASCADE;
DROP TABLE IF EXISTS operadores CASCADE;
DROP TABLE IF EXISTS pontos_coleta CASCADE;
DROP TABLE IF EXISTS materiais CASCADE;
DROP TABLE IF EXISTS moradores CASCADE;

-- ============================================================
-- CRIAÇÃO DAS TABELAS
-- ============================================================

CREATE TABLE moradores (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    telefone VARCHAR(15),
    ativo BOOLEAN NOT NULL DEFAULT TRUE,
    data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE enderecos (
    id BIGSERIAL PRIMARY KEY,
    morador_id BIGINT NOT NULL REFERENCES moradores(id),
    logradouro VARCHAR(255) NOT NULL,
    numero VARCHAR(20) NOT NULL,
    bairro VARCHAR(100) NOT NULL,
    municipio VARCHAR(100) NOT NULL,
    uf VARCHAR(2) NOT NULL,
    cep VARCHAR(9) NOT NULL,
    complemento VARCHAR(255)
);

CREATE TABLE materiais (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL UNIQUE,
    descricao TEXT,
    unidade VARCHAR(20) NOT NULL,
    orientacao TEXT,
    ativo BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE pontos_coleta (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    endereco TEXT NOT NULL,
    contato VARCHAR(100),
    horarios VARCHAR(255),
    ativo BOOLEAN NOT NULL DEFAULT TRUE,
    recolhimento_domiciliar BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE TABLE material_aceito (
    id BIGSERIAL PRIMARY KEY,
    ponto_id BIGINT NOT NULL REFERENCES pontos_coleta(id),
    material_id BIGINT NOT NULL REFERENCES materiais(id),
    observacoes TEXT,
    limite_por_solicitacao DOUBLE PRECISION,
    UNIQUE(ponto_id, material_id)
);

CREATE TABLE operadores (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE operador_ponto (
    operador_id BIGINT NOT NULL REFERENCES operadores(id),
    ponto_id BIGINT NOT NULL REFERENCES pontos_coleta(id),
    PRIMARY KEY (operador_id, ponto_id)
);

CREATE TABLE solicitacoes (
    id BIGSERIAL PRIMARY KEY,
    protocolo VARCHAR(50) NOT NULL UNIQUE,
    morador_id BIGINT NOT NULL REFERENCES moradores(id),
    ponto_id BIGINT NOT NULL REFERENCES pontos_coleta(id),
    endereco_copia TEXT NOT NULL,
    situacao VARCHAR(30) NOT NULL DEFAULT 'SOLICITADA',
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    observacoes TEXT,
    versao INT DEFAULT 0
);

CREATE TABLE item_solicitacao (
    id BIGSERIAL PRIMARY KEY,
    solicitacao_id BIGINT NOT NULL REFERENCES solicitacoes(id),
    material_id BIGINT NOT NULL REFERENCES materiais(id),
    unidade_copia VARCHAR(20) NOT NULL,
    quantidade_estimada DOUBLE PRECISION NOT NULL,
    quantidade_efetiva DOUBLE PRECISION,
    UNIQUE(solicitacao_id, material_id)
);

CREATE TABLE historico (
    id BIGSERIAL PRIMARY KEY,
    solicitacao_id BIGINT NOT NULL REFERENCES solicitacoes(id),
    situacao_anterior VARCHAR(30),
    nova_situacao VARCHAR(30) NOT NULL,
    data_alteracao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    responsavel VARCHAR(100),
    motivo TEXT
);