package br.edu.ifs.coletaseletiva.service;

import br.edu.ifs.coletaseletiva.model.Solicitacao;
import br.edu.ifs.coletaseletiva.repository.SolicitacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SolicitacaoServiceImpl implements SolicitacaoService {

    @Autowired
    private SolicitacaoRepository solicitacaoRepository;

    @Override
    public List<Solicitacao> listarTodos() {
        return solicitacaoRepository.findAll();
    }

    @Override
    public Solicitacao buscarPorId(Long id) {
        return solicitacaoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Solicitação não encontrada com o ID: " + id));
    }

    @Override
    public Solicitacao criar(Solicitacao solicitacao) {
        return solicitacaoRepository.save(solicitacao);
    }

    @Override
    public Solicitacao atualizar(Long id, Solicitacao solicitacaoAtualizada) {
        Solicitacao solicitacaoExistente = buscarPorId(id);
        
        solicitacaoExistente.setQuantidadeKg(solicitacaoAtualizada.getQuantidadeKg());
        solicitacaoExistente.setStatus(solicitacaoAtualizada.getStatus());
        solicitacaoExistente.setMorador(solicitacaoAtualizada.getMorador());
        solicitacaoExistente.setMaterial(solicitacaoAtualizada.getMaterial());
        solicitacaoExistente.setPontoColeta(solicitacaoAtualizada.getPontoColeta());
        
        return solicitacaoRepository.save(solicitacaoExistente);
    }

    @Override
    public void deletar(Long id) {
        Solicitacao solicitacaoExistente = buscarPorId(id);
        solicitacaoRepository.delete(solicitacaoExistente);
    }
}