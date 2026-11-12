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
    @Size(min = 2, max = 100, message = "O nome deve ter entre 2 e 100 caracteres")
    @Column(nullable = false, unique = true)
    private String nome;

    @NotBlank(message = "O tipo do material é obrigatório")
    @Size(min = 2, max = 50, message = "O tipo deve ter entre 2 e 50 caracteres")
    @Column(nullable = false)
    private String tipo;
}