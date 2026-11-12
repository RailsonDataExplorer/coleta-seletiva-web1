package br.edu.ifs.coletaseletiva.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import java.time.LocalDateTime;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "solicitacoes")
@Data
public class Solicitacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "O morador é obrigatório")
    @ManyToOne
    @JoinColumn(name = "morador_id", nullable = false)
    private Morador morador;

    @NotNull(message = "O material é obrigatório")
    @ManyToOne
    @JoinColumn(name = "material_id", nullable = false)
    private Material material;

    @NotNull(message = "O ponto de coleta é obrigatório")
    @ManyToOne
    @JoinColumn(name = "ponto_coleta_id", nullable = false)
    private PontoColeta pontoColeta;

    @NotNull(message = "A quantidade é obrigatória")
    @Positive(message = "A quantidade deve ser maior que zero")
    @Column(nullable = false)
    private Double quantidadeKg;

    @Column(name = "data_solicitacao")
    private LocalDateTime dataSolicitacao = LocalDateTime.now();

    @Size(max = 20, message = "O status deve ter no máximo 20 caracteres")
    @Column(length = 20)
    private String status;
}