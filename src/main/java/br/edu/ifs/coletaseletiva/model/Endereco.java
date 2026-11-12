package br.edu.ifs.coletaseletiva.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "enderecos")
@Data
public class Endereco {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O logradouro é obrigatório")
    @Size(max = 255)
    private String logradouro;

    @NotBlank(message = "O número é obrigatório (use 'S/N' se não houver)")
    @Size(max = 20)
    private String numero;

    @NotBlank(message = "O bairro é obrigatório")
    @Size(max = 100)
    private String bairro;

    @NotBlank(message = "O município é obrigatório")
    @Size(max = 100)
    private String municipio;

    @NotBlank(message = "A UF é obrigatória")
    @Size(min = 2, max = 2, message = "A UF deve ter 2 caracteres")
    private String uf;

    @NotBlank(message = "O CEP é obrigatório")
    @Size(min = 8, max = 9, message = "CEP inválido")
    private String cep;

    @Size(max = 255)
    private String complemento;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "morador_id", nullable = false)
    @JsonIgnore // Evita loop infinito no JSON
    private Morador morador;
}