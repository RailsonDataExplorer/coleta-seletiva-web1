package br.edu.ifs.coletaseletiva.service;

import br.edu.ifs.coletaseletiva.model.Material;
import br.edu.ifs.coletaseletiva.repository.MaterialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MaterialServiceImpl implements MaterialService {

    @Autowired
    private MaterialRepository materialRepository;

    @Override
    public List<Material> listarTodos() {
        return materialRepository.findAll();
    }

    @Override
    public Material buscarPorId(Long id) {
        return materialRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Material não encontrado com o ID: " + id));
    }

    @Override
    public Material criar(Material material) {
        // REGRA DE NEGÓCIO: verificar se já existe material com o mesmo nome
        // (depois podemos refinar isso)
        return materialRepository.save(material);
    }

    @Override
    public Material atualizar(Long id, Material materialAtualizado) {
        Material materialExistente = buscarPorId(id);
        
        materialExistente.setNome(materialAtualizado.getNome());
        materialExistente.setTipo(materialAtualizado.getTipo());
        
        return materialRepository.save(materialExistente);
    }

    @Override
    public void deletar(Long id) {
        Material materialExistente = buscarPorId(id);
        materialRepository.delete(materialExistente);
    }
}