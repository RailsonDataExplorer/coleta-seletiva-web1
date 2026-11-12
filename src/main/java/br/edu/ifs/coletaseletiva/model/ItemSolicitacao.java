package br.edu.ifs.coletaseletiva.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Entity
@Table(name = "item_solicitacao", 
       uniqueConstraints = @UniqueConstraint(columnNames = {"solicitacao_id", "material_id"}))
@Data
public class ItemSolicitacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Link bidirecional (não enviar no JSON, preenchido pelo Service)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "solicitacao_id", nullable = false)
    @JsonIgnore
    private Solicitacao solicitacao;

    @NotNull(message = "O material é obrigatório")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "material_id", nullable = false)
    private Material material;

    // Copiado automaticamente pelo Service a partir da unidade do material (sem @NotBlank)
    @Column(name = "unidade_copia", length = 20, nullable = false)
    private String unidadeCopia;

    @NotNull(message = "A quantidade estimada é obrigatória")
    @Positive(message = "A quantidade estimada deve ser maior que zero")
    @Column(name = "quantidade_estimada", nullable = false)
    private Double quantidadeEstimada;

    // Preenchida apenas na conclusão (RN02)
    @Positive(message = "A quantidade efetiva não pode ser negativa")
    @Column(name = "quantidade_efetiva")
    private Double quantidadeEfetiva;
}