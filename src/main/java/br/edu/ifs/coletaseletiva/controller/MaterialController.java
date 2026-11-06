package br.edu.ifs.coletaseletiva.controller;

import br.edu.ifs.coletaseletiva.model.Material;
import br.edu.ifs.coletaseletiva.repository.MaterialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/materiais")
public class MaterialController {

    @Autowired
    private MaterialRepository materialRepository;

    @GetMapping
    public List<Material> listarTodos() {
        return materialRepository.findAll();
    }

    @PostMapping
    public Material criar(@RequestBody Material material) {
        return materialRepository.save(material);
    }
}