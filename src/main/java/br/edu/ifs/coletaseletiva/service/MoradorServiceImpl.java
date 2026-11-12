package br.edu.ifs.coletaseletiva.service;

import br.edu.ifs.coletaseletiva.model.Morador;
import br.edu.ifs.coletaseletiva.repository.MoradorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MoradorServiceImpl implements MoradorService {

    @Autowired
    private MoradorRepository moradorRepository;

    @Override
    public List<Morador> listarTodos() {
        return moradorRepository.findAll();
    }

    @Override
    public Morador buscarPorId(Long id) {
        return moradorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Morador não encontrado com o ID: " + id));
    }

    @Override
    public Morador criar(Morador morador) {
        return moradorRepository.save(morador);
    }

    @Override
    public Morador atualizar(Long id, Morador moradorAtualizado) {
        Morador moradorExistente = buscarPorId(id);
        
        moradorExistente.setNome(moradorAtualizado.getNome());
        moradorExistente.setEmail(moradorAtualizado.getEmail());
        moradorExistente.setEndereco(moradorAtualizado.getEndereco());
        moradorExistente.setTelefone(moradorAtualizado.getTelefone());
        
        return moradorRepository.save(moradorExistente);
    }

    @Override
    public void deletar(Long id) {
        Morador moradorExistente = buscarPorId(id);
        moradorRepository.delete(moradorExistente);
    }
}