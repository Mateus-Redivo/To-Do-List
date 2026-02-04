# API To-Do List - Backend

Esta é a API REST do projeto de gerenciamento de tarefas desenvolvida com Spring Boot.

## Tecnologias Utilizadas

- **Java 21**: Linguagem de programação principal
- **Spring Boot 3.5.4**: Framework para criação de aplicações Java
- **Spring Data JPA**: Abstração para acesso a dados e persistência
- **MySQL**: Banco de dados relacional
- **Maven**: Gerenciador de dependências e build
- **Hibernate**: ORM para mapeamento objeto-relacional
- **SpringDoc OpenAPI**: Documentação automática da API (Swagger)
- **Bean Validation**: Validação de dados com anotações

## Como Começar

### Requisitos do Sistema

Antes de iniciar o desenvolvimento, você precisa ter instalado em sua máquina:

- Java 21 ou superior
- Maven 3.6 ou superior
- MySQL 8.0 ou superior (ou via Docker)
- Git para controle de versão

### Configuração Inicial

**Configurar o Banco de Dados:**

Edite o arquivo `src/main/resources/application.properties` conforme necessário:

```properties
# Configuração do MySQL
spring.datasource.url=jdbc:mysql://localhost:3406/revisao_db?allowPublicKeyRetrieval=true&useSSL=false
spring.datasource.username=${MYSQL_USER:revisao_user}
spring.datasource.password=${MYSQL_PASSWORD:revisao_user_password}
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# JPA/Hibernate
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect

# Configurações do SpringDoc OpenAPI
springdoc.api-docs.path=/api-docs
springdoc.swagger-ui.path=/swagger-ui.html
springdoc.swagger-ui.operationsSorter=method
```

> **Nota**: Use variáveis de ambiente `MYSQL_USER` e `MYSQL_PASSWORD` para maior segurança.

## Comandos Principais

Durante o desenvolvimento, você usará principalmente estes comandos:

```bash
# Windows - Iniciar o servidor de desenvolvimento
mvnw.cmd spring-boot:run

# Linux/Mac - Iniciar o servidor de desenvolvimento
./mvnw spring-boot:run

# Executar testes
mvnw.cmd test

# Criar build para produção
mvnw.cmd clean package

# Executar o JAR gerado
java -jar target/revisao-0.0.1-SNAPSHOT.jar
```

O servidor ficará disponível em `http://localhost:8080`.

## Arquitetura da API

### Estrutura em Camadas

A API segue o padrão de arquitetura em camadas:

```plaintext
src/main/java/com/rev/revisao/
├── controller/       # Camada de apresentação (REST endpoints)
├── service/          # Camada de lógica de negócio
├── repository/       # Camada de acesso a dados
├── model/            # Entidades do banco de dados
├── dto/              # Data Transfer Objects
├── mapper/           # Conversão entre Entity e DTO
└── config/           # Configurações (CORS, etc)
```

### Endpoints da API

A API fornece os seguintes endpoints RESTful:

- **GET** `/api/tasks` - Lista todas as tarefas
- **GET** `/api/tasks/{id}` - Busca uma tarefa específica por ID
- **POST** `/api/tasks` - Cria uma nova tarefa
- **PUT** `/api/tasks/{id}` - Atualiza completamente uma tarefa (título e descrição)
- **PATCH** `/api/tasks/{id}/toggle` - Alterna o status de conclusão da tarefa
- **DELETE** `/api/tasks/{id}` - Remove uma tarefa

### Modelo de Dados

A entidade principal `Task` possui os seguintes campos:

```json
{
  "id": 1,
  "title": "Estudar Spring Boot",
  "description": "Completar o tutorial de Spring Boot",
  "completed": false,
  "createdAt": "2024-01-15T10:30:00Z",
  "updatedAt": "2024-01-15T10:30:00Z"
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
    private Boolean completed = false;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
```

### 2. DTO (Data Transfer Object)

O `TaskDTO.java` é usado para transferência de dados entre camadas:

```java
public class TaskDTO {
    private Long id;
    
    @NotBlank(message = "Title is required")
    @Size(max = 100)
    private String title;
    
    @Size(max = 500)
    private String description;
    
    private Boolean completed;
    
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
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
    // getAllTasks(): Retorna todas as tarefas
    // getTaskById(Long id): Busca tarefa por ID
    // createTask(TaskDTO): Cria nova tarefa
    // updateTask(Long id, TaskDTO): Atualiza tarefa existente
    // deleteTask(Long id): Remove tarefa
    // toggleTaskCompletion(Long id): Alterna status
}
```

### 5. Controller

Camada REST que expõe os endpoints:

```java
@RestController
@RequestMapping("/api/tasks")
@Tag(name = "Tasks", description = "API para gerenciamento de tarefas")
public class TaskController {
    // Endpoints mapeados com @GetMapping, @PostMapping, etc.
}
```

### 6. Mapper

Converte entre Entity e DTO:

```java
@Component
public class TaskMapper {
    public TaskDTO toDTO(Task task);
    public Task toEntity(TaskDTO taskDTO);
    public void updateEntityFromDTO(TaskDTO taskDTO, Task task);
}
```

### 7. Exception Handler

Trata erros globalmente e retorna mensagens apropriadas ao frontend:

```java
@ControllerAdvice
public class GlobalExceptionHandler {
    
    // Trata erros de validação (@Valid)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationErrors(
        MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String errorMessage = error.getDefaultMessage();
            errors.put("error", errorMessage);
        });
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }
    
    // Trata exceções genéricas
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleGenericError(Exception ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", "Erro interno no servidor. Tente novamente.");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}
```

**Respostas de erro padronizadas:**

- **400 Bad Request**: Retorna mensagem de validação específica (ex: "Title is required")
- **404 Not Found**: Recurso não encontrado
- **500 Internal Server Error**: Erro genérico do servidor

Exemplo de resposta de erro:

```json
{
  "error": "Title is required"
}
```

## Documentação da API

A documentação interativa está disponível através do Swagger UI:

- **Swagger UI**: `http://localhost:8080/swagger-ui.html`
- **OpenAPI JSON**: `http://localhost:8080/api-docs`

A documentação permite:

- Visualizar todos os endpoints disponíveis
- Testar requisições diretamente no navegador
- Ver os schemas de requisição e resposta
- Consultar códigos de status HTTP

## Configuração CORS

Para permitir requisições do frontend, o CORS está configurado em `CorsConfig.java`:

```java
@Configuration
@EnableWebMvc
public class CorsConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins("http://localhost:3000", "http://localhost:5173")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}
```

## Executando com Docker

### Usando Docker Compose

O projeto inclui configuração Docker Compose na raiz do projeto:

```bash
# Iniciar todos os serviços (MySQL + Backend)
docker-compose up -d

# Parar os serviços
docker-compose down

# Ver logs
docker-compose logs -f backend
```

### Build Manual do Container

```bash
# Construir a imagem
docker build -t todolist-backend .

# Executar o container
docker run -p 8080:8080 --name backend todolist-backend
```

## Testes

Execute os testes unitários e de integração:

```bash
# Executar todos os testes
mvnw.cmd test

# Executar com relatório de cobertura
mvnw.cmd test jacoco:report
```

## Tratamento de Erros

A API retorna respostas HTTP apropriadas:

- **200 OK**: Operação bem-sucedida
- **201 Created**: Recurso criado com sucesso
- **400 Bad Request**: Dados inválidos na requisição
- **404 Not Found**: Recurso não encontrado
- **500 Internal Server Error**: Erro interno do servidor

## Exemplo de Uso

### Criar uma nova tarefa

```bash
curl -X POST http://localhost:8080/api/tasks \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Estudar Spring Boot",
    "description": "Completar o tutorial",
    "completed": false
  }'
```

### Listar todas as tarefas

```bash
curl http://localhost:8080/api/tasks
```

### Atualizar uma tarefa

```bash
curl -X PUT http://localhost:8080/api/tasks/1 \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Estudar Spring Boot - Atualizado",
    "description": "Completar o tutorial avançado"
  }'
```

### Alternar status de conclusão

```bash
curl -X PATCH http://localhost:8080/api/tasks/1/toggle
```

### Deletar uma tarefa

```bash
curl -X DELETE http://localhost:8080/api/tasks/1
```

## Recursos Adicionais

- Validação automática com Bean Validation
- Documentação automática com Swagger/OpenAPI
- Auditoria de timestamps (createdAt, updatedAt)
- Mapeamento DTO/Entity com Mapper pattern
- Tratamento de exceções personalizado
- Configuração CORS para frontend
- Suporte a variáveis de ambiente
- Docker e Docker Compose
- Hot reload com DevTools
