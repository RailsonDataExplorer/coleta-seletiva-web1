package br.edu.ifs.coletaseletiva.repository;

import br.edu.ifs.coletaseletiva.model.Operador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OperadorRepository extends JpaRepository<Operador, Long> {
}