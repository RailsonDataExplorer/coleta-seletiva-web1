package br.edu.ifs.coletaseletiva.controller;

import br.edu.ifs.coletaseletiva.model.Solicitacao;
import br.edu.ifs.coletaseletiva.service.SolicitacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/solicitacoes")
public class SolicitacaoController {

    @Autowired
    private SolicitacaoService solicitacaoService;

    @GetMapping
    public List<Solicitacao> listarTodos() {
        return solicitacaoService.listarTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Solicitacao> buscarPorId(@PathVariable Long id) {
        try {
            Solicitacao solicitacao = solicitacaoService.buscarPorId(id);
            return ResponseEntity.ok(solicitacao);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public Solicitacao criar(@RequestBody Solicitacao solicitacao) {
        return solicitacaoService.criar(solicitacao);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Solicitacao> atualizar(@PathVariable Long id, 
                                                 @RequestBody Solicitacao solicitacaoAtualizada) {
        try {
            Solicitacao atualizada = solicitacaoService.atualizar(id, solicitacaoAtualizada);
            return ResponseEntity.ok(atualizada);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        try {
            solicitacaoService.deletar(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}