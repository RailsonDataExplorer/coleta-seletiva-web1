package br.edu.ifs.coletaseletiva.repository;

import br.edu.ifs.coletaseletiva.model.Solicitacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SolicitacaoRepository extends JpaRepository<Solicitacao, Long> {
    Optional<Solicitacao> findByProtocolo(String protocolo);
    boolean existsByProtocolo(String protocolo);
}