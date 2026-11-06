package br.edu.ifs.coletaseletiva.controller;

import br.edu.ifs.coletaseletiva.model.Solicitacao;
import br.edu.ifs.coletaseletiva.repository.SolicitacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/solicitacoes")
public class SolicitacaoController {

    @Autowired
    private SolicitacaoRepository solicitacaoRepository;

    @GetMapping
    public List<Solicitacao> listarTodos() {
        return solicitacaoRepository.findAll();
    }

    @PostMapping
    public Solicitacao criar(@RequestBody Solicitacao solicitacao) {
        return solicitacaoRepository.save(solicitacao);
    }
}