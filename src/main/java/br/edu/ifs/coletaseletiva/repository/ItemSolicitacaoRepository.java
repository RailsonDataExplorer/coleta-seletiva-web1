package br.edu.ifs.coletaseletiva.repository;

import br.edu.ifs.coletaseletiva.model.ItemSolicitacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemSolicitacaoRepository extends JpaRepository<ItemSolicitacao, Long> {
}