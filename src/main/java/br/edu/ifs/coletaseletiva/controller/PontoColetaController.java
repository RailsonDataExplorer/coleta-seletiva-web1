package br.edu.ifs.coletaseletiva.controller;

import br.edu.ifs.coletaseletiva.model.PontoColeta;
import br.edu.ifs.coletaseletiva.service.PontoColetaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pontos-coleta")
@Tag(name = "Pontos de Coleta", description = "Gerenciamento de pontos de coleta")
public class PontoColetaController {

    @Autowired
    private PontoColetaService pontoColetaService;

    @GetMapping
    @Operation(summary = "Lista pontos com filtros e paginação (RF06)")
    public Page<PontoColeta> listar(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) String endereco,
            @RequestParam(required = false) String material,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        // RNF04 — Limite máximo de 100 registros por página
        int tamanhoPagina = Math.min(size, 100);
        Pageable pageable = PageRequest.of(page, tamanhoPagina, Sort.by("nome"));

        if (nome != null && !nome.isBlank()) {
            return pontoColetaService.buscarPorNome(nome, pageable);
        }
        if (endereco != null && !endereco.isBlank()) {
            return pontoColetaService.buscarPorEndereco(endereco, pageable);
        }
        if (material != null && !material.isBlank()) {
            return pontoColetaService.buscarPorMaterialAceito(material, pageable);
        }

        return pontoColetaService.listarTodos(pageable);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca ponto por ID")
    public ResponseEntity<PontoColeta> buscarPorId(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(pontoColetaService.buscarPorId(id));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    @Operation(summary = "Cria novo ponto")
    public ResponseEntity<PontoColeta> criar(@Valid @RequestBody PontoColeta pontoColeta) {
        return ResponseEntity.status(HttpStatus.CREATED).body(pontoColetaService.criar(pontoColeta));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualiza ponto existente")
    public PontoColeta atualizar(@PathVariable Long id, @Valid @RequestBody PontoColeta pontoColeta) {
        return pontoColetaService.atualizar(id, pontoColeta);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Inativa ponto (Soft Delete — RN03)")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        try {
            pontoColetaService.deletar(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}