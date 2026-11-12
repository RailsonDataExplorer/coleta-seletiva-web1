package br.edu.ifs.coletaseletiva.controller;

import br.edu.ifs.coletaseletiva.model.MaterialAceito;
import br.edu.ifs.coletaseletiva.service.MaterialAceitoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/materiais-aceitos")
public class MaterialAceitoController {

    @Autowired
    private MaterialAceitoService materialAceitoService;

    @GetMapping
    public List<MaterialAceito> listarTodos() {
        return materialAceitoService.listarTodos();
    }

    @GetMapping("/ponto/{pontoId}")
    public List<MaterialAceito> listarPorPonto(@PathVariable Long pontoId) {
        return materialAceitoService.listarPorPonto(pontoId);
    }

    @PostMapping
    public ResponseEntity<?> vincular(@Valid @RequestBody MaterialAceito materialAceito) {
        try {
            MaterialAceito vinculo = materialAceitoService.vincular(materialAceito);
            return ResponseEntity.status(HttpStatus.CREATED).body(vinculo);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> desvincular(@PathVariable Long id) {
        try {
            materialAceitoService.desvincular(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}