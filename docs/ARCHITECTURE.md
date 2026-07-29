# TODO List - Arquitetura

## Visão Geral

Aplicação full-stack para gerenciamento de tarefas com:

- **Frontend Web** (React 19 + TypeScript + Vite)
- **Frontend Mobile** (React Native + Expo)
- **API REST** (Spring Boot 3.5.10 + Java 21)
- **Banco de Dados** (MySQL 8.0)

## Diagrama de Arquitetura

```mermaid
flowchart TD
    Web["Frontend Web · React<br/>:5173 dev · :80 prod"]
    Mobile["Frontend Mobile · React Native<br/>Expo / Metro"]

    subgraph API["API Spring Boot :8080"]
        direction TB
        Controller["TaskController<br/>camada REST"]
        Service["TaskService<br/>regra de negócio · @Transactional"]
        Mapper["TaskMapper<br/>Task ↔ TaskDTO"]
        Repository["TaskRepository<br/>Spring Data JPA"]
        Handler["GlobalExceptionHandler<br/>@ControllerAdvice"]
    end

    DB[("MySQL :3406<br/>todolist_db")]

    Web -->|"HTTP · /api/tasks"| Controller
    Mobile -->|"HTTP · /api/tasks"| Controller
    Controller -->|TaskDTO| Service
    Service --> Mapper
    Service --> Repository
    Repository -->|JDBC / JPA| DB

    Controller -.->|exceções| Handler
    Service -.->|exceções| Handler
    Repository -.->|exceções| Handler
    Handler -.->|"resposta de erro"| Web

    classDef db fill:#e8e8e8,stroke:#666,color:#000
    class DB db
```

**Papel de cada peça:**

| Camada | Responsabilidade |
| ------ | ---------------- |
| `TaskController` | Recebe HTTP, delega e traduz o resultado em status code. Sem regra de negócio |
| `TaskService` | Regra de negócio e fronteira transacional. Retorna `Optional.empty()` para "não encontrado" |
| `TaskRepository` | Acesso a dados via Spring Data JPA |
| `TaskMapper` | Converte `Task` ↔ `TaskDTO`, impedindo que a estrutura da tabela vaze para a API |
| `TaskDTO` | Contrato público da API — o único tipo que cruza a fronteira HTTP |
| `Task` | Entidade JPA, visível apenas do service para baixo |
| `GlobalExceptionHandler` | Traduz exceções em respostas HTTP e impede vazamento de detalhes internos |

## Fluxo de Dados

### 1. Criar Tarefa

```mermaid
sequenceDiagram
    autonumber
    participant F as Frontend
    participant C as TaskController
    participant S as TaskService
    participant M as TaskMapper
    participant R as TaskRepository
    participant DB as MySQL

    F->>C: POST /api/tasks<br/>{ title, description }
    C->>C: @Valid — valida title e description
    C->>S: createTask(TaskDTO)
    S->>M: convertToEntity(dto)
    Note over M: completed nulo vira false
    M-->>S: Task
    S->>R: save(task)
    R->>DB: INSERT INTO tasks
    DB-->>R: id gerado
    R-->>S: Task persistida
    S->>M: convertToDTO(task)
    M-->>S: TaskDTO
    S-->>C: TaskDTO
    C-->>F: 201 Created + TaskDTO
```

### 2. Listar Tarefas

```mermaid
sequenceDiagram
    autonumber
    participant F as Frontend
    participant C as TaskController
    participant S as TaskService
    participant M as TaskMapper
    participant R as TaskRepository
    participant DB as MySQL

    F->>C: GET /api/tasks
    C->>S: getAllTasks()
    Note over S: @Transactional(readOnly = true)
    S->>R: findAll()
    R->>DB: SELECT * FROM tasks
    DB-->>R: linhas
    R-->>S: lista de Task
    S->>M: convertToDTO() para cada Task
    M-->>S: lista de TaskDTO
    S-->>C: lista de TaskDTO
    C-->>F: 200 OK + array de TaskDTO
```

### 3. Atualizar Tarefa

```mermaid
sequenceDiagram
    autonumber
    participant F as Frontend
    participant C as TaskController
    participant S as TaskService
    participant R as TaskRepository
    participant DB as MySQL

    F->>C: PUT /api/tasks/{id}<br/>{ title, description, completed? }
    C->>S: updateTask(id, TaskDTO)
    Note over S: @Transactional
    S->>R: findById(id)
    R->>DB: SELECT ... WHERE id = ?

    alt Tarefa não existe
        R-->>S: Optional.empty()
        S-->>C: Optional.empty()
        C-->>F: 404 Not Found
    else Tarefa existe
        R-->>S: Task
        S->>S: aplica title e description
        S->>S: aplica completed apenas se veio no corpo
        S->>R: save(task)
        R->>DB: UPDATE tasks SET ...
        S-->>C: TaskDTO
        C-->>F: 200 OK + TaskDTO
    end
```

> **`completed` é opcional no `PUT`.** Omitir o campo preserva o estado atual da tarefa; enviá-lo
> sobrescreve. É o que permite ao frontend renomear uma tarefa concluída sem desmarcá-la — o
> `TaskFormData` de web e mobile envia apenas `title` e `description`.

### 4. Alternar Status (Toggle)

```mermaid
sequenceDiagram
    autonumber
    participant F as Frontend
    participant C as TaskController
    participant S as TaskService
    participant R as TaskRepository
    participant DB as MySQL

    F->>C: PATCH /api/tasks/{id}/toggle
    C->>S: toggleTaskCompletion(id)
    Note over S: @Transactional
    S->>R: findById(id)
    R-->>S: Task
    S->>S: completed = !completed
    S->>R: save(task)
    R->>DB: UPDATE tasks SET completed = ?
    S-->>C: TaskDTO
    C-->>F: 200 OK + TaskDTO
```

> **Por que `@Transactional` nas escritas**: `updateTask` e `toggleTaskCompletion` leem a tarefa e
> só depois gravam. Sem a transação envolvendo as duas operações, duas requisições simultâneas
> sobre a mesma tarefa poderiam sobrescrever uma à outra (*lost update*).

### 5. Deletar Tarefa

```mermaid
sequenceDiagram
    autonumber
    participant F as Frontend
    participant C as TaskController
    participant S as TaskService
    participant R as TaskRepository
    participant DB as MySQL

    F->>C: DELETE /api/tasks/{id}
    C->>S: deleteTask(id)
    S->>R: existsById(id)

    alt Não existe
        R-->>S: false
        S-->>C: false
        C-->>F: 404 Not Found
    else Existe
        R-->>S: true
        S->>R: deleteById(id)
        R->>DB: DELETE FROM tasks WHERE id = ?
        S-->>C: true
        C-->>F: 204 No Content
    end
```

### 6. Caminho de Erro

```mermaid
flowchart TD
    E["Exceção lançada em<br/>qualquer camada"] --> H{"GlobalExceptionHandler<br/>@ControllerAdvice"}

    H -->|MethodArgumentNotValidException| V["400<br/>{ 'title': 'Title is required' }"]
    H -->|MethodArgumentTypeMismatchException| T["400<br/>{ 'error': 'Invalid value for parameter id' }"]
    H -->|HttpMessageNotReadableException| B["400<br/>{ 'error': 'Malformed or missing request body' }"]
    H -->|HttpRequestMethodNotSupportedException| A["405<br/>+ cabeçalho Allow"]
    H -->|IllegalArgumentException| I["400<br/>{ 'error': mensagem da exceção }"]
    H -->|"Exception (rede de segurança)"| G["500<br/>{ 'error': 'Internal server error...' }"]

    G -.->|"causa real"| L["Log do servidor<br/>nunca vai para o cliente"]
```

## Containers Docker

| Serviço | Container | Porta | Descrição |
| ------- | --------- | ----- | --------- |
| Frontend Web | todolist_frontend_web | 80 | React + Vite (build) |
| Backend API | todolist_backend_api | 8080 | Spring Boot |
| Database | todolist_backend_db | 3406 | MySQL 8.0 |
| Test DB | todolist_backend_test_db | 3307 | MySQL (testes) |

Todos os containers na rede: `api_network`

## Inicialização Rápida

```bash
# Subir todos os serviços
docker-compose up -d

# Ver logs da API
docker logs -f todolist_backend_api

# Testar API
curl http://localhost:8080/api/tasks

# Parar serviços
docker-compose down
```

## Stack Técnico

| Camada | Tecnologia |
| ------ | ---------- |
| Backend | Java 21 + Spring Boot 3.5.10 |
| Banco de testes | H2 em memória (profile `test`) |
| Frontend Web | React 19 + TypeScript + Vite |
| Frontend Mobile | React Native 0.76.5 + Expo ~52.0 |
| Database | MySQL 8.0 |
| ORM | Spring Data JPA + Hibernate |
| Build (backend) | Maven 3.9 |
| Container | Docker + Docker Compose |

---

**Última atualização:** 29 de julho de 2026
