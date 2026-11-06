package br.edu.ifs.coletaseletiva.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "materiais")
@Data
public class Material {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nome; // Ex: "Papel", "Plástico PET", "Vidro"

    @Column(nullable = false)
    private String tipo; // Ex: "Reciclável", "Orgânico", "Rejeito"
}