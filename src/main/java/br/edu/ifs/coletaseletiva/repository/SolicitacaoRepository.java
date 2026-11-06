package br.edu.ifs.coletaseletiva.repository;

import br.edu.ifs.coletaseletiva.model.Solicitacao;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SolicitacaoRepository extends JpaRepository<Solicitacao, Long> {
}