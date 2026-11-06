package br.edu.ifs.coletaseletiva.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "pontos_coleta")
@Data
public class PontoColeta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome; // Ex: "Ponto Centro", "Escola Municipal"

    @Column(nullable = false)
    private String endereco;

    @Column
    private Double capacidadeMaxima; // Em kg, por exemplo
}