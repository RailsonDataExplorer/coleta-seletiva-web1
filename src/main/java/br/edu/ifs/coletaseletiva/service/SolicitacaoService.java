package br.edu.ifs.coletaseletiva.service;

import br.edu.ifs.coletaseletiva.model.Solicitacao;

import java.util.List;

public interface SolicitacaoService {

    List<Solicitacao> listarTodos();

    Solicitacao buscarPorId(Long id);

    Solicitacao criar(Solicitacao solicitacao);

    Solicitacao atualizar(Long id, Solicitacao solicitacaoAtualizada);

    void deletar(Long id);
}