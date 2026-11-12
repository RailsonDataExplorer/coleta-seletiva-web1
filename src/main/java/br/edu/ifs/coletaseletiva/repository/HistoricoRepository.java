package br.edu.ifs.coletaseletiva.repository;

import br.edu.ifs.coletaseletiva.model.Historico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HistoricoRepository extends JpaRepository<Historico, Long> {
    List<Historico> findBySolicitacaoIdOrderByDataAlteracaoDesc(Long solicitacaoId);
}