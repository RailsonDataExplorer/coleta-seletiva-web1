package br.edu.ifs.coletaseletiva.service;

import br.edu.ifs.coletaseletiva.model.Morador;

import java.util.List;

public interface MoradorService {

    List<Morador> listarTodos();

    Morador buscarPorId(Long id);

    Morador criar(Morador morador);

    Morador atualizar(Long id, Morador moradorAtualizado);

    void deletar(Long id);
}