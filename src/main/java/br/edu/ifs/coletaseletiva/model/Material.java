package br.edu.ifs.coletaseletiva.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Entity
@Table(name = "materiais")
@Data
public class Material {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O nome do material é obrigatório")
    @Size(max = 100)
    @Column(unique = true, nullable = false)
    private String nome;

    @Column(columnDefinition = "TEXT")
    private String descricao;

    @NotBlank(message = "A unidade de medida é obrigatória")
    @Size(max = 20)
    @Column(nullable = false)
    private String unidade; // Ex: KG, UNIDADE, LITRO

    @Column(columnDefinition = "TEXT")
    private String orientacao;

    @Column(nullable = false)
    private Boolean ativo = true;
}