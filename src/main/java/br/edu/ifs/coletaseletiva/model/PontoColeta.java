package br.edu.ifs.coletaseletiva.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Entity
@Table(name = "pontos_coleta")
@Data
public class PontoColeta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O nome do ponto é obrigatório")
    @Column(nullable = false)
    private String nome;

    @NotBlank(message = "O endereço é obrigatório")
    @Column(columnDefinition = "TEXT", nullable = false)
    private String endereco;

    private String contato;
    private String horarios;

    @Column(nullable = false)
    private Boolean ativo = true;

    @Column(name = "recolhimento_domiciliar", nullable = false)
    private Boolean recolhimentoDomiciliar = false;
}