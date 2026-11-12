package br.edu.ifs.coletaseletiva.repository;

import br.edu.ifs.coletaseletiva.model.MaterialAceito;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MaterialAceitoRepository extends JpaRepository<MaterialAceito, Long> {
    List<MaterialAceito> findByPontoId(Long pontoId);
    Optional<MaterialAceito> findByPontoIdAndMaterialId(Long pontoId, Long materialId);
}