package br.edu.ifs.coletaseletiva.controller;

import br.edu.ifs.coletaseletiva.model.ItemSolicitacao;
import br.edu.ifs.coletaseletiva.model.Solicitacao;
import br.edu.ifs.coletaseletiva.service.SolicitacaoServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/solicitacoes")
public class SolicitacaoController {

    @Autowired
    private SolicitacaoServiceImpl solicitacaoService;

    @GetMapping
    public List<Solicitacao> listarTodas() {
        return solicitacaoService.listarTodas();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Solicitacao> buscarPorId(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(solicitacaoService.buscarPorId(id));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/protocolo/{protocolo}")
    public ResponseEntity<Solicitacao> buscarPorProtocolo(@PathVariable String protocolo) {
        try {
            return ResponseEntity.ok(solicitacaoService.buscarPorProtocolo(protocolo));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<?> criar(@Valid @RequestBody Solicitacao solicitacao) {
        try {
            Solicitacao nova = solicitacaoService.criar(solicitacao);
            return ResponseEntity.status(HttpStatus.CREATED).body(nova);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // ===== Transições de status (RF10 a RF14) =====

    @PostMapping("/{id}/agendar")
    public ResponseEntity<?> agendar(@PathVariable Long id,
                                     @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime data) {
        try {
            return ResponseEntity.ok(solicitacaoService.agendar(id, data));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/{id}/recusar")
    public ResponseEntity<?> recusar(@PathVariable Long id, @RequestParam String motivo) {
        try {
            return ResponseEntity.ok(solicitacaoService.recusar(id, motivo));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/{id}/cancelar")
    public ResponseEntity<?> cancelar(@PathVariable Long id, @RequestParam String motivo) {
        try {
            return ResponseEntity.ok(solicitacaoService.cancelar(id, motivo));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/{id}/iniciar")
    public ResponseEntity<?> iniciar(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(solicitacaoService.iniciarRecolhimento(id));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/{id}/concluir")
    public ResponseEntity<?> concluir(@PathVariable Long id, @RequestBody List<ItemSolicitacao> itens) {
        try {
            return ResponseEntity.ok(solicitacaoService.concluirRecolhimento(id, itens));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/{id}/reagendar")
    public ResponseEntity<?> reagendar(@PathVariable Long id,
                                       @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime novaData,
                                       @RequestParam String motivo) {
        try {
            return ResponseEntity.ok(solicitacaoService.reagendar(id, novaData, motivo));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}