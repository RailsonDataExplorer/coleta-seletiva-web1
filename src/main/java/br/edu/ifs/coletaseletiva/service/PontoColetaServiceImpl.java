package br.edu.ifs.coletaseletiva.service;

import br.edu.ifs.coletaseletiva.model.PontoColeta;
import br.edu.ifs.coletaseletiva.repository.PontoColetaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PontoColetaServiceImpl implements PontoColetaService {

    @Autowired
    private PontoColetaRepository pontoColetaRepository;

    @Override
    public List<PontoColeta> listarTodos() {
        return pontoColetaRepository.findAll();
    }

    @Override
    public PontoColeta buscarPorId(Long id) {
        return pontoColetaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ponto de coleta não encontrado com o ID: " + id));
    }

    @Override
    public PontoColeta criar(PontoColeta pontoColeta) {
        return pontoColetaRepository.save(pontoColeta);
    }

    @Override
    public PontoColeta atualizar(Long id, PontoColeta pontoAtualizado) {
        PontoColeta pontoExistente = buscarPorId(id);
        
        pontoExistente.setNome(pontoAtualizado.getNome());
        pontoExistente.setEndereco(pontoAtualizado.getEndereco());
        pontoExistente.setCapacidadeMaxima(pontoAtualizado.getCapacidadeMaxima());
        
        return pontoColetaRepository.save(pontoExistente);
    }

    @Override
    public void deletar(Long id) {
        PontoColeta pontoExistente = buscarPorId(id);
        pontoColetaRepository.delete(pontoExistente);
    }
}