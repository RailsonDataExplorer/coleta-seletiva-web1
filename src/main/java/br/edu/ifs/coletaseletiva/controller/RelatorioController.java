package br.edu.ifs.coletaseletiva.controller;

import br.edu.ifs.coletaseletiva.model.ItemSolicitacao;
import br.edu.ifs.coletaseletiva.model.Solicitacao;
import br.edu.ifs.coletaseletiva.repository.ItemSolicitacaoRepository;
import br.edu.ifs.coletaseletiva.repository.SolicitacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/relatorios")
public class RelatorioController {

    @Autowired
    private SolicitacaoRepository solicitacaoRepository;

    @Autowired
    private ItemSolicitacaoRepository itemSolicitacaoRepository;

    /**
     * RF15 — Resumo Gerencial
     * Retorna:
     * 1. Número de solicitações por situação
     * 2. Quantidades recolhidas por material (apenas CONCLUÍDAS)
     * 3. Não mistura unidades
     */
    @GetMapping("/resumo")
    public Map<String, Object> resumo() {
        Map<String, Object> resumo = new LinkedHashMap<>();

        // 1. Contagem por situação
        List<Solicitacao> todas = solicitacaoRepository.findAll();
        Map<String, Long> porSituacao = todas.stream()
                .collect(Collectors.groupingBy(
                        Solicitacao::getSituacao,
                        Collectors.counting()
                ));
        resumo.put("solicitacoesPorSituacao", porSituacao);

        // 2. Quantidades efetivas por material (apenas CONCLUÍDAS)
        List<Solicitacao> concluidas = todas.stream()
                .filter(s -> "CONCLUÍDA".equals(s.getSituacao()))
                .collect(Collectors.toList());

        // Agrupa por material e unidade (para não misturar KG com UNIDADE)
        Map<String, Map<String, Double>> porMaterialEUnidade = new LinkedHashMap<>();

        for (Solicitacao s : concluidas) {
            for (ItemSolicitacao item : s.getItens()) {
                if (item.getQuantidadeEfetiva() == null) continue;

                String materialNome = item.getMaterial().getNome();
                String unidade = item.getUnidadeCopia();
                String chave = materialNome + " (" + unidade + ")";

                porMaterialEUnidade
                        .computeIfAbsent(materialNome, k -> new LinkedHashMap<>())
                        .merge(unidade, item.getQuantidadeEfetiva(), Double::sum);
            }
        }

        resumo.put("quantidadesRecolhidasPorMaterial", porMaterialEUnidade);

        // 3. Totais gerais
        resumo.put("totalSolicitacoes", todas.size());
        resumo.put("totalConcluidas", concluidas.size());

        return resumo;
    }
}