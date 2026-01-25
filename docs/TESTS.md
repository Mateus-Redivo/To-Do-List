# Guia de Testes - Aplicação To Do List

## Objetivo deste Guia

Este documento explica a estrutura de testes da aplicação To Do List, servindo como material didático para ensinar conceitos de testes unitários e de integração em Spring Boot.

---

## Índice

1. [Conceitos Fundamentais](#conceitos-fundamentais)
2. [Estrutura de Testes](#estrutura-de-testes)
3. [Tipos de Testes](#tipos-de-testes)
4. [Anotações Importantes](#anotações-importantes)
5. [Padrão AAA](#padrão-aaa)
6. [Mockito - Framework de Mocks](#mockito---framework-de-mocks)
7. [Como Executar os Testes](#como-executar-os-testes)

---

## Conceitos Fundamentais

### O que são Testes Automatizados?

Testes automatizados são códigos que verificam se o código de produção funciona corretamente. Eles trazem diversos benefícios:

- Previnem bugs antes que cheguem à produção
- Facilitam refatoração do código com segurança
- Documentam o comportamento esperado do sistema
- Aumentam a confiança ao fazer mudanças

### Pirâmide de Testes

```plaintext
        /\
       /  \      Testes E2E (poucos)
      /    \     - Testam a aplicação completa
     /------\    
    /        \   Testes de Integração (médio)
   /          \  - Testam múltiplas camadas juntas
  /------------\
 /              \ Testes Unitários (muitos)
/________________\ - Testam componentes isolados
```

Nossa aplicação foca em **testes unitários** e **testes de integração**.

---

## Estrutura de Testes

```plaintext
src/test/java/com/rev/revisao/
├── controller/
│   └── TaskControllerTest.java      # Testa endpoints HTTP
├── service/
│   └── TaskServiceTest.java         # Testa lógica de negócio
├── repository/
│   └── TaskRepositoryTest.java      # Testa acesso ao banco
├── mapper/
│   └── TaskMapperTest.java          # Testa conversões Entity↔DTO
├── model/
│   └── TaskTest.java                # Testa entidade
└── dto/
    └── TaskDTOTest.java             # Testa DTO
```

### Espelhamento da Estrutura

A estrutura de testes **espelha** a estrutura do código de produção:

- `src/main/java/...` → Código de produção
- `src/test/java/...` → Código de testes

---

## Tipos de Testes

### 1. Testes Unitários

**Objetivo:** Testar um único componente isoladamente

**Características:**

- Rápidos (milissegundos)
- Isolados (usam mocks)
- Não acessam banco de dados real
- Não fazem requisições HTTP reais

**Exemplos na aplicação:**

- `TaskServiceTest` - testa lógica de negócio
- `TaskMapperTest` - testa conversões
- `TaskDTOTest` - testa objetos de transferência

### 2. Testes de Integração

**Objetivo:** Testar integração entre componentes

**Características:**

- Mais lentos que unitários
- Usam componentes reais
- Podem usar banco H2 em memória
- Testam fluxo completo

**Exemplos na aplicação:**

- `TaskRepositoryTest` - testa com banco H2 real
- `TaskControllerTest` - testa requisições HTTP simuladas

---

## Anotações Importantes

### Anotações de Teste (JUnit 5)

| Anotação | Uso | Exemplo |
| -------- | --- | ------- |
| `@Test` | Marca um método como teste | `@Test void testSomething()` |
| `@BeforeEach` | Executa antes de cada teste | `@BeforeEach void setUp()` |
| `@AfterEach` | Executa depois de cada teste | `@AfterEach void tearDown()` |
| `@DisplayName` | Nome legível do teste | `@DisplayName("Deve criar tarefa")` |

### Anotações do Mockito

| Anotação | Uso | Exemplo |
| -------- | --- | ------- |
| `@ExtendWith(MockitoExtension.class)` | Habilita Mockito | Na classe de teste |
| `@Mock` | Cria um mock (objeto simulado) | `@Mock TaskRepository repo` |
| `@InjectMocks` | Injeta mocks na classe testada | `@InjectMocks TaskService service` |

### Anotações do Spring Boot

| Anotação | Uso | Quando usar |
| -------- | --- | ----------- |
| `@DataJpaTest` | Testa repositories | `TaskRepositoryTest` |
| `@WebMvcTest` | Testa controllers | Alternativa ao MockMvc |
| `@SpringBootTest` | Carrega contexto completo | Testes E2E |

---

## Padrão AAA (Arrange-Act-Assert)

Todos os testes seguem este padrão:

```java
@Test
void testExample() {
    // ARRANGE (Preparar)
    // Configura o cenário do teste
    TaskDTO taskDTO = new TaskDTO(1L, "Teste", "Descrição", false);
    when(repository.findById(1L)).thenReturn(Optional.of(task));
    
    // ACT (Agir)
    // Executa o código que está sendo testado
    Optional<TaskDTO> result = service.getTaskById(1L);
    
    // ASSERT (Verificar)
    // Verifica se o resultado é o esperado
    assertTrue(result.isPresent());
    assertEquals("Teste", result.get().getTitle());
    verify(repository, times(1)).findById(1L);
}
```

### Explicação de cada fase

1. **ARRANGE**: Prepara tudo que o teste precisa
   - Cria objetos
   - Configura mocks
   - Define comportamentos esperados

2. **ACT**: Executa a ação sendo testada
   - Chama o método que queremos testar
   - Deve ser **uma única linha** idealmente

3. **ASSERT**: Verifica os resultados
   - Usa assertions (`assertEquals`, `assertTrue`, etc.)
   - Verifica chamadas de métodos (`verify`)

---

## Mockito - Framework de Mocks

### O que é um Mock?

Um **mock** é uma versão simulada de um objeto real. Usamos mocks para:

- Isolar o código sendo testado
- Simular comportamentos
- Verificar interações

### Principais Métodos do Mockito

#### 1. `when().thenReturn()`

Configura o comportamento do mock:

```java
// Quando chamar repository.findById(1L), retorne Optional.of(task)
when(repository.findById(1L)).thenReturn(Optional.of(task));
```

#### 2. `verify()`

Verifica se um método foi chamado:

```java
// Verifica se findById foi chamado exatamente 1 vez
verify(repository, times(1)).findById(1L);

// Verifica se NÃO foi chamado
verify(repository, never()).deleteById(any());
```

#### 3. `any()`

Matcher que aceita qualquer valor:

```java
// Aceita qualquer TaskDTO
when(service.createTask(any(TaskDTO.class))).thenReturn(taskDTO);
```

#### 4. `eq()`

Matcher que verifica igualdade exata:

```java
// ID deve ser exatamente 1L, mas aceita qualquer TaskDTO
when(service.updateTask(eq(1L), any(TaskDTO.class))).thenReturn(Optional.of(taskDTO));
```

### Exemplo Completo

```java
@ExtendWith(MockitoExtension.class)
class TaskServiceTest {
    
    @Mock  // Cria mock do repository
    private TaskRepository taskRepository;
    
    @InjectMocks  // Injeta o mock no service
    private TaskService taskService;
    
    @Test
    void testGetTaskById() {
        // ARRANGE: Configura comportamento do mock
        Task task = new Task("Teste", "Descrição");
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        
        // ACT: Chama o método real do service
        Optional<TaskDTO> result = taskService.getTaskById(1L);
        
        // ASSERT: Verifica resultados e interações
        assertTrue(result.isPresent());
        verify(taskRepository, times(1)).findById(1L);
    }
}
```

---

## Assertions Comuns

### JUnit Assertions

```java
// Igualdade
assertEquals(esperado, real);
assertEquals(1L, task.getId());

// Verdadeiro/Falso
assertTrue(condition);
assertFalse(condition);

// Nulo/Não-nulo
assertNull(object);
assertNotNull(object);

// Arrays/Coleções
assertArrayEquals(esperado, real);
assertEquals(3, lista.size());

// Exceções
assertThrows(ExceptionType.class, () -> metodo());
```

---

## Testes por Camada

### 1. Controller Tests (`TaskControllerTest`)

**O que testa:**

- Endpoints HTTP (GET, POST, PUT, DELETE, PATCH)
- Status codes (200, 404, etc.)
- Formato do JSON retornado
- Validações de entrada

**Ferramentas:**

- `MockMvc` - Simula requisições HTTP
- `@Mock` - Simula o Service
- `ObjectMapper` - Converte JSON

**Exemplo:**

```java
mockMvc.perform(get("/api/tasks/1"))
    .andExpect(status().isOk())
    .andExpect(jsonPath("$.title").value("Test Task"));
```

### 2. Service Tests (`TaskServiceTest`)

**O que testa:**

- Lógica de negócio
- Interação com Repository
- Conversões via Mapper
- Tratamento de Optional

**Ferramentas:**

- `@Mock` - Simula Repository e Mapper
- `@InjectMocks` - Cria Service real

**Exemplo:**

```java
when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
Optional<TaskDTO> result = taskService.getTaskById(1L);
verify(taskRepository, times(1)).findById(1L);
```

### 3. Repository Tests (`TaskRepositoryTest`)

**O que testa:**

- Operações CRUD no banco
- Queries personalizadas
- Integridade dos dados

**Ferramentas:**

- `@DataJpaTest` - Configura banco H2 em memória
- `@Autowired` - Injeta repository REAL (não mock)

**Exemplo:**

```java
Task savedTask = taskRepository.save(task);
assertNotNull(savedTask.getId());
```

### 4. Mapper Tests (`TaskMapperTest`)

**O que testa:**

- Conversão Entity → DTO
- Conversão DTO → Entity
- Preservação de dados

**Sem mocks:**

```java
TaskMapper mapper = new TaskMapper();
TaskDTO dto = mapper.convertToDTO(task);
assertEquals(task.getTitle(), dto.getTitle());
```

### 5. DTO/Model Tests

**O que testa:**

- Construtores
- Getters/Setters
- Valores padrão

**Testes simples:**

```java
TaskDTO dto = new TaskDTO();
assertEquals("", dto.getTitle());
assertFalse(dto.getCompleted());
```

---

## Como Executar os Testes

### Pelo Maven (linha de comando)

```bash
# Executar todos os testes
mvn test

# Executar testes de uma classe específica
mvn test -Dtest=TaskControllerTest

# Executar um teste específico
mvn test -Dtest=TaskControllerTest#testGetAllTasks

# Ver relatório de cobertura
mvn test jacoco:report
```

### Pelo IDE (VS Code, IntelliJ)

1. **Executar todos os testes:**
   - Clique direito na pasta `test`
   - Selecione "Run Tests"

2. **Executar testes de uma classe:**
   - Clique direito no arquivo `...Test.java`
   - Selecione "Run Tests"

3. **Executar um teste específico:**
   - Clique no ícone > ao lado do método `@Test`

---

## Cobertura de Testes

### O que é Cobertura?

Cobertura mede quantas linhas/métodos do código foram executados pelos testes.

**Meta ideal:** 80% ou mais de cobertura

### Tipos de Cobertura

- **Line Coverage**: % de linhas executadas
- **Branch Coverage**: % de condicionais (if/else) testadas
- **Method Coverage**: % de métodos executados

---

## Boas Práticas

### 1. Nomenclatura de Testes

```java
// Ruim
@Test void test1() { }

// Bom
@Test void testGetTaskById_ShouldReturnTask_WhenTaskExists() { }

// Alternativa
@Test 
@DisplayName("Deve retornar tarefa quando ela existe")
void testGetTaskById() { }
```

### 2. Um Conceito por Teste

```java
// Ruim - testa muitas coisas
@Test void testTaskOperations() {
    // cria tarefa
    // atualiza tarefa
    // deleta tarefa
}

// Bom - testes separados
@Test void testCreateTask() { }
@Test void testUpdateTask() { }
@Test void testDeleteTask() { }
```

### 3. Testes Independentes

```java
// Ruim - testes dependem um do outro
@Test void test1() { savedId = save(); }
@Test void test2() { find(savedId); }  // Depende de test1

// Bom - cada teste prepara seu próprio cenário
@Test void test1() { 
    Long id = save();
    find(id);
}
```

### 4. Testes Legíveis

```java
// Use constantes descritivas
private static final Long EXISTING_TASK_ID = 1L;
private static final Long NON_EXISTING_TASK_ID = 999L;

@Test
void testGetTaskByIdNotFound() {
    when(repository.findById(NON_EXISTING_TASK_ID))
        .thenReturn(Optional.empty());
}
```

### 5. Teste Cenários Positivos E Negativos

```java
@Test void testGetTaskById_Success() { }      // Cenário feliz
@Test void testGetTaskById_NotFound() { }     // Cenário de erro
@Test void testCreateTask_WithValidData() { } // Dados válidos
@Test void testCreateTask_WithNullTitle() { } // Dados inválidos
```

---

## Debugging de Testes

### Quando um teste falha

1. **Leia a mensagem de erro** completa
2. **Identifique a linha** que falhou
3. **Verifique o ASSERT** - o que era esperado vs o que foi retornado
4. **Adicione prints** temporários:

   ```java
   System.out.println("Result: " + result);
   System.out.println("Expected: " + expected);
   ```

5. **Use debugger** - coloque breakpoint e execute em modo debug

---

## Recursos Adicionais

### Documentação Oficial

- [JUnit 5](https://junit.org/junit5/docs/current/user-guide/)
- [Mockito](https://javadoc.io/doc/org.mockito/mockito-core/latest/org/mockito/Mockito.html)
- [Spring Boot Testing](https://docs.spring.io/spring-boot/docs/current/reference/html/features.html#features.testing)

### Conceitos para Aprofundar

- TDD (Test-Driven Development)
- BDD (Behavior-Driven Development)
- Test Doubles (Mocks, Stubs, Fakes, Spies)
- Mutation Testing
- Contract Testing

---

## Exercícios Propostos

### Nível Iniciante

1. Adicione um teste que verifica se `getTaskById` lança exceção para ID nulo
2. Crie teste para verificar se `createTask` não aceita título vazio
3. Teste se `deleteTask` retorna false para ID negativo

### Nível Intermediário

1. Implemente testes de validação (@Valid) no controller
2. Adicione teste de paginação na listagem de tarefas
3. Crie testes para buscar tarefas filtradas por status (completed/pending)

### Nível Avançado

1. Implemente testes de performance (verificar tempo de execução)
2. Adicione testes de concorrência (múltiplas threads)
3. Crie testes de integração completos com TestContainers

---

## Contribuindo

Ao adicionar novos testes, lembre-se de:

1. Seguir o padrão AAA
2. Adicionar comentários explicativos
3. Testar cenários positivos e negativos
4. Manter testes independentes
5. Atualizar esta documentação se necessário

---

**Criado para fins educacionais**
**Última atualização:** Janeiro 2026
