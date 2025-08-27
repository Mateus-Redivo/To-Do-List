# Guia Completo para Criação da API

Este guia detalha o passo a passo para criar uma API RESTful utilizando Spring Boot, seguindo a estrutura do projeto fornecido.

## 1. Criação do Projeto
- Acesse o [Spring Initializr](https://start.spring.io/).
- Selecione as opções:
  - Project: Maven Project
  - Language: Java
  - Spring Boot: versão estável mais recente
  - Group: com.rev
  - Artifact: revisao
  - Name: revisao
  - Packaging: Jar
  - Java: 17 ou superior

### Dependências do projeto

- spring-boot-starter-actuator
  Monitoramento e métricas do Spring Boot.

- spring-boot-starter-data-jpa 
  Suporte a JPA para persistência de dados.

- spring-boot-starter-web 
  Criação de aplicações web (REST, MVC).

- spring-boot-starter-validation 
  Validação de dados com Bean Validation.

- spring-boot-devtools 
  Ferramentas para desenvolvimento (hot reload, etc).  
  `scope: runtime`, `optional: true`

- mysql-connector-j 
  Driver JDBC para conexão com banco de dados MySQL.  
  `scope: runtime`

- spring-boot-starter-test 
  Dependências para testes (JUnit, Mockito, etc).  
  `scope: test`

- jakarta.validation-api 
  API de validação Jakarta Bean Validation.  
  `version: 3.0.2`

- springdoc-openapi-starter-webmvc-ui  
  Geração automática de documentação Swagger/OpenAPI para APIs Spring Boot.  
  `version: 2.5.0`

## 2. Estrutura de Pastas
Organize o projeto conforme o padrão abaixo:
```
src/main/java/com/rev/revisao/
  controller/
  dto/
  model/
  repository/
  service/
src/main/resources/
  application.properties
```

## 3. Configuração do Banco de Dados
No arquivo `src/main/resources/application.properties`, configure:
```properties
spring.datasource.url=jdbc:mysql://localhost:`sua porta escolhida`/nome_do_banco
spring.datasource.username=seu_usuario
spring.datasource.password=sua_senha
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.jpa.hibernate.ddl-auto=update
spring.jpa.database-platform=org.hibernate.dialect.MySQLDialect
```
Altere `nome_do_banco`, `seu_usuario` e `sua_senha` conforme sua configuração local.

## 4. Criação do Modelo (Entidade)
No pacote `model`, crie a classe `Task.java`:
```java
@Entity
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String description;
    private boolean completed;
    // getters e setters
}
```

## 5. Criação do DTO
No pacote `dto`, crie a classe `TaskDTO.java` para transferir dados:
```java
public class TaskDTO {
    private Long id;
    private String title;
    private String description;
    private boolean completed;
    // getters e setters
}
```

## 6. Criação do Repository
No pacote `repository`, crie a interface:
```java
public interface TaskRepository extends JpaRepository<Task, Long> {}
```

## 7. Criação do Service
No pacote `service`, crie a classe `TaskService.java`:
```java
@Service
public class TaskService {
    @Autowired
    private TaskRepository repository;
    // Métodos para CRUD (create, read, update, delete)
}
```

## 8. Criação do Controller
No pacote `controller`, crie a classe `TaskController.java`:
```java
@RestController
@RequestMapping("/tasks")
public class TaskController {
    @Autowired
    private TaskService service;
    // Endpoints REST (GET, POST, PUT, DELETE)
}
```

## 9. Testes
- Crie testes unitários e de integração em `src/test/java/com/rev/revisao/`.
- Utilize JUnit e Mockito para os testes.

## 10. Documentação e Testes de API
- Utilize o Postman para testar os endpoints.
- (Opcional) Adicione Swagger para documentação automática:
  - Adicione a dependência `springdoc-openapi-ui` no `pom.xml`.
  - Acesse `/swagger-ui.html` após rodar a aplicação.

## 11. Build e Execução
- Para rodar a aplicação, utilize:
  - No terminal: `./mvnw spring-boot:run` (Linux/Mac) ou `mvnw.cmd spring-boot:run` (Windows)
- Acesse a API em `http://localhost:8080/tasks`.

## 12. Dicas Finais
- Mantenha o código organizado e siga boas práticas de programação.
- Utilize DTOs para evitar expor entidades diretamente.
- Implemente tratamento de exceções para respostas mais amigáveis.

---

Se precisar de exemplos de código para algum dos passos, consulte as pastas do projeto ou solicite exemplos específicos.
