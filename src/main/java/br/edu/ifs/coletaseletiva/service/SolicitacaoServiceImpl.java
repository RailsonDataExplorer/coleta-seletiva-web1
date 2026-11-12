package br.edu.ifs.coletaseletiva.service;

import br.edu.ifs.coletaseletiva.exception.ResourceNotFoundException;
import br.edu.ifs.coletaseletiva.model.Endereco;
import br.edu.ifs.coletaseletiva.model.Historico;
import br.edu.ifs.coletaseletiva.model.ItemSolicitacao;
import br.edu.ifs.coletaseletiva.model.Material;
import br.edu.ifs.coletaseletiva.model.MaterialAceito;
import br.edu.ifs.coletaseletiva.model.Morador;
import br.edu.ifs.coletaseletiva.model.PontoColeta;
import br.edu.ifs.coletaseletiva.model.Solicitacao;
import br.edu.ifs.coletaseletiva.repository.MaterialAceitoRepository;
import br.edu.ifs.coletaseletiva.repository.MaterialRepository;
import br.edu.ifs.coletaseletiva.repository.MoradorRepository;
import br.edu.ifs.coletaseletiva.repository.PontoColetaRepository;
import br.edu.ifs.coletaseletiva.repository.SolicitacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class SolicitacaoServiceImpl implements SolicitacaoService {

    @Autowired
    private SolicitacaoRepository solicitacaoRepository;

    @Autowired
    private MoradorRepository moradorRepository;

    @Autowired
    private PontoColetaRepository pontoColetaRepository;

    @Autowired
    private MaterialRepository materialRepository;

    @Autowired
    private MaterialAceitoRepository materialAceitoRepository;

    // ============================================================
    // LEITURA
    // ============================================================

    @Override
    public List<Solicitacao> listarTodas() {
        return solicitacaoRepository.findAll();
    }

    @Override
    public Solicitacao buscarPorId(Long id) {
        return solicitacaoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Solicitação não encontrada com ID: " + id));
    }

    @Override
    public Solicitacao buscarPorProtocolo(String protocolo) {
        return solicitacaoRepository.findByProtocolo(protocolo)
                .orElseThrow(() -> new ResourceNotFoundException("Solicitação não encontrada com protocolo: " + protocolo));
    }

    // ============================================================
    // RF08 — CRIAÇÃO COM RN01, RN04 E RN06
    // ============================================================

    @Override
    @Transactional
    public Solicitacao criar(Solicitacao solicitacao) {

        // 1. Validar Morador (RF01 + RN01)
        Morador morador = moradorRepository.findById(solicitacao.getMorador().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Morador não encontrado"));
        if (!morador.getAtivo()) {
            throw new RuntimeException("Morador inativo não pode criar solicitações");
        }

        // 2. Validar Ponto de Coleta (RF04 + RN01)
        PontoColeta ponto = pontoColetaRepository.findById(solicitacao.getPontoColeta().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Ponto de coleta não encontrado"));
        if (!ponto.getAtivo()) {
            throw new RuntimeException("Ponto de coleta inativo");
        }
        if (!ponto.getRecolhimentoDomiciliar()) {
            throw new RuntimeException("Ponto de coleta não realiza recolhimento domiciliar");
        }

        // 3. RN04 — Copiar endereço do morador (preservação histórica)
        if (morador.getEnderecos() == null || morador.getEnderecos().isEmpty()) {
            throw new RuntimeException("Morador deve ter pelo menos um endereço cadastrado");
        }
        Endereco enderecoPrincipal = morador.getEnderecos().get(0);
        String enderecoFormatado = String.format("%s, %s - %s, %s/%s - CEP: %s",
                enderecoPrincipal.getLogradouro(),
                enderecoPrincipal.getNumero(),
                enderecoPrincipal.getBairro(),
                enderecoPrincipal.getMunicipio(),
                enderecoPrincipal.getUf(),
                enderecoPrincipal.getCep());

        solicitacao.setEnderecoCopia(enderecoFormatado);
        solicitacao.setMorador(morador);
        solicitacao.setPontoColeta(ponto);
        solicitacao.setSituacao("SOLICITADA");
        solicitacao.setDataCriacao(LocalDateTime.now());

        // 4. Gerar protocolo único
        solicitacao.setProtocolo("COL-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());

        // 5. RN01 — Validar itens (materiais ativos, aceitos e dentro do limite)
        if (solicitacao.getItens() == null || solicitacao.getItens().isEmpty()) {
            throw new RuntimeException("A solicitação deve ter pelo menos um item");
        }

        for (ItemSolicitacao item : solicitacao.getItens()) {
            Material material = materialRepository.findById(item.getMaterial().getId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Material não encontrado: ID " + item.getMaterial().getId()));

            if (!material.getAtivo()) {
                throw new RuntimeException("Material inativo não pode ser solicitado: " + material.getNome());
            }

            // Verificar se o ponto aceita este material (RN01)
            MaterialAceito vinculo = materialAceitoRepository
                    .findByPontoIdAndMaterialId(ponto.getId(), material.getId())
                    .orElseThrow(() -> new RuntimeException(
                            "O ponto '" + ponto.getNome() + "' não aceita o material '" + material.getNome() + "'"));

            // RN01 — Validar limite por solicitação
            if (vinculo.getLimitePorSolicitacao() != null
                    && item.getQuantidadeEstimada() > vinculo.getLimitePorSolicitacao()) {
                throw new RuntimeException(
                        "Quantidade estimada (" + item.getQuantidadeEstimada()
                                + ") excede o limite do ponto ("
                                + vinculo.getLimitePorSolicitacao() + " " + material.getUnidade() + ")");
            }

            // RN04 — Copiar unidade do material para o item
            item.setUnidadeCopia(material.getUnidade());

            // RN02 — Quantidade estimada deve ser positiva
            if (item.getQuantidadeEstimada() == null || item.getQuantidadeEstimada() <= 0) {
                throw new RuntimeException("Quantidade estimada deve ser maior que zero");
            }

            // Link bidirecional
            item.setSolicitacao(solicitacao);
        }

        // 6. RF15/RN03 — Registrar histórico inicial
        Historico historicoInicial = new Historico();
        historicoInicial.setSituacaoAnterior(null);
        historicoInicial.setNovaSituacao("SOLICITADA");
        historicoInicial.setDataAlteracao(LocalDateTime.now());
        historicoInicial.setMotivo("Solicitação criada");
        solicitacao.registrarHistorico(historicoInicial);

        // 7. RN06 — Salvar em transação atômica (rollback em caso de falha)
        try {
            return solicitacaoRepository.save(solicitacao);
        } catch (OptimisticLockingFailureException e) {
            throw new RuntimeException("Conflito de versão: a solicitação foi modificada por outro usuário");
        }
    }

    // ============================================================
    // TRANSIÇÕES DE STATUS (RN03)
    // ============================================================

    @Transactional
    public Solicitacao agendar(Long id, LocalDateTime dataAgendamento) {
        Solicitacao solicitacao = buscarPorId(id);
        validarTransicao(solicitacao, "AGENDADA");

        if (dataAgendamento.isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Não é possível agendar para data/hora passada");
        }

        registrarMudancaStatus(solicitacao, "AGENDADA",
                "Agendamento realizado para " + dataAgendamento);
        return solicitacaoRepository.save(solicitacao);
    }

    @Transactional
    public Solicitacao recusar(Long id, String motivo) {
        Solicitacao solicitacao = buscarPorId(id);
        validarTransicao(solicitacao, "RECUSADA");

        if (motivo == null || motivo.isBlank()) {
            throw new RuntimeException("Motivo da recusa é obrigatório");
        }

        registrarMudancaStatus(solicitacao, "RECUSADA", "Recusa: " + motivo);
        return solicitacaoRepository.save(solicitacao);
    }

    @Transactional
    public Solicitacao cancelar(Long id, String motivo) {
        Solicitacao solicitacao = buscarPorId(id);

        // RF12: Só pode cancelar se estiver SOLICITADA ou AGENDADA
        if (!"SOLICITADA".equals(solicitacao.getSituacao())
                && !"AGENDADA".equals(solicitacao.getSituacao())) {
            throw new RuntimeException(
                    "Só é possível cancelar solicitações SOLICITADAS ou AGENDADAS. Situação atual: "
                            + solicitacao.getSituacao());
        }

        if (motivo == null || motivo.isBlank()) {
            throw new RuntimeException("Motivo do cancelamento é obrigatório");
        }

        registrarMudancaStatus(solicitacao, "CANCELADA", "Cancelamento: " + motivo);
        return solicitacaoRepository.save(solicitacao);
    }

    @Transactional
    public Solicitacao iniciarRecolhimento(Long id) {
        Solicitacao solicitacao = buscarPorId(id);
        validarTransicao(solicitacao, "EM_RECOLHIMENTO");

        registrarMudancaStatus(solicitacao, "EM_RECOLHIMENTO", "Recolhimento iniciado");
        return solicitacaoRepository.save(solicitacao);
    }

    @Transactional
    public Solicitacao concluirRecolhimento(Long id, List<ItemSolicitacao> itensAtualizados) {
        Solicitacao solicitacao = buscarPorId(id);
        validarTransicao(solicitacao, "CONCLUÍDA");

        // RF13: Pelo menos uma quantidade positiva
        boolean temQuantidadePositiva = false;
        for (ItemSolicitacao itemAtualizado : itensAtualizados) {
            ItemSolicitacao itemOriginal = solicitacao.getItens().stream()
                    .filter(i -> i.getId().equals(itemAtualizado.getId()))
                    .findFirst()
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Item não encontrado: " + itemAtualizado.getId()));

            if (itemAtualizado.getQuantidadeEfetiva() != null
                    && itemAtualizado.getQuantidadeEfetiva() < 0) {
                throw new RuntimeException("Quantidade efetiva não pode ser negativa");
            }

            itemOriginal.setQuantidadeEfetiva(itemAtualizado.getQuantidadeEfetiva());
            if (itemAtualizado.getQuantidadeEfetiva() != null
                    && itemAtualizado.getQuantidadeEfetiva() > 0) {
                temQuantidadePositiva = true;
            }
        }

        if (!temQuantidadePositiva) {
            throw new RuntimeException(
                    "Pelo menos um item deve ter quantidade efetiva positiva para concluir");
        }

        registrarMudancaStatus(solicitacao, "CONCLUÍDA", "Recolhimento concluído");
        return solicitacaoRepository.save(solicitacao);
    }

    @Transactional
    public Solicitacao reagendar(Long id, LocalDateTime novaData, String motivo) {
        Solicitacao solicitacao = buscarPorId(id);

        // RF14: Só reagendar se AGENDADA ou EM_RECOLHIMENTO
        if (!"AGENDADA".equals(solicitacao.getSituacao())
                && !"EM_RECOLHIMENTO".equals(solicitacao.getSituacao())) {
            throw new RuntimeException(
                    "Só é possível reagendar solicitações AGENDADAS ou EM_RECOLHIMENTO");
        }

        if (novaData.isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Nova data deve ser futura");
        }

        if (motivo == null || motivo.isBlank()) {
            throw new RuntimeException("Motivo do reagendamento é obrigatório");
        }

        // Mantém AGENDADA mas registra a tentativa no histórico
        registrarMudancaStatus(solicitacao, "AGENDADA",
                "Reagendamento: " + motivo + " | Nova data: " + novaData);
        return solicitacaoRepository.save(solicitacao);
    }

    // ============================================================
    // MÉTODOS AUXILIARES
    // ============================================================

    private void validarTransicao(Solicitacao solicitacao, String novaSituacao) {
        String atual = solicitacao.getSituacao();
        boolean valida = false;

        // Tabela de transições permitidas (RN03)
        if ("SOLICITADA".equals(atual)
                && ("AGENDADA".equals(novaSituacao)
                    || "RECUSADA".equals(novaSituacao)
                    || "CANCELADA".equals(novaSituacao))) {
            valida = true;
        } else if ("AGENDADA".equals(atual)
                && ("EM_RECOLHIMENTO".equals(novaSituacao)
                    || "CANCELADA".equals(novaSituacao))) {
            valida = true;
        } else if ("EM_RECOLHIMENTO".equals(atual)
                && ("CONCLUÍDA".equals(novaSituacao)
                    || "AGENDADA".equals(novaSituacao))) {
            valida = true;
        }

        if (!valida) {
            throw new RuntimeException("Transição inválida de " + atual + " para " + novaSituacao);
        }
    }

    private void registrarMudancaStatus(Solicitacao solicitacao,
                                        String novaSituacao,
                                        String motivo) {
        Historico registro = new Historico();
        registro.setSituacaoAnterior(solicitacao.getSituacao());
        registro.setNovaSituacao(novaSituacao);
        registro.setDataAlteracao(LocalDateTime.now());
        registro.setMotivo(motivo);
        solicitacao.registrarHistorico(registro);
        solicitacao.setSituacao(novaSituacao);
    }
}