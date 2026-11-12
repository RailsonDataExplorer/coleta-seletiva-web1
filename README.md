# 🗑️ Sistema de Coleta Seletiva - API REST

![Java](https://img.shields.io/badge/Java-23-orange?style=for-the-badge&logo=java)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-4.1.1-green?style=for-the-badge&logo=spring)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16-blue?style=for-the-badge&logo=postgresql)
![Docker](https://img.shields.io/badge/Docker-Ready-blue?style=for-the-badge&logo=docker)
![Gradle](https://img.shields.io/badge/Gradle-9.7.1-purple?style=for-the-badge&logo=gradle)
![Swagger](https://img.shields.io/badge/Swagger-OpenAPI_3-85EA2D?style=for-the-badge&logo=swagger)

API REST desenvolvida em **Spring Boot 4.1.1** para gerenciamento de um sistema municipal de **coleta seletiva de resíduos recicláveis**. O sistema permite o cadastro de **moradores** e seus **endereços**, **materiais recicláveis**, **pontos de coleta**, **operadores** e **solicitações de coleta** com fluxo completo de situações (SOLICITADA → AGENDADA → EM_RECOLHIMENTO → CONCLUÍDA), incluindo **itens de solicitação**, **histórico de alterações** e **resumo gerencial**.

Integra um banco de dados relacional **PostgreSQL 16** rodando em container **Docker**, com carga automática de esquema (`schema.sql`) e dados de demonstração (`data.sql`).

Projeto acadêmico da disciplina de **Programação Web I** do **Instituto Federal de Sergipe (IFS)**.

---

## 🛠️ Tecnologias Utilizadas

| Tecnologia | Versão | Função |
|------------|--------|--------|
| **Java** | 23 | Linguagem de programação |
| **Spring Boot** | 4.1.1 | Framework principal |
| **Spring Web MVC** | 4.1.1 | API REST |
| **Spring Data JPA** | 4.1.1 | Persistência de dados |
| **Spring Validation** | 4.1.1 | Validação de dados (Bean Validation) |
| **Hibernate ORM** | 7.4.5.Final | Mapeamento objeto-relacional |
| **SpringDoc OpenAPI** | 2.8.5 | Documentação Swagger / OpenAPI 3 |
| **PostgreSQL** | 16.15 | Banco de dados relacional |
| **Docker / Compose** | - | Containerização do banco |
| **Gradle** | 9.7.1 | Gerenciador de build |
| **Lombok** | - | Redução de código boilerplate |
| **Tomcat (embutido)** | 11.0.24 | Servidor web |

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
│       Camada de Negócio (Service)       │  ← Regras de negócio (RN01..RN06)
│      Interface + Impl + @Service        │
└──────────────────┬──────────────────────┘
                   │
┌──────────────────▼──────────────────────┐
│      Camada de Persistência (JPA)       │  ← Acesso ao banco
│    @Repository extends JpaRepository    │
└──────────────────┬──────────────────────┘
                   │
┌──────────────────▼──────────────────────┐
│       Banco de Dados (PostgreSQL)       │  ← Armazenamento
│       Container Docker (porta 5434)     │
└─────────────────────────────────────────┘

Transversal: exception/GlobalExceptionHandler (@RestControllerAdvice)
```

---

## 📂 Estrutura do Projeto

```text
coleta-seletiva/
├── src/
│   ├── main/
│   │   ├── java/br/edu/ifs/coletaseletiva/
│   │   │   ├── ColetaseletivaApplication.java     ← Classe principal
│   │   │   ├── controller/                        ← Controllers REST
│   │   │   │   ├── MaterialController.java
│   │   │   │   ├── MaterialAceitoController.java
│   │   │   │   ├── MoradorController.java
│   │   │   │   ├── PontoColetaController.java
│   │   │   │   ├── SolicitacaoController.java
│   │   │   │   └── RelatorioController.java
│   │   │   ├── model/                             ← Entidades JPA (9)
│   │   │   │   ├── Material.java
│   │   │   │   ├── MaterialAceito.java
│   │   │   │   ├── Morador.java
│   │   │   │   ├── Endereco.java
│   │   │   │   ├── PontoColeta.java
│   │   │   │   ├── Operador.java
│   │   │   │   ├── Solicitacao.java
│   │   │   │   ├── ItemSolicitacao.java
│   │   │   │   └── Historico.java
│   │   │   ├── repository/                        ← Interfaces JPA
│   │   │   │   ├── MaterialRepository.java
│   │   │   │   ├── MaterialAceitoRepository.java
│   │   │   │   ├── MoradorRepository.java
│   │   │   │   ├── PontoColetaRepository.java
│   │   │   │   ├── OperadorRepository.java
│   │   │   │   ├── SolicitacaoRepository.java
│   │   │   │   ├── ItemSolicitacaoRepository.java
│   │   │   │   └── HistoricoRepository.java
│   │   │   ├── service/                           ← Camada de negócio
│   │   │   │   ├── MaterialService.java
│   │   │   │   ├── MaterialServiceImpl.java
│   │   │   │   ├── MaterialAceitoService.java
│   │   │   │   ├── MaterialAceitoServiceImpl.java
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
│   │       ├── schema.sql                         ← Criação das tabelas
│   │       └── data.sql                           ← Dados de demonstração
│   └── test/
│       └── java/br/edu/ifs/coletaseletiva/
│           └── ColetaseletivaApplicationTests.java
├── docker-compose.yml                             ← Configuração Docker
├── build.gradle                                   ← Dependências
├── settings.gradle
├── .gitignore
└── README.md
```

---

## 🚀 Como Rodar o Projeto

### Pré-requisitos

- [Java JDK 23](https://adoptium.net/) instalado
- [Docker Desktop](https://www.docker.com/products/docker-desktop/) instalado e **em execução**
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

O banco será iniciado na porta **5434** do host (evita conflito com PostgreSQL local).

#### 3. Verifique se o container está rodando

```bash
docker ps
```

Você deve ver o container `coleta-seletiva-db` com status `Up`.

#### 4. Execute a aplicação

**Windows (PowerShell):**

```powershell
.\gradlew clean bootRun
```

**Linux/Mac:**

```bash
./gradlew clean bootRun
```

#### 5. Confirme a inicialização no log

```text
HikariPool-1 - Start completed.
Tomcat started on port 8080 (http) with context path '/'
Started ColetaseletivaApplication in 3.152 seconds
```

> ℹ️ A tarefa `bootRun` permanece em execução (≈83%) enquanto a aplicação está no ar. Isso é **normal** — não espere chegar a 100%.

#### 6. Acesse a API e a documentação

| Recurso | URL |
|---------|-----|
| **API (base)** | http://localhost:8080 |
| **Swagger UI** | http://localhost:8080/swagger-ui.html |
| **OpenAPI JSON** | http://localhost:8080/api-docs |

#### 7. (Opcional) Inspecione o banco

```bash
docker exec -it coleta-seletiva-db psql -U postgres -d coletaseletiva
```

```sql
\dt
SELECT * FROM materiais;
\q
```

---

## 📡 Endpoints da API

### 🧱 Materiais (`/api/materiais`)

| Método | Endpoint | Descrição | Status de Sucesso |
|--------|----------|-----------|-------------------|
| `GET` | `/api/materiais` | Lista todos os materiais | 200 OK |
| `GET` | `/api/materiais/{id}` | Busca material por ID | 200 OK |
| `POST` | `/api/materiais` | Cria novo material | 201 Created |
| `PUT` | `/api/materiais/{id}` | Atualiza material existente | 200 OK |
| `DELETE` | `/api/materiais/{id}` | **Inativa** material (soft delete – RN03) | 204 No Content |

### 🔗 Materiais Aceitos por Ponto (`/api/materiais-aceitos`)

| Método | Endpoint | Descrição | Status de Sucesso |
|--------|----------|-----------|-------------------|
| `GET` | `/api/materiais-aceitos` | Lista todos os vínculos | 200 OK |
| `GET` | `/api/materiais-aceitos/ponto/{pontoId}` | Lista materiais aceitos por um ponto | 200 OK |
| `POST` | `/api/materiais-aceitos` | Vincula material a ponto (RF05) | 201 Created |
| `DELETE` | `/api/materiais-aceitos/{id}` | Remove vínculo | 204 No Content |

### 👤 Moradores (`/api/moradores`)

| Método | Endpoint | Descrição | Status de Sucesso |
|--------|----------|-----------|-------------------|
| `GET` | `/api/moradores` | Lista todos os moradores | 200 OK |
| `GET` | `/api/moradores/{id}` | Busca morador por ID (com endereços) | 200 OK |
| `POST` | `/api/moradores` | Cria morador + endereços (RF01/RF02) | 201 Created |
| `PUT` | `/api/moradores/{id}` | Atualiza morador e endereços | 200 OK |
| `DELETE` | `/api/moradores/{id}` | **Inativa** morador (soft delete – RN03) | 204 No Content |

### 📍 Pontos de Coleta (`/api/pontos-coleta`)

| Método | Endpoint | Descrição | Status de Sucesso |
|--------|----------|-----------|-------------------|
| `GET` | `/api/pontos-coleta` | Lista pontos **ativos** com filtros e paginação (RF06) | 200 OK |
| `GET` | `/api/pontos-coleta/{id}` | Busca ponto por ID | 200 OK |
| `POST` | `/api/pontos-coleta` | Cria novo ponto | 201 Created |
| `PUT` | `/api/pontos-coleta/{id}` | Atualiza ponto existente | 200 OK |
| `DELETE` | `/api/pontos-coleta/{id}` | **Inativa** ponto (bloqueado se houver solicitações abertas – RN05) | 204 No Content |

#### Parâmetros de consulta (RF06 / RNF04)

| Parâmetro | Tipo | Padrão | Descrição |
|-----------|------|--------|-----------|
| `nome` | String | - | Filtra por nome (case-insensitive, parcial) |
| `endereco` | String | - | Filtra por endereço (case-insensitive, parcial) |
| `material` | String | - | Filtra pontos que aceitam o material informado |
| `page` | int | `0` | Número da página |
| `size` | int | `20` | Itens por página (**máximo 100** – RNF04) |

### 📋 Solicitações (`/api/solicitacoes`)

| Método | Endpoint | Descrição | Status de Sucesso |
|--------|----------|-----------|-------------------|
| `GET` | `/api/solicitacoes` | Lista todas as solicitações | 200 OK |
| `GET` | `/api/solicitacoes/{id}` | Busca por ID | 200 OK |
| `GET` | `/api/solicitacoes/protocolo/{protocolo}` | Busca por protocolo (ex.: `COL-DEMO0001`) | 200 OK |
| `POST` | `/api/solicitacoes` | Cria solicitação com itens (RF08) | 201 Created |
| `POST` | `/api/solicitacoes/{id}/agendar` | Agenda recolhimento (RF10) | 200 OK |
| `POST` | `/api/solicitacoes/{id}/recusar` | Recusa solicitação (motivo obrigatório) | 200 OK |
| `POST` | `/api/solicitacoes/{id}/cancelar` | Cancela solicitação (RF12) | 200 OK |
| `POST` | `/api/solicitacoes/{id}/iniciar` | Inicia recolhimento | 200 OK |
| `POST` | `/api/solicitacoes/{id}/concluir` | Conclui com quantidades efetivas (RF13) | 200 OK |
| `POST` | `/api/solicitacoes/{id}/reagendar` | Reagenda após falha (RF14) | 200 OK |

#### Parâmetros das transições

| Endpoint | Parâmetro | Tipo | Obrigatório |
|----------|-----------|------|-------------|
| `/agendar` | `data` | ISO DateTime (`2026-11-15T14:00:00`) | Sim |
| `/recusar` | `motivo` | String | Sim |
| `/cancelar` | `motivo` | String | Sim |
| `/reagendar` | `novaData`, `motivo` | ISO DateTime, String | Sim |
| `/concluir` | body | Lista de `ItemSolicitacao` | Sim |

### 📊 Relatórios (`/api/relatorios`)

| Método | Endpoint | Descrição | Status de Sucesso |
|--------|----------|-----------|-------------------|
| `GET` | `/api/relatorios/resumo` | Resumo gerencial: contagem por situação e quantidades recolhidas por material/unidade (RF15) | 200 OK |

---

## 🧪 Exemplos de Uso

### Criar um material

```bash
curl -X POST http://localhost:8080/api/materiais \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "Papel",
    "descricao": "Papelão, folhas de escritório, jornais",
    "unidade": "KG",
    "orientacao": "Limpo, seco e sem contaminantes",
    "ativo": true
  }'
```

**Resposta esperada:**

```json
{
  "id": 1,
  "nome": "Papel",
  "descricao": "Papelão, folhas de escritório, jornais",
  "unidade": "KG",
  "orientacao": "Limpo, seco e sem contaminantes",
  "ativo": true
}
```

### Criar um morador com endereços (RF01 / RF02)

```bash
curl -X POST http://localhost:8080/api/moradores \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "João Silva",
    "email": "joao.silva@email.com",
    "telefone": "79988887777",
    "enderecos": [
      {
        "logradouro": "Rua das Flores",
        "numero": "456",
        "bairro": "Jardim",
        "municipio": "Aracaju",
        "uf": "SE",
        "cep": "49000000",
        "complemento": "Casa"
      }
    ]
  }'
```

### Vincular material a um ponto de coleta (RF05)

```bash
curl -X POST http://localhost:8080/api/materiais-aceitos \
  -H "Content-Type: application/json" \
  -d '{
    "ponto": {"id": 1},
    "material": {"id": 1},
    "observacoes": "Apenas papel limpo e seco",
    "limitePorSolicitacao": 50.0
  }'
```

### Listar pontos com filtro e paginação (RF06)

```bash
curl "http://localhost:8080/api/pontos-coleta?material=papel&page=0&size=10"
```

### Criar uma solicitação de coleta (RF08)

```bash
curl -X POST http://localhost:8080/api/solicitacoes \
  -H "Content-Type: application/json" \
  -d '{
    "morador": {"id": 1},
    "pontoColeta": {"id": 1},
    "observacoes": "Caixas desmontadas",
    "itens": [
      {"material": {"id": 1}, "quantidadeEstimada": 10.5},
      {"material": {"id": 2}, "quantidadeEstimada": 5.0}
    ]
  }'
```

> ℹ️ `protocolo`, `situacao`, `enderecoCopia` e `unidadeCopia` são **gerados automaticamente** pelo Service (RN04).

### Agendar (RF10)

```bash
curl -X POST "http://localhost:8080/api/solicitacoes/3/agendar?data=2026-11-20T10:00:00"
```

### Iniciar recolhimento

```bash
curl -X POST http://localhost:8080/api/solicitacoes/3/iniciar
```

### Concluir com quantidades efetivas (RF13)

```bash
curl -X POST http://localhost:8080/api/solicitacoes/3/concluir \
  -H "Content-Type: application/json" \
  -d '[
    {"id": 5, "quantidadeEfetiva": 9.8},
    {"id": 6, "quantidadeEfetiva": 4.5}
  ]'
```

### Cancelar (RF12)

```bash
curl -X POST "http://localhost:8080/api/solicitacoes/2/cancelar?motivo=Desistencia%20do%20morador"
```

### Reagendar após falha (RF14)

```bash
curl -X POST "http://localhost:8080/api/solicitacoes/5/reagendar?novaData=2026-11-22T09:00:00&motivo=Morador%20ausente"
```

### Resumo gerencial (RF15)

```bash
curl http://localhost:8080/api/relatorios/resumo
```

**Resposta esperada:**

```json
{
  "solicitacoesPorSituacao": {
    "CONCLUÍDA": 1,
    "AGENDADA": 1,
    "SOLICITADA": 1,
    "CANCELADA": 1,
    "EM_RECOLHIMENTO": 1
  },
  "quantidadesRecolhidasPorMaterial": {
    "Papel": { "KG": 9.8 },
    "Plástico": { "KG": 4.5 }
  },
  "totalSolicitacoes": 5,
  "totalConcluidas": 1
}
```

### Exemplo de erro de regra de negócio

```bash
curl -X POST http://localhost:8080/api/solicitacoes \
  -H "Content-Type: application/json" \
  -d '{
    "morador": {"id": 1},
    "pontoColeta": {"id": 2},
    "itens": [{"material": {"id": 3}, "quantidadeEstimada": 5.0}]
  }'
```

**Resposta (400 Bad Request):**

```text
O ponto 'Ecoponto Norte' não aceita o material 'Vidro'
```

---

## 🗄️ Modelo de Dados

### Diagrama Entidade-Relacionamento

```text
┌──────────────────┐            ┌────────────────────┐            ┌──────────────────────┐
│     Morador      │            │    Solicitacao     │            │     PontoColeta      │
├──────────────────┤            ├────────────────────┤            ├──────────────────────┤
│ id (PK)          │◄───────────│ id (PK)            │───────────►│ id (PK)              │
│ nome             │    N:1     │ protocolo (UNIQUE) │    N:1     │ nome                 │
│ email (UNIQUE)   │            │ morador_id (FK)    │            │ endereco             │
│ telefone         │            │ ponto_id (FK)      │            │ contato              │
│ ativo            │            │ endereco_copia     │            │ horarios             │
│ data_cadastro    │            │ situacao           │            │ ativo                │
└────────┬─────────┘            │ data_criacao       │            │ recolhimento_domic.  │
         │ 1:N                  │ observacoes        │            └──────────┬───────────┘
         ▼                      │ versao (@Version)  │                       │ 1:N
┌──────────────────┐            └─────┬────────┬─────┘                       ▼
│     Endereco     │                  │ 1:N    │ 1:N             ┌──────────────────────┐
├──────────────────┤                  ▼        ▼                 │    MaterialAceito    │
│ id (PK)          │        ┌──────────────┐ ┌──────────────┐    ├──────────────────────┤
│ morador_id (FK)  │        │ItemSolicitacao│ │  Historico   │    │ id (PK)              │
│ logradouro       │        ├──────────────┤ ├──────────────┤    │ ponto_id (FK)        │
│ numero           │        │ id (PK)      │ │ id (PK)      │    │ material_id (FK)     │
│ bairro           │        │ solicit._id  │ │ solicit._id  │    │ observacoes          │
│ municipio        │        │ material_id  │ │ sit_anterior │    │ limite_por_solicit.  │
│ uf / cep         │        │ unidade_copia│ │ nova_situacao│    │ UNIQUE(ponto,mat.)   │
│ complemento      │        │ qtd_estimada │ │ data_alter.  │    └──────────┬───────────┘
└──────────────────┘        │ qtd_efetiva  │ │ responsavel  │               │ N:1
                            │ UNIQUE(sol,  │ │ motivo       │               ▼
                            │        mat.) │ └──────────────┘    ┌──────────────────────┐
                            └──────┬───────┘                     │       Material       │
                                   │ N:1                         ├──────────────────────┤
                                   └────────────────────────────►│ id (PK)              │
                                                                 │ nome (UNIQUE)        │
┌──────────────────┐        ┌────────────────────┐               │ descricao            │
│     Operador     │        │   operador_ponto   │               │ unidade              │
├──────────────────┤        ├────────────────────┤               │ orientacao           │
│ id (PK)          │◄───────│ operador_id (FK)   │               │ ativo                │
│ nome             │  N:M   │ ponto_id (FK)      │──────────────►└──────────────────────┘
│ email (UNIQUE)   │        │ PK(operador,ponto) │   (PontoColeta)
└──────────────────┘        └────────────────────┘
```

### Entidades e Campos

#### 📦 Material

| Campo | Tipo | Restrições |
|-------|------|------------|
| id | Long | PK, Auto Increment |
| nome | String | Not Null, Unique, max 100 |
| descricao | String | TEXT, opcional |
| unidade | String | Not Null, max 20 (`KG`, `UNIDADE`, `LITRO`) |
| orientacao | String | TEXT, opcional |
| ativo | Boolean | Not Null, default `true` |

#### 🔗 MaterialAceito

| Campo | Tipo | Restrições |
|-------|------|------------|
| id | Long | PK, Auto Increment |
| ponto | PontoColeta | FK `ponto_id`, Not Null |
| material | Material | FK `material_id`, Not Null |
| observacoes | String | TEXT, opcional |
| limitePorSolicitacao | Double | `@Positive`, opcional |
| — | — | UNIQUE(`ponto_id`, `material_id`) |

#### 👤 Morador

| Campo | Tipo | Restrições |
|-------|------|------------|
| id | Long | PK, Auto Increment |
| nome | String | Not Null, 3–100 chars |
| email | String | Not Null, Unique, `@Email`, normalizado (lower/trim) |
| telefone | String | max 15 chars |
| ativo | Boolean | Not Null, default `true` (soft delete) |
| dataCadastro | LocalDateTime | Auto (`now`) |
| enderecos | List\<Endereco\> | 1:N, cascade ALL, orphanRemoval |

#### 🏠 Endereco

| Campo | Tipo | Restrições |
|-------|------|------------|
| id | Long | PK, Auto Increment |
| morador | Morador | FK `morador_id`, Not Null, `@JsonIgnore` |
| logradouro | String | Not Null, max 255 |
| numero | String | Not Null, max 20 (use `S/N`) |
| bairro | String | Not Null, max 100 |
| municipio | String | Not Null, max 100 |
| uf | String | Not Null, exatamente 2 chars |
| cep | String | Not Null, 8–9 chars |
| complemento | String | max 255, opcional |

#### 📍 PontoColeta

| Campo | Tipo | Restrições |
|-------|------|------------|
| id | Long | PK, Auto Increment |
| nome | String | Not Null |
| endereco | String | Not Null, TEXT |
| contato | String | max 100 |
| horarios | String | max 255 |
| ativo | Boolean | Not Null, default `true` |
| recolhimentoDomiciliar | Boolean | Not Null, default `false` |

#### 👷 Operador

| Campo | Tipo | Restrições |
|-------|------|------------|
| id | Long | PK, Auto Increment |
| nome | String | Not Null, max 100 |
| email | String | Not Null, Unique, `@Email` |

#### 📋 Solicitacao

| Campo | Tipo | Restrições |
|-------|------|------------|
| id | Long | PK, Auto Increment |
| protocolo | String | Unique, Not Null, max 50, **gerado** (`COL-XXXXXXXX`) |
| morador | Morador | FK `morador_id`, Not Null |
| pontoColeta | PontoColeta | FK `ponto_id`, Not Null |
| enderecoCopia | String | Not Null, TEXT, **copiado** (RN04) |
| situacao | String | Not Null, max 30, default `SOLICITADA` |
| dataCriacao | LocalDateTime | Auto (`now`) |
| observacoes | String | TEXT, opcional |
| versao | Integer | `@Version` – optimistic locking (RN06) |
| itens | List\<ItemSolicitacao\> | 1:N, `@Valid`, cascade ALL |
| historico | List\<Historico\> | 1:N, cascade ALL |

#### 📑 ItemSolicitacao

| Campo | Tipo | Restrições |
|-------|------|------------|
| id | Long | PK, Auto Increment |
| solicitacao | Solicitacao | FK `solicitacao_id`, Not Null, `@JsonIgnore` |
| material | Material | FK `material_id`, Not Null |
| unidadeCopia | String | Not Null, max 20, **copiado** do material (RN04) |
| quantidadeEstimada | Double | Not Null, `@Positive` (RN02) |
| quantidadeEfetiva | Double | `@Positive`, preenchida só na conclusão |
| — | — | UNIQUE(`solicitacao_id`, `material_id`) |

#### 🕓 Historico

| Campo | Tipo | Restrições |
|-------|------|------------|
| id | Long | PK, Auto Increment |
| solicitacao | Solicitacao | FK `solicitacao_id`, Not Null, `@JsonIgnore` |
| situacaoAnterior | String | max 30, `null` na criação |
| novaSituacao | String | Not Null, max 30 |
| dataAlteracao | LocalDateTime | Auto (`now`) |
| responsavel | String | max 100, opcional |
| motivo | String | TEXT, opcional |

---

## ⚙️ Regras de Negócio Implementadas

| Código | Regra | Onde está implementada |
|--------|-------|------------------------|
| **RF01** | Cadastro de morador com e-mail único e normalizado | `MoradorServiceImpl.criar()` / `atualizar()` |
| **RF02** | Morador possui múltiplos endereços | `Morador` ↔ `Endereco` (1:N, cascade) |
| **RF03** | Cadastro de materiais com unidade e orientação | `MaterialServiceImpl` |
| **RF04** | Listagem apenas de pontos ativos | `PontoColetaRepository.findByAtivoTrue()` |
| **RF05** | Vínculo material ↔ ponto com limite por solicitação | `MaterialAceitoServiceImpl.vincular()` |
| **RF06** | Filtros por nome, endereço e material + paginação | `PontoColetaController.listar()` |
| **RF07** | Operadores vinculados a pontos de coleta | `Operador` + tabela `operador_ponto` |
| **RF08** | Solicitação com múltiplos itens e protocolo único | `SolicitacaoServiceImpl.criar()` |
| **RF10** | Agendamento com data futura obrigatória | `SolicitacaoServiceImpl.agendar()` |
| **RF12** | Cancelamento apenas em SOLICITADA/AGENDADA + motivo | `SolicitacaoServiceImpl.cancelar()` |
| **RF13** | Conclusão exige ao menos uma quantidade efetiva positiva | `SolicitacaoServiceImpl.concluirRecolhimento()` |
| **RF14** | Reagendamento apenas em AGENDADA/EM_RECOLHIMENTO | `SolicitacaoServiceImpl.reagendar()` |
| **RF15** | Resumo gerencial sem misturar unidades de medida | `RelatorioController.resumo()` |
| **RN01** | Material ativo, aceito pelo ponto e dentro do limite | `SolicitacaoServiceImpl.criar()` |
| **RN02** | Quantidades estimada/efetiva positivas | `@Positive` + validação no Service |
| **RN03** | Soft delete (inativação em vez de exclusão física) | Campo `ativo` em Morador, Material e PontoColeta |
| **RN04** | Preservação histórica de endereço e unidade | `endereco_copia` e `unidade_copia` |
| **RN05** | Bloqueia inativar ponto com solicitações abertas | `PontoColetaRepository.countSolicitacoesAbertas()` |
| **RN06** | Controle de concorrência otimista e transação atômica | `@Version` + `@Transactional` |
| **RNF04** | Limite máximo de 100 registros por página | `Math.min(size, 100)` no Controller |

### Máquina de Estados da Solicitação

```text
                    ┌──────────────┐
                    │  SOLICITADA  │  (criação – protocolo gerado)
                    └──┬────┬───┬──┘
        agendar        │    │   │        recusar / cancelar
              ┌────────┘    │   └────────► RECUSADA / CANCELADA
              ▼             │ cancelar
      ┌──────────────┐      └────────────► CANCELADA
      │   AGENDADA   │──────────────────► CANCELADA
      └──────┬───────┘  cancelar
   iniciar   │
             ▼
   ┌────────────────────┐   reagendar (RF14)
   │  EM_RECOLHIMENTO   │──────────────────► AGENDADA
   └─────────┬──────────┘
    concluir │ (RF13 – qtd efetiva > 0)
             ▼
      ┌──────────────┐
      │  CONCLUÍDA   │  (estado terminal)
      └──────────────┘
```

> 🔒 Transições fora dessa tabela são rejeitadas com **400 Bad Request** e a mensagem `Transição inválida de X para Y`.

---

## ✅ Validações Implementadas

O projeto utiliza **Bean Validation** (`jakarta.validation`) combinada com validações de negócio na camada de Service:

| Anotação | Função | Exemplo de Uso |
|----------|--------|----------------|
| `@NotBlank` | String não pode ser nula/vazia | `nome`, `email`, `logradouro`, `uf` |
| `@NotNull` | Objeto não pode ser nulo | `morador`, `pontoColeta`, `material` |
| `@Email` | Valida formato de e-mail | `email` do morador e do operador |
| `@Size(min, max)` | Tamanho mínimo/máximo | `nome` (3–100), `uf` (2), `cep` (8–9) |
| `@Positive` | Número maior que zero | `quantidadeEstimada`, `limitePorSolicitacao` |
| `@Valid` | Validação em cascata | `Solicitacao.itens`, parâmetros `@RequestBody` |
| `@UniqueConstraint` | Unicidade composta no banco | `(ponto_id, material_id)`, `(solicitacao_id, material_id)` |
| `@Version` | Controle de concorrência otimista | `Solicitacao.versao` |

---

## 🛡️ Tratamento de Erros

Erros são centralizados em `GlobalExceptionHandler` (`@RestControllerAdvice`) e retornam **JSON padronizado**:

```json
{
  "timestamp": "2026-11-12T10:30:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Erro de validação",
  "violations": {
    "nome": "O nome do material é obrigatório",
    "email": "O email deve ser válido"
  }
}
```

### Códigos HTTP utilizados

| Código | Significado | Quando ocorre |
|--------|-------------|---------------|
| **200** | OK | GET, PUT e transições de status bem-sucedidas |
| **201** | Created | POST de criação bem-sucedido |
| **204** | No Content | DELETE (inativação) bem-sucedido |
| **400** | Bad Request | Validação de campos ou regra de negócio violada |
| **404** | Not Found | Recurso inexistente (`ResourceNotFoundException`) |
| **409** | Conflict | Violação de integridade (e-mail/nome duplicado, FK inválida) |
| **500** | Internal Server Error | Erro inesperado do servidor |

### Exceções tratadas

| Exceção | Status | Mensagem típica |
|---------|--------|-----------------|
| `MethodArgumentNotValidException` | 400 | Erro de validação (+ mapa de `violations`) |
| `ConstraintViolationException` | 400 | Erro de validação |
| `DataIntegrityViolationException` | 409 | Já existe um registro com esse valor |
| `ResourceNotFoundException` | 404 | Material não encontrado com o ID: 99 |
| `RuntimeException` | 500 | Mensagem da exceção |

---

## 🐳 Configuração do Docker

O projeto utiliza **Docker Compose** para subir o banco PostgreSQL:

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

- **Porta do host:** `5434` (evita conflito com PostgreSQL local em 5432/5433)
- **Porta do container:** `5432` (padrão PostgreSQL)
- **Volume persistente:** `postgres_data`
- **Banco:** `coletaseletiva` | **Usuário:** `postgres` | **Senha:** `postgres123`

### Configuração da aplicação (`application.yml`)

```yaml
spring:
  application:
    name: coleta-seletiva
  datasource:
    url: jdbc:postgresql://localhost:5434/coletaseletiva
    username: postgres
    password: postgres123
    driver-class-name: org.postgresql.Driver
  jpa:
    hibernate:
      ddl-auto: none
    show-sql: false
    properties:
      hibernate:
        format_sql: true
        dialect: org.hibernate.dialect.PostgreSQLDialect
  sql:
    init:
      mode: always
      data-locations: classpath:data.sql

server:
  port: 8080

springdoc:
  api-docs:
    path: /api-docs
  swagger-ui:
    path: /swagger-ui.html
    tags-sorter: alpha
    operations-sorter: alpha
```

> ⚠️ **Atenção:** com `spring.sql.init.mode: always`, o `schema.sql` **recria as tabelas** e o `data.sql` **recarrega os dados de demonstração** a cada inicialização. Ideal para desenvolvimento/demonstração; em produção use `never` e migrações com Flyway/Liquibase.

### Dados de demonstração carregados (`data.sql`)

| Recurso | Quantidade | Observação |
|---------|-----------|------------|
| Materiais | 5 | 4 ativos + 1 inativo (teste RN01) |
| Pontos de coleta | 3 | 2 ativos + 1 inativo (teste RF04) |
| Vínculos material-ponto | 6 | Ecoponto Norte **não** aceita Vidro (teste RN01) |
| Moradores | 3 | Um deles com 2 endereços (teste RF02) |
| Operadores | 2 | Vinculados a pontos (RF07) |
| Solicitações | 5 | Uma em cada situação (testes RF10–RF15) |

---

## 📋 Comandos Úteis

### Gradle

```bash
./gradlew bootRun          # Rodar aplicação
./gradlew clean bootRun    # Limpar e rodar
./gradlew build            # Compilar e testar
./gradlew bootJar          # Gerar JAR executável
./gradlew test             # Executar testes
./gradlew --stop           # Parar daemons
```

### Windows (PowerShell)

```powershell
.\gradlew clean bootRun              # Limpar e rodar
tasklist | findstr java              # Ver processos Java
taskkill /F /IM java.exe             # Encerrar processos Java travados
netstat -ano | findstr :8080         # Ver quem usa a porta 8080
```

### Docker

```bash
docker-compose up -d       # Subir container
docker-compose down        # Parar container
docker-compose down -v     # Parar e remover volumes (reset do banco)
docker ps                  # Ver containers rodando
docker volume prune -f     # Limpar volumes órfãos
docker exec -it coleta-seletiva-db psql -U postgres -d coletaseletiva
```

### PostgreSQL (dentro do container)

```sql
\dt                          -- Listar tabelas
\d solicitacoes              -- Ver estrutura de uma tabela
\l                           -- Listar bancos
SELECT * FROM materiais;     -- Consultar materiais
SELECT protocolo, situacao FROM solicitacoes;
\q                           -- Sair
```

### Git

```bash
git status                       # Ver status
git add .                        # Adicionar arquivos
git commit -m "feat: mensagem"   # Commit
git push                         # Enviar ao GitHub
git log --oneline                # Histórico resumido
```

---

## 🩺 Solução de Problemas

| Sintoma | Causa provável | Solução |
|---------|----------------|---------|
| `Port 8080 was already in use` | Processos Java zumbis | `taskkill /F /IM java.exe` e `.\gradlew --stop` |
| `password authentication failed` | Volume Docker com senha antiga | `docker-compose down -v` e `docker-compose up -d` |
| Conecta em PostgreSQL 14 em vez de 16 | PostgreSQL local na mesma porta | Confirme a porta `5434` no `application.yml` |
| `Did not find any relations` no `\dt` | `schema.sql` não executado | Verifique `spring.sql.init.mode: always` |
| `BUILD SUCCESSFUL` e app não sobe | Foi executado `build` em vez de `bootRun` | Use `.\gradlew bootRun` |
| `bootRun` em 83% indefinidamente | Comportamento normal | A aplicação está no ar; acesse http://localhost:8080 |
| `package ...autoconfigure.domain does not exist` | `@EntityScan` incompatível com Boot 4 | Remova a anotação; `@SpringBootApplication` já escaneia subpacotes |

---

## 📌 Status do Projeto

| Etapa | Descrição | Status |
|-------|-----------|--------|
| **1** | Requisitos, protótipos e modelo de dados | ✅ Concluída |
| **2** | Banco, API e regras de negócio | ✅ Concluída |
| **3** | Interface e integração (Unidade 2) | ⏳ Pendente |
| **4** | Testes automatizados | ⏳ Parcial (`contextLoads`) |

---

## 👨‍💻 Desenvolvedor

**Railson**  
Instituto Federal de Sergipe (IFS)  
Disciplina: Programação Web I  
Ano: 2026

Repositório: [RailsonDataExplorer/coleta-seletiva-web1](https://github.com/RailsonDataExplorer/coleta-seletiva-web1)

---

## 📄 Licença

Projeto acadêmico desenvolvido para fins educacionais.

---

> 💡 **Dica:** para testar a API de forma visual, use o [Swagger UI](http://localhost:8080/swagger-ui.html), o [Postman](https://www.postman.com/) ou o [Insomnia](https://insomnia.rest/).