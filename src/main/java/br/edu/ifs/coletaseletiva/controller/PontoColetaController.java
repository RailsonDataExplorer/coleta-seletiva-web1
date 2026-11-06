package br.edu.ifs.coletaseletiva.controller;

import br.edu.ifs.coletaseletiva.model.PontoColeta;
import br.edu.ifs.coletaseletiva.repository.PontoColetaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/pontos-coleta")
public class PontoColetaController {

    @Autowired
    private PontoColetaRepository pontoColetaRepository;

    @GetMapping
    public List<PontoColeta> listarTodos() {
        return pontoColetaRepository.findAll();
    }

    @PostMapping
    public PontoColeta criar(@RequestBody PontoColeta pontoColeta) {
        return pontoColetaRepository.save(pontoColeta);
    }
}