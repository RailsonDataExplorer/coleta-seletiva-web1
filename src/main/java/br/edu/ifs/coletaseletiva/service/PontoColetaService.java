package br.edu.ifs.coletaseletiva.service;

import br.edu.ifs.coletaseletiva.model.PontoColeta;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PontoColetaService {
    Page<PontoColeta> listarTodos(Pageable pageable);
    Page<PontoColeta> buscarPorNome(String nome, Pageable pageable);
    Page<PontoColeta> buscarPorEndereco(String endereco, Pageable pageable);
    Page<PontoColeta> buscarPorMaterialAceito(String material, Pageable pageable);
    PontoColeta buscarPorId(Long id);
    PontoColeta criar(PontoColeta pontoColeta);
    PontoColeta atualizar(Long id, PontoColeta pontoColeta);
    void deletar(Long id);
}