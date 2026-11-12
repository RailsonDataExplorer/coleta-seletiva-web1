package br.edu.ifs.coletaseletiva.service;

import br.edu.ifs.coletaseletiva.exception.ResourceNotFoundException;
import br.edu.ifs.coletaseletiva.model.PontoColeta;
import br.edu.ifs.coletaseletiva.repository.PontoColetaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PontoColetaServiceImpl implements PontoColetaService {

    @Autowired
    private PontoColetaRepository pontoColetaRepository;

    // ============================================================
    // RF04 + RF06 — Listagem com paginação (apenas ativos)
    // ============================================================

    @Override
    public Page<PontoColeta> listarTodos(Pageable pageable) {
        List<PontoColeta> ativos = pontoColetaRepository.findByAtivoTrue();
        return toPage(ativos, pageable);
    }

    // ============================================================
    // RF06 — Filtros com paginação
    // ============================================================

    @Override
    public Page<PontoColeta> buscarPorNome(String nome, Pageable pageable) {
        return pontoColetaRepository.findByNomeContainingIgnoreCaseAndAtivoTrue(nome, pageable);
    }

    @Override
    public Page<PontoColeta> buscarPorEndereco(String endereco, Pageable pageable) {
        return pontoColetaRepository.findByEnderecoContainingIgnoreCaseAndAtivoTrue(endereco, pageable);
    }

    @Override
    public Page<PontoColeta> buscarPorMaterialAceito(String material, Pageable pageable) {
        return pontoColetaRepository.findByMaterialAceito(material, pageable);
    }

    // ============================================================
    // CRUD básico
    // ============================================================

    @Override
    public PontoColeta buscarPorId(Long id) {
        return pontoColetaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Ponto de coleta não encontrado com o ID: " + id));
    }

    @Override
    @Transactional
    public PontoColeta criar(PontoColeta pontoColeta) {
        if (pontoColeta.getAtivo() == null) {
            pontoColeta.setAtivo(true);
        }
        if (pontoColeta.getRecolhimentoDomiciliar() == null) {
            pontoColeta.setRecolhimentoDomiciliar(false);
        }
        return pontoColetaRepository.save(pontoColeta);
    }

    @Override
    @Transactional
    public PontoColeta atualizar(Long id, PontoColeta pontoAtualizado) {
        PontoColeta pontoExistente = buscarPorId(id);

        pontoExistente.setNome(pontoAtualizado.getNome());
        pontoExistente.setEndereco(pontoAtualizado.getEndereco());
        pontoExistente.setContato(pontoAtualizado.getContato());
        pontoExistente.setHorarios(pontoAtualizado.getHorarios());
        pontoExistente.setRecolhimentoDomiciliar(pontoAtualizado.getRecolhimentoDomiciliar());

        return pontoColetaRepository.save(pontoExistente);
    }

    @Override
    @Transactional
    public void deletar(Long id) {
        PontoColeta ponto = buscarPorId(id);

        // RN05 — Bloquear inativação se houver solicitações abertas
        List<String> situacoesAbertas = List.of("SOLICITADA", "AGENDADA", "EM_RECOLHIMENTO");
        long abertas = pontoColetaRepository.countSolicitacoesAbertas(id, situacoesAbertas);

        if (abertas > 0) {
            throw new RuntimeException(
                    "Não é possível inativar o ponto: existem " + abertas
                            + " solicitação(ões) aberta(s). Cancele ou conclua antes de inativar.");
        }

        // RN03: Soft Delete
        ponto.setAtivo(false);
        pontoColetaRepository.save(ponto);
    }

    // ============================================================
    // Método auxiliar: converter List em Page
    // ============================================================

    /**
     * Converte uma List em Page, aplicando paginação manual.
     * Usado quando o repositório retorna List (ex: findByAtivoTrue).
     */
    private Page<PontoColeta> toPage(List<PontoColeta> lista, Pageable pageable) {
        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), lista.size());

        List<PontoColeta> subLista = start < end
                ? lista.subList(start, end)
                : List.of();

        return new PageImpl<>(subLista, pageable, lista.size());
    }
}