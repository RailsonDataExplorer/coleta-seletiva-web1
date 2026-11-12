package br.edu.ifs.coletaseletiva.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "moradores")
@Data
public class Morador {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O nome do morador é obrigatório")
    @Size(min = 3, max = 100, message = "O nome deve ter entre 3 e 100 caracteres")
    @Column(nullable = false, length = 100)
    private String nome;

    @NotBlank(message = "O email é obrigatório")
    @Email(message = "O email deve ser válido")
    @Column(nullable = false, unique = true)
    private String email;

    @NotBlank(message = "O endereço é obrigatório")
    @Size(max = 255, message = "O endereço deve ter no máximo 255 caracteres")
    @Column(nullable = false)
    private String endereco;

    @Size(max = 15, message = "O telefone deve ter no máximo 15 caracteres")
    @Column(length = 15)
    private String telefone;

    @Column(name = "data_cadastro")
    private LocalDateTime dataCadastro = LocalDateTime.now();
}