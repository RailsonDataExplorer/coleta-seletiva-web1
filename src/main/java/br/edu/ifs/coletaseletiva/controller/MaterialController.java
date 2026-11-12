package br.edu.ifs.coletaseletiva.controller;

import br.edu.ifs.coletaseletiva.model.Material;
import br.edu.ifs.coletaseletiva.service.MaterialService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/materiais")
public class MaterialController {

    @Autowired
    private MaterialService materialService;

    @GetMapping
    public List<Material> listarTodos() {
        return materialService.listarTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Material> buscarPorId(@PathVariable Long id) {
        try {
            Material material = materialService.buscarPorId(id);
            return ResponseEntity.ok(material);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public Material criar(@Valid @RequestBody Material material) {
        return materialService.criar(material);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Material> atualizar(@PathVariable Long id, 
                                              @RequestBody Material materialAtualizado) {
        try {
            Material atualizado = materialService.atualizar(id, materialAtualizado);
            return ResponseEntity.ok(atualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        try {
            materialService.deletar(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}