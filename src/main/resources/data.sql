-- ============================================================
-- DADOS DE DEMONSTRAÇÃO - SISTEMA DE COLETA SELETIVA
-- ============================================================
-- Estes dados cobrem todos os cenários de aceite do PDF (Seção 13)
-- ============================================================

-- ============================================================
-- 1. MATERIAIS RECICLÁVEIS (RF03)
-- ============================================================
INSERT INTO materiais (nome, descricao, unidade, orientacao, ativo) VALUES
('Papel', 'Papelão, folhas de escritório, jornais e revistas', 'KG', 'Limpo, seco e sem contaminantes', true),
('Plástico', 'Garrafas PET, embalagens plásticas rígidas', 'KG', 'Lavado e sem tampas', true),
('Vidro', 'Garrafas, potes e frascos de vidro', 'KG', 'Sem cacos quebrados', true),
('Metal', 'Latas de alumínio e aço', 'KG', 'Amassadas para ocupar menos espaço', true),
('Orgânico', 'Restos de comida e resíduos orgânicos', 'KG', 'Em sacos biodegradáveis', false);  -- Material INATIVO para teste

-- ============================================================
-- 2. PONTOS DE COLETA (RF04)
-- ============================================================
INSERT INTO pontos_coleta (nome, endereco, contato, horarios, ativo, recolhimento_domiciliar) VALUES
('Ecoponto Centro', 'Rua A, 123 - Centro, Aracaju/SE', '79999999999', 'Seg-Sex 08:00-17:00', true, true),
('Ecoponto Norte', 'Av. Beira Mar, 456 - Atalaia, Aracaju/SE', '79888888888', 'Seg-Sáb 07:00-19:00', true, true),
('Ponto Inativo', 'Rua X, 789 - Bairro Y', '79777777777', 'Seg-Sex 08:00-12:00', false, false);  -- Ponto INATIVO para teste

-- ============================================================
-- 3. VÍNCULOS MATERIAL-PONTO (RF05)
-- ============================================================
-- Ecoponto Centro aceita: Papel, Plástico, Vidro, Metal
INSERT INTO material_aceito (ponto_id, material_id, observacoes, limite_por_solicitacao) VALUES
(1, 1, 'Apenas papel limpo e seco', 50.0),
(1, 2, 'Garrafas PET e embalagens rígidas', 30.0),
(1, 3, 'Sem cacos quebrados', 20.0),
(1, 4, 'Latas amassadas', 40.0);

-- Ecoponto Norte aceita: Papel, Plástico (NÃO aceita Vidro - para teste de incompatibilidade)
INSERT INTO material_aceito (ponto_id, material_id, observacoes, limite_por_solicitacao) VALUES
(2, 1, 'Papel de escritório', 100.0),
(2, 2, 'Plástico reciclável', 50.0);

-- ============================================================
-- 4. MORADORES COM ENDEREÇOS (RF01, RF02)
-- ============================================================
INSERT INTO moradores (nome, email, telefone, ativo, data_cadastro) VALUES
('João Silva', 'joao.silva@email.com', '79988887777', true, CURRENT_TIMESTAMP),
('Maria Santos', 'maria.santos@email.com', '79977776666', true, CURRENT_TIMESTAMP),
('Pedro Oliveira', 'pedro.oliveira@email.com', '79966665555', true, CURRENT_TIMESTAMP);

-- Endereços dos moradores
INSERT INTO enderecos (morador_id, logradouro, numero, bairro, municipio, uf, cep, complemento) VALUES
(1, 'Rua das Flores', '456', 'Jardim', 'Aracaju', 'SE', '49000000', 'Casa'),
(1, 'Av. Principal', '100', 'Centro', 'Aracaju', 'SE', '49000001', 'Apto 301'),  -- Morador com 2 endereços
(2, 'Rua do Comércio', '789', 'Atalaia', 'Aracaju', 'SE', '49000002', null),
(3, 'Travessa da Paz', '234', 'São José', 'Aracaju', 'SE', '49000003', 'Fundos');

-- ============================================================
-- 5. OPERADORES (RF07)
-- ============================================================
INSERT INTO operadores (nome, email) VALUES
('Carlos Operador', 'carlos.operador@coleta.com'),
('Ana Responsável', 'ana.responsavel@coleta.com');

-- Vínculos operador-ponto
INSERT INTO operador_ponto (operador_id, ponto_id) VALUES
(1, 1),  -- Carlos opera o Ecoponto Centro
(2, 2);  -- Ana opera o Ecoponto Norte

-- ============================================================
-- 6. SOLICITAÇÕES EM DIFERENTES SITUAÇÕES (RF08-RF14)
-- ============================================================

-- Solicitação 1: CONCLUÍDA (para testar RF15 - resumo gerencial)
INSERT INTO solicitacoes (protocolo, morador_id, ponto_id, endereco_copia, situacao, data_criacao, observacoes, versao) VALUES
('COL-DEMO0001', 1, 1, 'Rua das Flores, 456 - Jardim, Aracaju/SE - CEP: 49000000', 'CONCLUÍDA', CURRENT_TIMESTAMP - INTERVAL '5 days', 'Caixas desmontadas', 3);

INSERT INTO item_solicitacao (solicitacao_id, material_id, unidade_copia, quantidade_estimada, quantidade_efetiva) VALUES
(1, 1, 'KG', 10.5, 9.8),   -- Papel: estimado 10.5kg, efetivo 9.8kg
(1, 2, 'KG', 5.0, 4.5);    -- Plástico: estimado 5kg, efetivo 4.5kg

-- Histórico da Solicitação 1 (fluxo completo)
INSERT INTO historico (solicitacao_id, situacao_anterior, nova_situacao, data_alteracao, responsavel, motivo) VALUES
(1, null, 'SOLICITADA', CURRENT_TIMESTAMP - INTERVAL '5 days', null, 'Solicitação criada'),
(1, 'SOLICITADA', 'AGENDADA', CURRENT_TIMESTAMP - INTERVAL '4 days', 'Carlos Operador', 'Agendamento realizado para 2026-11-10T10:00:00'),
(1, 'AGENDADA', 'EM_RECOLHIMENTO', CURRENT_TIMESTAMP - INTERVAL '3 days', 'Carlos Operador', 'Recolhimento iniciado'),
(1, 'EM_RECOLHIMENTO', 'CONCLUÍDA', CURRENT_TIMESTAMP - INTERVAL '3 days', 'Carlos Operador', 'Recolhimento concluído');

-- Solicitação 2: AGENDADA (para testar RF10, RF12 - cancelamento)
INSERT INTO solicitacoes (protocolo, morador_id, ponto_id, endereco_copia, situacao, data_criacao, observacoes, versao) VALUES
('COL-DEMO0002', 2, 2, 'Rua do Comércio, 789 - Atalaia, Aracaju/SE - CEP: 49000002', 'AGENDADA', CURRENT_TIMESTAMP - INTERVAL '2 days', 'Material separado', 1);

INSERT INTO item_solicitacao (solicitacao_id, material_id, unidade_copia, quantidade_estimada, quantidade_efetiva) VALUES
(2, 1, 'KG', 15.0, null);  -- Papel: estimado 15kg, ainda não recolhido

INSERT INTO historico (solicitacao_id, situacao_anterior, nova_situacao, data_alteracao, responsavel, motivo) VALUES
(2, null, 'SOLICITADA', CURRENT_TIMESTAMP - INTERVAL '2 days', null, 'Solicitação criada'),
(2, 'SOLICITADA', 'AGENDADA', CURRENT_TIMESTAMP - INTERVAL '1 day', 'Ana Responsável', 'Agendamento realizado para 2026-11-15T14:00:00');

-- Solicitação 3: SOLICITADA (para testar RF08 - criação nova)
INSERT INTO solicitacoes (protocolo, morador_id, ponto_id, endereco_copia, situacao, data_criacao, observacoes, versao) VALUES
('COL-DEMO0003', 3, 1, 'Travessa da Paz, 234 - São José, Aracaju/SE - CEP: 49000003', 'SOLICITADA', CURRENT_TIMESTAMP - INTERVAL '1 day', 'Vidros e metais', 0);

INSERT INTO item_solicitacao (solicitacao_id, material_id, unidade_copia, quantidade_estimada, quantidade_efetiva) VALUES
(3, 3, 'KG', 8.0, null),   -- Vidro: estimado 8kg
(3, 4, 'KG', 12.0, null);  -- Metal: estimado 12kg

INSERT INTO historico (solicitacao_id, situacao_anterior, nova_situacao, data_alteracao, responsavel, motivo) VALUES
(3, null, 'SOLICITADA', CURRENT_TIMESTAMP - INTERVAL '1 day', null, 'Solicitação criada');

-- Solicitação 4: CANCELADA (para testar RF12 - cancelamento)
INSERT INTO solicitacoes (protocolo, morador_id, ponto_id, endereco_copia, situacao, data_criacao, observacoes, versao) VALUES
('COL-DEMO0004', 1, 2, 'Rua das Flores, 456 - Jardim, Aracaju/SE - CEP: 49000000', 'CANCELADA', CURRENT_TIMESTAMP - INTERVAL '10 days', 'Desistência do morador', 2);

INSERT INTO item_solicitacao (solicitacao_id, material_id, unidade_copia, quantidade_estimada, quantidade_efetiva) VALUES
(4, 1, 'KG', 20.0, null);  -- Papel: estimado 20kg

INSERT INTO historico (solicitacao_id, situacao_anterior, nova_situacao, data_alteracao, responsavel, motivo) VALUES
(4, null, 'SOLICITADA', CURRENT_TIMESTAMP - INTERVAL '10 days', null, 'Solicitação criada'),
(4, 'SOLICITADA', 'AGENDADA', CURRENT_TIMESTAMP - INTERVAL '9 days', 'Ana Responsável', 'Agendamento realizado'),
(4, 'AGENDADA', 'CANCELADA', CURRENT_TIMESTAMP - INTERVAL '8 days', 'João Silva', 'Desistência do morador');

-- Solicitação 5: EM_RECOLHIMENTO (para testar RF14 - reagendamento após falha)
INSERT INTO solicitacoes (protocolo, morador_id, ponto_id, endereco_copia, situacao, data_criacao, observacoes, versao) VALUES
('COL-DEMO0005', 2, 1, 'Rua do Comércio, 789 - Atalaia, Aracaju/SE - CEP: 49000002', 'EM_RECOLHIMENTO', CURRENT_TIMESTAMP - INTERVAL '6 hours', 'Tentativa de recolhimento', 2);

INSERT INTO item_solicitacao (solicitacao_id, material_id, unidade_copia, quantidade_estimada, quantidade_efetiva) VALUES
(5, 2, 'KG', 25.0, null);  -- Plástico: estimado 25kg

INSERT INTO historico (solicitacao_id, situacao_anterior, nova_situacao, data_alteracao, responsavel, motivo) VALUES
(5, null, 'SOLICITADA', CURRENT_TIMESTAMP - INTERVAL '1 day', null, 'Solicitação criada'),
(5, 'SOLICITADA', 'AGENDADA', CURRENT_TIMESTAMP - INTERVAL '12 hours', 'Carlos Operador', 'Agendamento realizado'),
(5, 'AGENDADA', 'EM_RECOLHIMENTO', CURRENT_TIMESTAMP - INTERVAL '6 hours', 'Carlos Operador', 'Recolhimento iniciado');

-- ============================================================
-- RESUMO DOS DADOS DE DEMONSTRAÇÃO
-- ============================================================
-- Materiais: 5 (4 ativos, 1 inativo)
-- Pontos: 3 (2 ativos, 1 inativo)
-- Moradores: 3 (com endereços)
-- Operadores: 2 (vinculados a pontos)
-- Solicitações: 5 (em diferentes situações)
--   - 1 CONCLUÍDA (com quantidades efetivas)
--   - 1 AGENDADA
--   - 1 SOLICITADA
--   - 1 CANCELADA
--   - 1 EM_RECOLHIMENTO
-- ============================================================