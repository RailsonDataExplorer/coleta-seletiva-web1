package br.edu.ifs.coletaseletiva.controller;

import br.edu.ifs.coletaseletiva.model.PontoColeta;
import br.edu.ifs.coletaseletiva.service.PontoColetaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pontos-coleta")
public class PontoColetaController {

    @Autowired
    private PontoColetaService pontoColetaService;

    @GetMapping
    public List<PontoColeta> listarTodos() {
        return pontoColetaService.listarTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PontoColeta> buscarPorId(@PathVariable Long id) {
        try {
            PontoColeta ponto = pontoColetaService.buscarPorId(id);
            return ResponseEntity.ok(ponto);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public PontoColeta criar(@Valid @RequestBody PontoColeta pontoColeta) {
        return pontoColetaService.criar(pontoColeta);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PontoColeta> atualizar(@PathVariable Long id, 
                                                 @RequestBody PontoColeta pontoAtualizado) {
        try {
            PontoColeta atualizado = pontoColetaService.atualizar(id, pontoAtualizado);
            return ResponseEntity.ok(atualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        try {
            pontoColetaService.deletar(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}