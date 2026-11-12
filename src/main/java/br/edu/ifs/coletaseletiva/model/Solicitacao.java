package br.edu.ifs.coletaseletiva.model;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "solicitacoes")
@Data
public class Solicitacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Gerado automaticamente pelo Service (sem @NotBlank)
    @Size(max = 50)
    @Column(unique = true, nullable = false)
    private String protocolo;

    @NotNull(message = "O morador é obrigatório")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "morador_id", nullable = false)
    private Morador morador;

    @NotNull(message = "O ponto de coleta é obrigatório")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ponto_id", nullable = false)
    private PontoColeta pontoColeta;

    // Copiado automaticamente pelo Service a partir do endereço do morador (sem @NotBlank)
    @Column(name = "endereco_copia", columnDefinition = "TEXT", nullable = false)
    private String enderecoCopia;

    // Definida automaticamente pelo Service como "SOLICITADA" (sem @NotBlank)
    @Size(max = 30)
    @Column(nullable = false)
    private String situacao = "SOLICITADA";

    @Column(name = "data_criacao")
    private LocalDateTime dataCriacao = LocalDateTime.now();

    @Column(columnDefinition = "TEXT")
    private String observacoes;

    // RN06: Controle de concorrência (optimistic locking)
    @Version
    @Column(nullable = false)
    private Integer versao = 0;

    // RF08: Uma solicitação tem vários itens
    @Valid
    @OneToMany(mappedBy = "solicitacao", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemSolicitacao> itens = new ArrayList<>();

    // RF15: Histórico de alterações
    @OneToMany(mappedBy = "solicitacao", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Historico> historico = new ArrayList<>();

    // Métodos auxiliares para manter o relacionamento bidirecional
    public void adicionarItem(ItemSolicitacao item) {
        itens.add(item);
        item.setSolicitacao(this);
    }

    public void removerItem(ItemSolicitacao item) {
        itens.remove(item);
        item.setSolicitacao(null);
    }

    public void registrarHistorico(Historico registro) {
        historico.add(registro);
        registro.setSolicitacao(this);
    }
}