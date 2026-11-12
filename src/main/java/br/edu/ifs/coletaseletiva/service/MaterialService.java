package br.edu.ifs.coletaseletiva.service;

import br.edu.ifs.coletaseletiva.model.Material;

import java.util.List;

public interface MaterialService {

    List<Material> listarTodos();

    Material buscarPorId(Long id);

    Material criar(Material material);

    Material atualizar(Long id, Material materialAtualizado);

    void deletar(Long id);
}