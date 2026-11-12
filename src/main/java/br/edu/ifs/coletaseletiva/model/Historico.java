package br.edu.ifs.coletaseletiva.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "historico")
@Data
public class Historico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "A solicitação é obrigatória")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "solicitacao_id", nullable = false)
    @JsonIgnore
    private Solicitacao solicitacao;

    @Size(max = 30)
    @Column(name = "situacao_anterior")
    private String situacaoAnterior;

    @NotBlank(message = "A nova situação é obrigatória")
    @Size(max = 30)
    @Column(name = "nova_situacao", nullable = false)
    private String novaSituacao;

    @Column(name = "data_alteracao")
    private LocalDateTime dataAlteracao = LocalDateTime.now();

    @Size(max = 100)
    private String responsavel;

    @Column(columnDefinition = "TEXT")
    private String motivo;
}