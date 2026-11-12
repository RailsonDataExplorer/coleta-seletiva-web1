package br.edu.ifs.coletaseletiva.service;

import br.edu.ifs.coletaseletiva.model.Solicitacao;
import java.util.List;

public interface SolicitacaoService {
    List<Solicitacao> listarTodas();
    Solicitacao buscarPorId(Long id);
    Solicitacao buscarPorProtocolo(String protocolo);
    Solicitacao criar(Solicitacao solicitacao);
}