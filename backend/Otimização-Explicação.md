# Otimização com Lombok - To-Do List API

## 🚀 Dependência Lombok

Primeiro, adicione a dependência do Lombok no `pom.xml`:

```xml
<dependencies>
    <!-- Dependências existentes... -->
    
    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
        <scope>provided</scope>
    </dependency>
    
    <!-- Outras dependências... -->
</dependencies>
```

## Otimizações por Classe

### 1. Modelo Task - Entidade JPA

```java
package com.rev.revisao.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity // JPA: Marca a classe como uma entidade de banco de dados
@Table(name = "tasks") // JPA: Define o nome da tabela no banco
@Data // Lombok: Gera getters, setters, toString, equals, hashCode automaticamente
@NoArgsConstructor // Lombok: Gera construtor sem parâmetros (obrigatório para JPA)
@AllArgsConstructor // Lombok: Gera construtor com todos os parâmetros da classe
public class Task {
    
    @Id // JPA: Marca este campo como chave primária
    @GeneratedValue(strategy = GenerationType.IDENTITY) // JPA: Auto-incremento no banco
    private Long id;

    @Column(nullable = false) // JPA: Campo obrigatório no banco de dados
    private String title;

    @Column // JPA: Campo opcional no banco de dados
    private String description;

    @Column(nullable = false) // JPA: Campo obrigatório no banco de dados
    private Boolean completed = false;

    // Construtor personalizado para criação sem ID
    public Task(String title, String description) {
        this.title = title;
        this.description = description;
        this.completed = false;
    }
}
```

### 2. TaskDTO - Data Transfer Object

```java
package com.rev.revisao.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data // Lombok: Gera getters, setters, toString, equals, hashCode
@NoArgsConstructor // Lombok: Construtor vazio para deserialização JSON
@AllArgsConstructor // Lombok: Construtor com todos os campos para facilitar testes
public class TaskDTO {
    
    @JsonProperty("id") // Jackson: Define nome do campo no JSON
    private Long id;

    @NotBlank(message = "Title is required") // Validation: Campo não pode ser vazio
    @Size(max = 100, message = "Title must be less than 100 characters") // Validation: Tamanho máximo
    @JsonProperty("title") // Jackson: Define nome do campo no JSON
    private String title;

    @Size(max = 500, message = "Description must be less than 500 characters") // Validation: Tamanho máximo
    @JsonProperty("description") // Jackson: Define nome do campo no JSON
    private String description;

    @JsonProperty("completed") // Jackson: Define nome do campo no JSON
    private Boolean completed;
}
```

### 3. TaskService - Camada de Serviço

```java
package com.rev.revisao.service;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

@Service // Spring: Marca como componente de serviço para injeção de dependência
@RequiredArgsConstructor // Lombok: Gera construtor apenas para campos 'final' não inicializados
public class TaskService {
    
    // 'final' indica que estas dependências são obrigatórias e imutáveis
    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;

    // O @RequiredArgsConstructor gera automaticamente este construtor:
    // public TaskService(TaskRepository taskRepository, TaskMapper taskMapper) {
    //     this.taskRepository = taskRepository;
    //     this.taskMapper = taskMapper;
    // }
    
    // Métodos do serviço permanecem iguais...
}
```

### 4. TaskController - Camada de Controle

```java
package com.rev.revisao.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController // Spring: Combina @Controller + @ResponseBody para APIs REST
@RequestMapping("/api/tasks") // Spring: Define prefixo da URL para todos os endpoints
@Tag(name = "Tasks", description = "API para gerenciamento de tarefas") // Swagger: Documentação da API
@RequiredArgsConstructor // Lombok: Construtor para campos 'final' (injeção de dependência)
public class TaskController {

    private final TaskService taskService; // Dependência injetada automaticamente

    // O @RequiredArgsConstructor gera automaticamente:
    // public TaskController(TaskService taskService) {
    //     this.taskService = taskService;
    // }

    // Todos os endpoints permanecem iguais...
}
```

## Sobre as Anotações 

### @Data
- **Gera automaticamente:**
  - `@Getter` - Métodos getter para todos os campos
  - `@Setter` - Métodos setter para campos não-final
  - `@ToString` - Método toString() com todos os campos
  - `@EqualsAndHashCode` - Métodos equals() e hashCode()
  - `@RequiredArgsConstructor` - Construtor para campos final não inicializados

### @NoArgsConstructor
- Gera construtor público sem parâmetros: `public ClassName() {}`
- **Essencial para JPA entities** (Hibernate precisa instanciar objetos)
- **Resolve problemas de desserialização JSON** (Jackson precisa de construtor vazio)

### @AllArgsConstructor
- Gera construtor com todos os campos como parâmetros
- **Útil para testes** e criação de objetos completos
- **Ordem dos parâmetros** segue a ordem de declaração dos campos

### @RequiredArgsConstructor
- Gera construtor **apenas para campos `final`** não inicializados
- **Perfeito para injeção de dependências** no Spring
- **Elimina a necessidade de `@Autowired`** em construtores
- **Torna as dependências explicitamente obrigatórias**

## Anotações Adicionais Úteis

### Para casos específicos:
- `@Getter` - Apenas getters (sem setters)
- `@Setter` - Apenas setters (sem getters)  
- `@ToString` - Apenas método toString()
- `@EqualsAndHashCode` - Apenas equals() e hashCode()
- `@Builder` - Padrão Builder para criação de objetos
- `@Value` - Classe imutável (todos os campos final)

