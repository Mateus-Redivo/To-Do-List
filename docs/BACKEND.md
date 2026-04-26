# API To-Do List - Backend

API REST do projeto de gerenciamento de tarefas desenvolvida com Spring Boot.

## Tecnologias Utilizadas

- **Java 21**: Linguagem de programação principal
- **Spring Boot 3.5.10**: Framework para criação de aplicações Java
- **Spring Data JPA**: Abstração para acesso a dados e persistência
- **MySQL**: Banco de dados relacional
- **Maven**: Gerenciador de dependências e build
- **Hibernate**: ORM para mapeamento objeto-relacional
- **SpringDoc OpenAPI 2.5.0**: Documentação automática da API (Swagger)
- **Bean Validation**: Validação de dados com anotações
- **Spring Boot Actuator**: Monitoramento e métricas

## Como Começar

### Requisitos do Sistema

- Java 21 ou superior
- Maven 3.6 ou superior
- MySQL 8.0 ou superior (ou via Docker)
- Git para controle de versão

### Configuração do Banco de Dados

Edite `src/main/resources/application.properties` ou defina variáveis de ambiente:

```properties
# Data Source Configuration (MySQL)
spring.datasource.url=${SPRING_DATASOURCE_URL:jdbc:mysql://localhost:3406/todolist_db?allowPublicKeyRetrieval=true&useSSL=false}
spring.datasource.username=${SPRING_DATASOURCE_USERNAME:todolist_user}
spring.datasource.password=${SPRING_DATASOURCE_PASSWORD:todolist_password}
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# JPA/Hibernate
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect
```

> **Nota**: Defina `SPRING_DATASOURCE_URL`, `SPRING_DATASOURCE_USERNAME` e `SPRING_DATASOURCE_PASSWORD` como variáveis de ambiente em produção.

## Comandos Principais

```bash
# Windows — iniciar servidor de desenvolvimento
./mvnw.cmd spring-boot:run

# Linux/Mac — iniciar servidor de desenvolvimento
./mvnw spring-boot:run

# Executar testes
./mvnw test

# Criar build para produção
./mvnw clean package

# Executar o JAR gerado
java -jar target/todolist-api-1.0.0-RELEASE.jar
```

O servidor ficará disponível em `http://localhost:8080`.

## Arquitetura da API

### Estrutura em Camadas

```plaintext
src/main/java/com/todolist/api/
├── controller/       # Camada de apresentação (REST endpoints)
├── service/          # Camada de lógica de negócio
├── repository/       # Camada de acesso a dados
├── model/            # Entidades do banco de dados
├── dto/              # Data Transfer Objects
├── mapper/           # Conversão entre Entity e DTO
├── exceptions/       # Tratamento global de erros
└── config/           # Configurações (CORS, etc.)
```

### Endpoints da API

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| `GET` | `/api/tasks` | Lista todas as tarefas |
| `GET` | `/api/tasks/{id}` | Busca uma tarefa por ID |
| `POST` | `/api/tasks` | Cria uma nova tarefa |
| `PUT` | `/api/tasks/{id}` | Atualiza título, descrição e status |
| `PATCH` | `/api/tasks/{id}/toggle` | Alterna o status de conclusão |
| `DELETE` | `/api/tasks/{id}` | Remove uma tarefa |

### Modelo de Dados

A entidade `Task` possui os seguintes campos:

```json
{
  "id": 1,
  "title": "Estudar Spring Boot",
  "description": "Completar o tutorial de Spring Boot",
  "completed": false
}
```

**Validações:**

- `title`: Obrigatório, máximo 100 caracteres
- `description`: Opcional, máximo 500 caracteres
- `completed`: Booleano, valor padrão `false`

## Guia de Implementação

### 1. Model (Entidade)

A classe `Task.java` representa a entidade no banco de dados:

```java
@Entity
@Table(name = "tasks")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column
    private String description;

    @Column(nullable = false)
    private boolean completed;

    public Task() {
        this.title = "";
        this.completed = false;
    }

    public Task(String title, String description) {
        this.title = title;
        this.description = description;
        this.completed = false;
    }
    // getters e setters...
}
```

### 2. DTO (Data Transfer Object)

O `TaskDTO.java` é usado para transferência de dados entre camadas:

```java
public class TaskDTO {
    @JsonProperty("id")
    private Long id;

    @NotBlank(message = "Title is required")
    @Size(max = 100, message = "Title must be less than 100 characters")
    @JsonProperty("title")
    private String title;

    @Size(max = 500, message = "Description must be less than 500 characters")
    @JsonProperty("description")
    private String description;

    @JsonProperty("completed")
    private boolean completed;

    public TaskDTO() {
        this.title = "";
        this.completed = false;
    }

    public TaskDTO(Long id, String title, String description, boolean completed) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.completed = completed;
    }
    // getters e setters...
}
```

### 3. Repository

Interface que estende `JpaRepository` para acesso a dados:

```java
@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    // Métodos customizados podem ser adicionados aqui
}
```

### 4. Service

Camada de lógica de negócio:

```java
@Service
public class TaskService {
    // getAllTasks(): Retorna todas as tarefas como lista de DTOs
    // getTaskById(Long id): Busca tarefa por ID, retorna Optional<TaskDTO>
    // createTask(TaskDTO): Cria nova tarefa
    // updateTask(Long id, TaskDTO): Atualiza tarefa existente, retorna Optional<TaskDTO>
    // deleteTask(Long id): Remove tarefa, retorna boolean
    // toggleTaskCompletion(Long id): Alterna status, retorna Optional<TaskDTO>
}
```

### 5. Controller

Camada REST que expõe os endpoints:

```java
@RestController
@RequestMapping("/api/tasks")
@Tag(name = "Tasks", description = "Task management API")
public class TaskController {
    // Endpoints mapeados com @GetMapping, @PostMapping, @PutMapping, @PatchMapping, @DeleteMapping
}
```

### 6. Mapper

Converte entre `Task` (Entity) e `TaskDTO`:

```java
@Component
public class TaskMapper {
    public TaskDTO convertToDTO(Task task) { ... }
    public Task convertToEntity(TaskDTO taskDTO) { ... }
}
```

### 7. Exception Handler

Trata erros globalmente:

```java
@ControllerAdvice
public class GlobalExceptionHandler {

    // Trata erros de validação (@Valid) — retorna campo → mensagem
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationErrors(...) { ... }

    // Trata recurso não encontrado
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<Map<String, String>> handleNotFound(...) { ... }

    // Trata argumentos inválidos
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArgument(...) { ... }

    // Trata exceções genéricas
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleGenericError(...) { ... }
}
```

**Respostas de erro:**

- **400 Bad Request** — Validação: `{ "title": "Title is required" }`
- **400 Bad Request** — Argumento inválido: `{ "error": "Invalid request" }`
- **404 Not Found** — Recurso não encontrado: `{ "error": "Resource not found" }`
- **500 Internal Server Error** — Erro genérico: `{ "error": "Internal server error. Please try again." }`

## Documentação da API

A documentação interativa está disponível via Swagger UI:

- **Swagger UI**: `http://localhost:8080/swagger-ui/index.html`
- **OpenAPI JSON**: `http://localhost:8080/v3/api-docs`

## Configuração CORS

O CORS está configurado em `CorsConfig.java` para aceitar requisições dos frontends:

```java
@Configuration
public class CorsConfig {
    @Bean
    WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(@NonNull CorsRegistry registry) {
                registry.addMapping("/api/**")
                        .allowedOrigins(
                            "http://localhost:3000",
                            "http://localhost:5173",
                            "http://127.0.0.1:5173"
                        )
                        .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
                        .allowedHeaders("Content-Type", "Authorization", "Accept")
                        .allowCredentials(true)
                        .maxAge(3600);
            }
        };
    }
}
```

## Executando com Docker

```bash
# Iniciar todos os serviços (MySQL + Backend)
docker-compose up -d

# Ver logs
docker-compose logs -f backend

# Parar os serviços
docker-compose down
```

## Testes

```bash
# Executar todos os testes
./mvnw test

# Executar com relatório de cobertura
./mvnw test jacoco:report
```

## Tratamento de Erros HTTP

| Código | Significado |
|--------|-------------|
| 200 OK | Operação bem-sucedida |
| 201 Created | Recurso criado com sucesso |
| 204 No Content | Recurso deletado com sucesso |
| 400 Bad Request | Dados inválidos na requisição |
| 404 Not Found | Recurso não encontrado |
| 500 Internal Server Error | Erro interno do servidor |

## Exemplo de Uso

### Criar uma nova tarefa

```bash
curl -X POST http://localhost:8080/api/tasks \
  -H "Content-Type: application/json" \
  -d '{"title": "Estudar Spring Boot", "description": "Completar o tutorial", "completed": false}'
```

### Listar todas as tarefas

```bash
curl http://localhost:8080/api/tasks
```

### Atualizar uma tarefa

```bash
curl -X PUT http://localhost:8080/api/tasks/1 \
  -H "Content-Type: application/json" \
  -d '{"title": "Estudar Spring Boot - Atualizado", "description": "Completar o tutorial avançado"}'
```

### Alternar status de conclusão

```bash
curl -X PATCH http://localhost:8080/api/tasks/1/toggle
```

### Deletar uma tarefa

```bash
curl -X DELETE http://localhost:8080/api/tasks/1
```

---

**Última atualização:** 24 de abril de 2026
