package br.edu.ifs.coletaseletiva.service;

import br.edu.ifs.coletaseletiva.exception.ResourceNotFoundException;
import br.edu.ifs.coletaseletiva.model.Morador;
import br.edu.ifs.coletaseletiva.repository.MoradorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
                .orElseThrow(() -> new ResourceNotFoundException("Morador não encontrado com o ID: " + id));
    }

    @Override
    @Transactional
    public Morador criar(Morador morador) {
        // RF01 — Normalizar e-mail (minúsculas + trim)
        if (morador.getEmail() != null) {
            morador.setEmail(morador.getEmail().toLowerCase().trim());
        }
        
        // Garante que o relacionamento bidirecional esteja correto
        if (morador.getEnderecos() != null) {
            morador.getEnderecos().forEach(endereco -> endereco.setMorador(morador));
        }
        
        return moradorRepository.save(morador);
    }

    @Override
    @Transactional
    public Morador atualizar(Long id, Morador moradorAtualizado) {
        Morador moradorExistente = buscarPorId(id);

        // Atualiza campos simples
        moradorExistente.setNome(moradorAtualizado.getNome());
        
        // RF01 — Normalizar e-mail
        if (moradorAtualizado.getEmail() != null) {
            moradorExistente.setEmail(moradorAtualizado.getEmail().toLowerCase().trim());
        }
        
        moradorExistente.setTelefone(moradorAtualizado.getTelefone());

        // Atualiza endereços (remove os antigos e adiciona os novos)
        if (moradorAtualizado.getEnderecos() != null) {
            moradorExistente.getEnderecos().clear();
            moradorAtualizado.getEnderecos().forEach(endereco -> {
                endereco.setMorador(moradorExistente);
                moradorExistente.getEnderecos().add(endereco);
            });
        }

        return moradorRepository.save(moradorExistente);
    }

    @Override
    @Transactional
    public void deletar(Long id) {
        Morador moradorExistente = buscarPorId(id);
        // RN03: Soft Delete (apenas inativa, não remove do banco)
        moradorExistente.setAtivo(false);
        moradorRepository.save(moradorExistente);
    }
}