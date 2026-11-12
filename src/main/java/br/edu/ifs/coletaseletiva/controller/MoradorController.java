package br.edu.ifs.coletaseletiva.controller;

import br.edu.ifs.coletaseletiva.model.Morador;
import br.edu.ifs.coletaseletiva.service.MoradorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;

import java.util.List;

@RestController
@RequestMapping("/api/moradores")
public class MoradorController {

    @Autowired
    private MoradorService moradorService;

    @GetMapping
    public List<Morador> listarTodos() {
        return moradorService.listarTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Morador> buscarPorId(@PathVariable Long id) {
        try {
            Morador morador = moradorService.buscarPorId(id);
            return ResponseEntity.ok(morador);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public Morador criar(@Valid@ RequestBody Morador morador) {
        return moradorService.criar(morador);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Morador> atualizar(@PathVariable Long id, 
                                             @RequestBody Morador moradorAtualizado) {
        try {
            Morador atualizado = moradorService.atualizar(id, moradorAtualizado);
            return ResponseEntity.ok(atualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        try {
            moradorService.deletar(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}