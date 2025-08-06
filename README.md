# Guia Completo para Criação da API To-Do List

Este guia detalha o passo a passo para criar uma API RESTful utilizando Spring Boot, seguindo a estrutura do projeto implementado.

## 1. Criação do Projeto
- Acesse o [Spring Initializr](https://start.spring.io/).
- Selecione as opções:
  - Project: Maven Project
  - Language: Java
  - Spring Boot: 3.5.4
  - Group: com.todolist
  - Artifact: todolist-api
  - Name: todolist-api
  - Description: API RESTful para gerenciamento de tarefas (To-Do List)
  - Package name: com.todolist.api
  - Packaging: Jar
  - Java: 21

### Dependências do projeto

- **spring-boot-starter-actuator**  
  Monitoramento e métricas do Spring Boot.

- **spring-boot-starter-data-jpa**  
  Suporte a JPA para persistência de dados.

- **spring-boot-starter-web**  
  Criação de aplicações web (REST, MVC).

- **spring-boot-starter-validation**  
  Validação de dados com Bean Validation.

- **spring-boot-devtools**  
  Ferramentas para desenvolvimento (hot reload, etc).  
  `scope: runtime`, `optional: true`

- **mysql-connector-j**  
  Driver JDBC para conexão com banco de dados MySQL.  
  `scope: runtime`

- **spring-boot-starter-test**  
  Dependências para testes (JUnit, Mockito, etc).  
  `scope: test`

- **jakarta.validation-api**  
  API de validação Jakarta Bean Validation.

- **springdoc-openapi-starter-webmvc-ui** (versão 2.6.0)  
  Geração automática de documentação Swagger/OpenAPI para APIs Spring Boot.

- **springdoc-openapi-starter-common** (versão 2.6.0)  
  Dependência comum para SpringDoc OpenAPI.

## 2. Estrutura de Pastas Implementada
```
src/main/java/com/rev/revisao/
  controller/
    TaskController.java
  dto/
    TaskDTO.java
  mapper/
    TaskMapper.java
  model/
    Task.java
  repository/
    TaskRepository.java
  service/
    TaskService.java
src/main/resources/
  application.properties
```

## 3. Configuração do Banco de Dados
No arquivo `application.properties`:
```properties
spring.application.name=revisao

# Configuração do MySQL
spring.datasource.url=jdbc:mysql://localhost:3406/revisao_db?allowPublicKeyRetrieval=true&useSSL=false
spring.datasource.username=${MYSQL_USER:revisao_user}
spring.datasource.password=${MYSQL_PASSWORD:revisao_user_password}
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# JPA/Hibernate
spring.jpa.hibernate.ddl-auto=update
#Caso queira criar o banco do zero alterne para auto=create, lembre de voltar ao update para nao zerar o banco a cada execução
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect

# Configurações do SpringDoc OpenAPI
springdoc.api-docs.path=/api-docs
springdoc.swagger-ui.path=/swagger-ui.html
springdoc.swagger-ui.operationsSorter=method
```

## 4. Modelo (Entidade Task)
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

    // Constructors, getters e setters
}
```

## 5. DTO (Data Transfer Object)
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
    private Boolean completed;

    // Constructors, getters e setters
}
```

## 6. Mapper
Implementação de um mapper para conversão entre Entity e DTO:
```java
@Component
public class TaskMapper {
    public TaskDTO toDTO(Task task);
    public Task toEntity(TaskDTO taskDTO);
    public void updateEntityFromDTO(TaskDTO taskDTO, Task task);
}
```

## 7. Repository
```java
@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
}
```

## 8. Service
```java
@Service
public class TaskService {
    // Métodos implementados:
    // - getAllTasks()
    // - getTaskById(Long id)
    // - createTask(TaskDTO taskDTO)
    // - updateTask(Long id, TaskDTO taskDTO)
    // - deleteTask(Long id)
    // - toggleTaskCompletion(Long id)
}
```

## 9. Controller - Endpoints REST
```java
@RestController
@RequestMapping("/api/tasks")
public class TaskController {
    // GET /api/tasks - Listar todas as tarefas
    // GET /api/tasks/{id} - Buscar tarefa por ID
    // POST /api/tasks - Criar nova tarefa
    // PUT /api/tasks/{id} - Atualizar tarefa
    // DELETE /api/tasks/{id} - Deletar tarefa
    // PATCH /api/tasks/{id}/toggle - Alternar status da tarefa
}
```

### Endpoints Disponíveis:
- **GET** `/api/tasks` - Lista todas as tarefas
- **GET** `/api/tasks/{id}` - Busca tarefa específica
- **POST** `/api/tasks` - Cria nova tarefa
- **PUT** `/api/tasks/{id}` - Atualiza tarefa completa
- **DELETE** `/api/tasks/{id}` - Remove tarefa
- **PATCH** `/api/tasks/{id}/toggle` - Alterna status completed da tarefa

## 10. Docker e Containerização
O projeto inclui configuração Docker com `docker-compose.yml`:
- **MySQL**: Porta 3406 (container) → 3306 (host)
- **Backend**: Porta 8080
- Variáveis de ambiente configuradas para flexibilidade

## 11. Documentação da API
- **Swagger UI**: Disponível em `http://localhost:8080/swagger-ui.html`
- **OpenAPI Docs**: Disponível em `http://localhost:8080/api-docs`

## 12. Execução da Aplicação

### Opção 1: Docker Compose
```bash
docker-compose up -d
```

### Opção 2: Execução Local
```bash
# Windows
mvnw.cmd spring-boot:run

# Linux/Mac
./mvnw spring-boot:run
```

### Opção 3: IDE
Execute a classe principal da aplicação diretamente pela IDE.

## 13. Validações Implementadas
- **Title**: Obrigatório, máximo 100 caracteres
- **Description**: Opcional, máximo 500 caracteres
- **Completed**: Campo booleano com valor padrão false

## 14. Recursos Adicionais
- ✅ Validação de dados com Bean Validation
- ✅ Tratamento de erros com ResponseEntity
- ✅ Documentação automática com Swagger
- ✅ Mapper pattern para conversão Entity/DTO
- ✅ Configuração com variáveis de ambiente
- ✅ Containerização com Docker

## 15. Testando a API
Use os endpoints através de:
- **Postman** ou **Insomnia**
- **Swagger UI** (interface web)
- **cURL** ou ferramentas similares

Exemplo de requisição POST:
```json
{
  "title": "Estudar Spring Boot",
  "description": "Completar o tutorial de Spring Boot",
  "completed": false
}
```

---

**Base URL**: `http://localhost:8080/api/tasks`  
**Documentação**: `http://localhost:8080/swagger-ui.html`
