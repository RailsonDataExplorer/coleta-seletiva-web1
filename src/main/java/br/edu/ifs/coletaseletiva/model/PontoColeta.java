package br.edu.ifs.coletaseletiva.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Entity
@Table(name = "pontos_coleta")
@Data
public class PontoColeta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O nome do ponto de coleta é obrigatório")
    @Size(max = 255, message = "O nome deve ter no máximo 255 caracteres")
    @Column(nullable = false)
    private String nome;

    @NotBlank(message = "O endereço é obrigatório")
    @Size(max = 255, message = "O endereço deve ter no máximo 255 caracteres")
    @Column(nullable = false)
    private String endereco;

    @PositiveOrZero(message = "A capacidade máxima não pode ser negativa")
    @Column
    private Double capacidadeMaxima;
}