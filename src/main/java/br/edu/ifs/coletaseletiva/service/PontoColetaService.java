package br.edu.ifs.coletaseletiva.service;

import br.edu.ifs.coletaseletiva.model.PontoColeta;

import java.util.List;

public interface PontoColetaService {

    List<PontoColeta> listarTodos();

    PontoColeta buscarPorId(Long id);

    PontoColeta criar(PontoColeta pontoColeta);

    PontoColeta atualizar(Long id, PontoColeta pontoAtualizado);

    void deletar(Long id);
}