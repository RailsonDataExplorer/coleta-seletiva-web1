# 🗑️ Sistema de Coleta Seletiva - API REST

![Java](https://img.shields.io/badge/Java-23-orange?style=for-the-badge&logo=java)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-4.1.1-green?style=for-the-badge&logo=spring)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16-blue?style=for-the-badge&logo=postgresql)
![Docker](https://img.shields.io/badge/Docker-Ready-blue?style=for-the-badge&logo=docker)
![Gradle](https://img.shields.io/badge/Gradle-9.7.1-purple?style=for-the-badge&logo=gradle)

---

## 📖 Sobre o Projeto

API REST desenvolvida em **Spring Boot 4.1.1** para gerenciamento de solicitações de coleta seletiva. O sistema permite o cadastro e gerenciamento de **moradores**, **materiais recicláveis**, **pontos de coleta** e **solicitações de coleta**, integrando um banco de dados relacional PostgreSQL rodando em container Docker.

Projeto acadêmico da disciplina de **Programação Web I** do **Instituto Federal de Sergipe (IFS)**.

---

## 🛠️ Tecnologias Utilizadas

| Tecnologia | Versão | Função |
|------------|--------|--------|
| **Java** | 23 | Linguagem de programação |
| **Spring Boot** | 4.1.1 | Framework principal |
| **Spring Web MVC** | 4.1.1 | API REST |
| **Spring Data JPA** | 4.1.1 | Persistência de dados |
| **Spring Validation** | 4.1.1 | Validação de dados |
| **Hibernate ORM** | 7.4.5.Final | Mapeamento objeto-relacional |
| **PostgreSQL** | 16.15 | Banco de dados relacional |
| **Docker** | - | Containerização do banco |
| **Gradle** | 9.7.1 | Gerenciador de build |
| **Lombok** | - | Redução de código boilerplate |
| **Tomcat** | 11.0.24 | Servidor web embutido |

---

## 🏗️ Arquitetura do Projeto

O projeto segue o padrão de arquitetura em **camadas**, promovendo separação de responsabilidades:

```text
┌─────────────────────────────────────────┐
│         Camada Web (Controller)         │  ← Recebe requisições HTTP
│    @RestController + @RequestMapping    │
└──────────────────┬──────────────────────┘
                   │
┌──────────────────▼──────────────────────┐
│       Camada de Negócio (Service)       │  ← Regras de negócio
│    @Service + Interface + Impl          │
└──────────────────┬──────────────────────┘
                   │
┌──────────────────▼──────────────────────┐
│      Camada de Persistência (JPA)       │  ← Acesso ao banco
│    @Repository extends JpaRepository    │
└──────────────────┬──────────────────────┘
                   │
┌──────────────────▼──────────────────────┐
│          Banco de Dados (PostgreSQL)    │  ← Armazenamento
│         Container Docker (porta 5434)   │
└─────────────────────────────────────────┘
```

---

## 📂 Estrutura do Projeto

```text
coleta-seletiva/
├── src/
│   ├── main/
│   │   ├── java/br/edu/ifs/coletaseletiva/
│   │   │   ├── ColetaseletivaApplication.java    ← Classe principal
│   │   │   ├── controller/                        ← Controllers REST
│   │   │   │   ├── MaterialController.java
│   │   │   │   ├── MoradorController.java
│   │   │   │   ├── PontoColetaController.java
│   │   │   │   └── SolicitacaoController.java
│   │   │   ├── model/                             ← Entidades JPA
│   │   │   │   ├── Material.java
│   │   │   │   ├── Morador.java
│   │   │   │   ├── PontoColeta.java
│   │   │   │   └── Solicitacao.java
│   │   │   ├── repository/                        ← Interfaces JPA
│   │   │   │   ├── MaterialRepository.java
│   │   │   │   ├── MoradorRepository.java
│   │   │   │   ├── PontoColetaRepository.java
│   │   │   │   └── SolicitacaoRepository.java
│   │   │   ├── service/                           ← Camada de negócio
│   │   │   │   ├── MaterialService.java
│   │   │   │   ├── MaterialServiceImpl.java
│   │   │   │   ├── MoradorService.java
│   │   │   │   ├── MoradorServiceImpl.java
│   │   │   │   ├── PontoColetaService.java
│   │   │   │   ├── PontoColetaServiceImpl.java
│   │   │   │   ├── SolicitacaoService.java
│   │   │   │   └── SolicitacaoServiceImpl.java
│   │   │   └── exception/                         ← Tratamento de erros
│   │   │       ├── GlobalExceptionHandler.java
│   │   │       └── ResourceNotFoundException.java
│   │   └── resources/
│   │       ├── application.yml                    ← Configurações
│   │       └── schema.sql                         ← Criação das tabelas
│   └── test/
├── docker-compose.yml                             ← Configuração Docker
├── build.gradle                                   ← Dependências
├── .gitignore                                     ← Arquivos ignorados
└── README.md                                      ← Este arquivo
```

---

## 🚀 Como Rodar o Projeto

### Pré-requisitos

- [Java JDK 23](https://adoptium.net/) instalado
- [Docker Desktop](https://www.docker.com/products/docker-desktop/) instalado e rodando
- [Git](https://git-scm.com/) instalado

### Passo a Passo

#### 1. Clone o repositório

```bash
git clone https://github.com/RailsonDataExplorer/coleta-seletiva-web1.git
cd coleta-seletiva-web1
```

#### 2. Suba o banco de dados PostgreSQL

```bash
docker-compose up -d
```

*O banco será iniciado na porta **5434** do host.*

#### 3. Verifique se o container está rodando

```bash
docker ps
```

*Você deve ver o container `coleta-seletiva-db` com status `Up`.*

#### 4. Execute a aplicação

**Windows (PowerShell):**
```powershell
.\gradlew bootRun
```

**Linux/Mac:**
```bash
./gradlew bootRun
```

#### 5. Acesse a API

A API estará disponível em: **http://localhost:8080**

---

## 📡 Endpoints da API

### 🧱 Materiais (`/api/materiais`)

| Método | Endpoint | Descrição | Status de Sucesso |
|--------|----------|-----------|-------------------|
| `GET` | `/api/materiais` | Lista todos os materiais | 200 OK |
| `GET` | `/api/materiais/{id}` | Busca material por ID | 200 OK |
| `POST` | `/api/materiais` | Cria novo material | 201 Created |
| `PUT` | `/api/materiais/{id}` | Atualiza material existente | 200 OK |
| `DELETE` | `/api/materiais/{id}` | Remove material | 204 No Content |

### 👤 Moradores (`/api/moradores`)

| Método | Endpoint | Descrição | Status de Sucesso |
|--------|----------|-----------|-------------------|
| `GET` | `/api/moradores` | Lista todos os moradores | 200 OK |
| `GET` | `/api/moradores/{id}` | Busca morador por ID | 200 OK |
| `POST` | `/api/moradores` | Cria novo morador | 201 Created |
| `PUT` | `/api/moradores/{id}` | Atualiza morador existente | 200 OK |
| `DELETE` | `/api/moradores/{id}` | Remove morador | 204 No Content |

### 📍 Pontos de Coleta (`/api/pontos-coleta`)

| Método | Endpoint | Descrição | Status de Sucesso |
|--------|----------|-----------|-------------------|
| `GET` | `/api/pontos-coleta` | Lista todos os pontos | 200 OK |
| `GET` | `/api/pontos-coleta/{id}` | Busca ponto por ID | 200 OK |
| `POST` | `/api/pontos-coleta` | Cria novo ponto | 201 Created |
| `PUT` | `/api/pontos-coleta/{id}` | Atualiza ponto existente | 200 OK |
| `DELETE` | `/api/pontos-coleta/{id}` | Remove ponto | 204 No Content |

### 📋 Solicitações (`/api/solicitacoes`)

| Método | Endpoint | Descrição | Status de Sucesso |
|--------|----------|-----------|-------------------|
| `GET` | `/api/solicitacoes` | Lista todas as solicitações | 200 OK |
| `GET` | `/api/solicitacoes/{id}` | Busca solicitação por ID | 200 OK |
| `POST` | `/api/solicitacoes` | Cria nova solicitação | 201 Created |
| `PUT` | `/api/solicitacoes/{id}` | Atualiza solicitação existente | 200 OK |
| `DELETE` | `/api/solicitacoes/{id}` | Remove solicitação | 204 No Content |

---

## 🧪 Exemplos de Uso

### Criar um material

```bash
curl -X POST http://localhost:8080/api/materiais \
  -H "Content-Type: application/json" \
  -d '{"nome":"Papel","tipo":"Reciclável"}'
```

**Resposta esperada:**
```json
{
  "id": 1,
  "nome": "Papel",
  "tipo": "Reciclável"
}
```

### Listar todos os materiais

```bash
curl http://localhost:8080/api/materiais
```

### Buscar material por ID

```bash
curl http://localhost:8080/api/materiais/1
```

### Atualizar um material

```bash
curl -X PUT http://localhost:8080/api/materiais/1 \
  -H "Content-Type: application/json" \
  -d '{"nome":"Papel A4","tipo":"Reciclável"}'
```

### Deletar um material

```bash
curl -X DELETE http://localhost:8080/api/materiais/1
```

### Criar um morador

```bash
curl -X POST http://localhost:8080/api/moradores \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "João Silva",
    "email": "joao@email.com",
    "endereco": "Rua A, 123",
    "telefone": "79999999999"
  }'
```

### Criar uma solicitação de coleta

```bash
curl -X POST http://localhost:8080/api/solicitacoes \
  -H "Content-Type: application/json" \
  -d '{
    "morador": {"id": 1},
    "material": {"id": 1},
    "pontoColeta": {"id": 1},
    "quantidadeKg": 5.5,
    "status": "PENDENTE"
  }'
```

---

## 🗄️ Modelo de Dados

### Diagrama Entidade-Relacionamento

```text
┌──────────────┐       ┌──────────────────┐       ┌─────────────────┐
│   Morador    │       │   Solicitacao    │       │   PontoColeta   │
├──────────────┤       ├──────────────────┤       ├─────────────────┤
│ id (PK)      │◄──────│ id (PK)          │──────►│ id (PK)         │
│ nome         │       │ morador_id (FK)  │       │ nome            │
│ email        │       │ material_id (FK) │       │ endereco        │
│ endereco     │       │ ponto_coleta_id  │       │ capacidade_max  │
│ telefone     │       │ quantidade_kg    │       └─────────────────┘
│ data_cadastro│       │ data_solicitacao │
└──────────────┘       │ status           │
                       └────────┬─────────┘
                                │
                       ┌────────▼─────────┐
                       │    Material      │
                       ├──────────────────┤
                       │ id (PK)          │
                       │ nome (UNIQUE)    │
                       │ tipo             │
                       └──────────────────┘
```

### Entidades e Campos

#### 📦 Material

| Campo | Tipo | Restrições |
|-------|------|------------|
| id | Long | PK, Auto Increment |
| nome | String | Not Null, Unique, 2-100 chars |
| tipo | String | Not Null, 2-50 chars |

#### 👤 Morador

| Campo | Tipo | Restrições |
|-------|------|------------|
| id | Long | PK, Auto Increment |
| nome | String | Not Null, 3-100 chars |
| email | String | Not Null, Unique, Email válido |
| endereco | String | Not Null, max 255 chars |
| telefone | String | max 15 chars |
| dataCadastro | LocalDateTime | Auto (now) |

#### 📍 PontoColeta

| Campo | Tipo | Restrições |
|-------|------|------------|
| id | Long | PK, Auto Increment |
| nome | String | Not Null, max 255 chars |
| endereco | String | Not Null, max 255 chars |
| capacidadeMaxima | Double | >= 0 |

#### 📋 Solicitacao

| Campo | Tipo | Restrições |
|-------|------|------------|
| id | Long | PK, Auto Increment |
| morador | Morador | FK, Not Null |
| material | Material | FK, Not Null |
| pontoColeta | PontoColeta | FK, Not Null |
| quantidadeKg | Double | Not Null, > 0 |
| dataSolicitacao | LocalDateTime | Auto (now) |
| status | String | max 20 chars |

---

## ✅ Validações Implementadas

O projeto utiliza **Bean Validation** (`jakarta.validation`) para garantir a integridade dos dados:

| Anotação | Função | Exemplo de Uso |
|----------|--------|----------------|
| `@NotBlank` | String não pode ser nula/vazia | `nome`, `email` |
| `@NotNull` | Qualquer tipo não pode ser nulo | `morador`, `quantidadeKg` |
| `@Email` | Valida formato de email | `email` do morador |
| `@Size(min, max)` | Tamanho mínimo/máximo | `nome`, `telefone` |
| `@Positive` | Número maior que zero | `quantidadeKg` |
| `@PositiveOrZero` | Número >= 0 | `capacidadeMaxima` |

---

## 🛡️ Tratamento de Erros

A API retorna erros em formato **JSON padronizado**:

```json
{
  "timestamp": "2026-11-12T00:00:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Erro de validação",
  "violations": {
    "nome": "O nome do material é obrigatório"
  }
}
```

### Códigos HTTP utilizados

| Código | Significado | Quando ocorre |
|--------|-------------|---------------|
| **200** | OK | GET e PUT bem-sucedidos |
| **201** | Created | POST bem-sucedido |
| **204** | No Content | DELETE bem-sucedido |
| **400** | Bad Request | Dados inválidos (validação) |
| **404** | Not Found | Recurso não encontrado |
| **409** | Conflict | Conflito de dados (ex: nome duplicado) |
| **500** | Internal Server Error | Erro inesperado do servidor |

---

## 🐳 Configuração do Docker

O projeto utiliza **Docker Compose** para subir o banco PostgreSQL automaticamente:

```yaml
services:
  postgres-coleta:
    image: postgres:16
    container_name: coleta-seletiva-db
    environment:
      POSTGRES_DB: coletaseletiva
      POSTGRES_USER: postgres
      POSTGRES_PASSWORD: postgres123
    ports:
      - "5434:5432"
    volumes:
      - postgres_data:/var/lib/postgresql/data
    restart: unless-stopped

volumes:
  postgres_data:
```

**Detalhes:**
- **Porta do host:** 5434 (evita conflito com PostgreSQL local)
- **Porta do container:** 5432 (padrão PostgreSQL)
- **Volume persistente:** `postgres_data` (dados não são perdidos ao reiniciar)

---

## 📋 Comandos Úteis

### Gradle
```bash
./gradlew bootRun          # Rodar aplicação
./gradlew clean bootRun    # Limpar e rodar
./gradlew build            # Compilar
./gradlew bootJar          # Gerar JAR executável
./gradlew --stop           # Parar daemons
```

### Docker
```bash
docker-compose up -d       # Subir container
docker-compose down        # Parar container
docker ps                  # Ver containers rodando
docker exec -it coleta-seletiva-db psql -U postgres -d coletaseletiva
```

### PostgreSQL (dentro do container)
```sql
\dt                        # Listar tabelas
\d nome_tabela             # Ver estrutura
\l                         # Listar bancos
\q                         # Sair
```

### Git
```bash
git status                 # Ver status
git add .                  # Adicionar arquivos
git commit -m "mensagem"   # Commit
git push                   # Enviar ao GitHub
```

---

## 👨‍💻 Desenvolvedor

**Railson**  
Instituto Federal de Sergipe (IFS)  
Disciplina: Programação Web I  
Ano: 2026

---

## 📄 Licença

Projeto acadêmico desenvolvido para fins educacionais.

---

> 💡 **Dica:** Para testar a API de forma visual, recomenda-se o uso do [Postman](https://www.postman.com/) ou [Insomnia](https://insomnia.rest/).