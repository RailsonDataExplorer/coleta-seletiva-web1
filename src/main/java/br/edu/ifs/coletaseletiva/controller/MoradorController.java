package br.edu.ifs.coletaseletiva.controller;

import br.edu.ifs.coletaseletiva.model.Morador;
import br.edu.ifs.coletaseletiva.repository.MoradorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/moradores")
public class MoradorController {

    @Autowired
    private MoradorRepository moradorRepository;

    @GetMapping
    public List<Morador> listarTodos() {
        return moradorRepository.findAll();
    }

    @PostMapping
    public Morador criar(@RequestBody Morador morador) {
        return moradorRepository.save(morador);
    }
}