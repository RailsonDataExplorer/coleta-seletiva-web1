package br.edu.ifs.coletaseletiva.repository;

import br.edu.ifs.coletaseletiva.model.PontoColeta;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PontoColetaRepository extends JpaRepository<PontoColeta, Long> {

    // ============================================================
    // RF04 — Filtrar apenas pontos ativos
    // ============================================================
    
    /**
     * Retorna todos os pontos de coleta ativos.
     * Usado no GET /api/pontos-coleta para não exibir pontos inativos.
     */
    List<PontoColeta> findByAtivoTrue();

    // ============================================================
    // RF06 — Filtros com paginação
    // ============================================================

    /**
     * Filtra pontos por nome (case-insensitive) e retorna apenas ativos.
     * Exemplo: findByNomeContainingIgnoreCaseAndAtivoTrue("eco", pageable)
     * Retorna: "Ecoponto Centro", "Ecoponto Norte", etc.
     */
    Page<PontoColeta> findByNomeContainingIgnoreCaseAndAtivoTrue(
            String nome, 
            Pageable pageable
    );

    /**
     * Filtra pontos por endereço (case-insensitive) e retorna apenas ativos.
     * Exemplo: findByEnderecoContainingIgnoreCaseAndAtivoTrue("centro", pageable)
     */
    Page<PontoColeta> findByEnderecoContainingIgnoreCaseAndAtivoTrue(
            String endereco, 
            Pageable pageable
    );

    /**
     * Filtra pontos que aceitam um determinado material (via join com MaterialAceito).
     * Exemplo: findByMaterialAceito("papel", pageable)
     * Retorna apenas pontos ativos que tenham "papel" vinculado.
     */
    @Query("SELECT DISTINCT p FROM PontoColeta p " +
           "JOIN MaterialAceito ma ON ma.ponto = p " +
           "JOIN ma.material m " +
           "WHERE LOWER(m.nome) LIKE LOWER(CONCAT('%', :material, '%')) " +
           "AND p.ativo = true")
    Page<PontoColeta> findByMaterialAceito(
            @Param("material") String material, 
            Pageable pageable
    );

    // ============================================================
    // RN05 — Bloquear inativação de ponto com solicitações abertas
    // ============================================================

    /**
     * Conta quantas solicitações abertas existem para um determinado ponto.
     * Situações consideradas "abertas": SOLICITADA, AGENDADA, EM_RECOLHIMENTO.
     * 
     * Se o resultado for > 0, o ponto NÃO pode ser inativado (RN05).
     * 
     * Exemplo de uso no Service:
     *   long abertas = countSolicitacoesAbertas(pontoId, List.of("SOLICITADA", "AGENDADA", "EM_RECOLHIMENTO"));
     *   if (abertas > 0) throw new RuntimeException("Não é possível inativar...");
     */
    @Query("SELECT COUNT(s) FROM Solicitacao s " +
           "WHERE s.pontoColeta.id = :pontoId " +
           "AND s.situacao IN :situacoesAbertas")
    long countSolicitacoesAbertas(
            @Param("pontoId") Long pontoId, 
            @Param("situacoesAbertas") List<String> situacoesAbertas
    );

    // ============================================================
    // Métodos auxiliares (opcionais, mas úteis)
    // ============================================================

    /**
     * Verifica se já existe um ponto com o mesmo nome (para evitar duplicação).
     */
    boolean existsByNomeIgnoreCase(String nome);

    /**
     * Busca ponto por nome exato (case-insensitive).
     */
    PontoColeta findByNomeIgnoreCase(String nome);
}