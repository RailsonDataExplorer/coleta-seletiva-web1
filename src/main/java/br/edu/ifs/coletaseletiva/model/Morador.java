package br.edu.ifs.coletaseletiva.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity // Diz ao JPA que esta classe é uma tabela no banco
@Table(name = "moradores") // Define o nome da tabela (opcional, mas boa prática)
@Data // Anotação do Lombok: cria getters, setters, toString, equals e hashCode automaticamente
public class Morador {

    @Id // Define que este é o campo da Chave Primária (Primary Key)
    @GeneratedValue(strategy = GenerationType.IDENTITY) // O banco vai gerar o ID automaticamente (auto-incremento)
    private Long id;

    @Column(nullable = false, length = 100) // Não pode ser nulo no banco, max 100 caracteres
    private String nome;

    @Column(nullable = false, unique = true) // Email não pode ser nulo e não pode repetir
    private String email;

    @Column(nullable = false)
    private String endereco;

    @Column(length = 15)
    private String telefone;

    @Column(name = "data_cadastro")
    private LocalDateTime dataCadastro = LocalDateTime.now(); // Define a data atual ao criar
}