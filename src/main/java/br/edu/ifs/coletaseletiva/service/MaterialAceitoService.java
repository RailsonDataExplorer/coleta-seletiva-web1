package br.edu.ifs.coletaseletiva.service;

import br.edu.ifs.coletaseletiva.model.MaterialAceito;
import java.util.List;

public interface MaterialAceitoService {
    List<MaterialAceito> listarTodos();
    List<MaterialAceito> listarPorPonto(Long pontoId);
    MaterialAceito vincular(MaterialAceito materialAceito);
    void desvincular(Long id);
}