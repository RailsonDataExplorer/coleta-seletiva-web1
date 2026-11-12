package br.edu.ifs.coletaseletiva.service;

import br.edu.ifs.coletaseletiva.exception.ResourceNotFoundException;
import br.edu.ifs.coletaseletiva.model.Material;
import br.edu.ifs.coletaseletiva.repository.MaterialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MaterialServiceImpl implements MaterialService {

    @Autowired
    private MaterialRepository materialRepository;

    @Override
    public List<Material> listarTodos() {
        // Dica: Em um cenário real, poderíamos filtrar apenas os ativos
        return materialRepository.findAll();
    }

    @Override
    public Material buscarPorId(Long id) {
        return materialRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Material não encontrado com o ID: " + id));
    }

    @Override
    @Transactional
    public Material criar(Material material) {
        // Garante que novos materiais comecem ativos
        if (material.getAtivo() == null) {
            material.setAtivo(true);
        }
        return materialRepository.save(material);
    }

    @Override
    @Transactional
    public Material atualizar(Long id, Material materialAtualizado) {
        Material materialExistente = buscarPorId(id);

        // Atualiza os campos permitidos
        materialExistente.setNome(materialAtualizado.getNome());
        materialExistente.setDescricao(materialAtualizado.getDescricao());
        materialExistente.setUnidade(materialAtualizado.getUnidade());
        materialExistente.setOrientacao(materialAtualizado.getOrientacao());
        // Nota: Não permitimos mudar o 'ativo' diretamente pelo update padrão 
        // para evitar inconsistências, usamos o método inativar/ativar se necessário.
        
        return materialRepository.save(materialExistente);
    }

    @Override
    @Transactional
    public void deletar(Long id) {
        Material material = buscarPorId(id);
        
        // RN03: Soft Delete (Inativar em vez de excluir fisicamente)
        material.setAtivo(false);
        materialRepository.save(material);
    }
}