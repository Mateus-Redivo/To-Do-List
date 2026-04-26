# Guia de Testes - Aplicação To Do List

## Objetivo deste Guia

Este documento explica a estrutura de testes da aplicação To Do List, servindo como material didático para ensinar conceitos de testes unitários e de integração em Spring Boot.

---

## Índice

1. [Conceitos Fundamentais](#conceitos-fundamentais)
2. [Estrutura de Testes](#estrutura-de-testes)
3. [Tipos de Testes](#tipos-de-testes)
4. [Anotações Importantes](#anotações-importantes)
5. [Mockito - Framework de Mocks](#mockito---framework-de-mocks)
6. [Como Executar os Testes](#como-executar-os-testes)

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
src/test/java/com/todolist/api/
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

- `TaskServiceTest` — testa lógica de negócio
- `TaskMapperTest` — testa conversões
- `TaskDTOTest` — testa objetos de transferência

### 2. Testes de Integração

**Objetivo:** Testar integração entre componentes

**Características:**

- Mais lentos que unitários
- Usam componentes reais
- Podem usar banco H2 em memória
- Testam fluxo completo

**Exemplos na aplicação:**

- `TaskRepositoryTest` — testa com banco H2 real
- `TaskControllerTest` — testa requisições HTTP simuladas via `MockMvc`

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
| `@WebMvcTest` | Testa controllers | Alternativa ao MockMvc standalone |
| `@SpringBootTest` | Carrega contexto completo | Testes E2E |

---

## Padrão AAA (Arrange-Act-Assert)

Todos os testes seguem este padrão:

```java
@Test
void testExample() {
    // ARRANGE (Preparar)
    TaskDTO taskDTO = new TaskDTO(1L, "Teste", "Descrição", false);
    when(repository.findById(1L)).thenReturn(Optional.of(task));
    
    // ACT (Agir)
    Optional<TaskDTO> result = service.getTaskById(1L);
    
    // ASSERT (Verificar)
    assertTrue(result.isPresent());
    assertEquals("Teste", result.get().getTitle());
    verify(repository, times(1)).findById(1L);
}
```

### Explicação de cada fase

1. **ARRANGE**: Prepara tudo que o teste precisa — cria objetos, configura mocks, define comportamentos esperados
2. **ACT**: Executa a ação sendo testada — deve ser **uma única linha** idealmente
3. **ASSERT**: Verifica os resultados com assertions e verifica chamadas de métodos

---

## Mockito - Framework de Mocks

### O que é um Mock?

Um **mock** é uma versão simulada de um objeto real. Usamos mocks para isolar o código sendo testado, simular comportamentos e verificar interações.

### Principais Métodos do Mockito

#### `when().thenReturn()`

```java
when(repository.findById(1L)).thenReturn(Optional.of(task));
```

#### `verify()`

```java
verify(repository, times(1)).findById(1L);
verify(repository, never()).deleteById(any());
```

#### `any()` e `eq()`

```java
when(service.createTask(any(TaskDTO.class))).thenReturn(taskDTO);
when(service.updateTask(eq(1L), any(TaskDTO.class))).thenReturn(Optional.of(taskDTO));
```

### Exemplo Completo

```java
@ExtendWith(MockitoExtension.class)
class TaskServiceTest {
    
    @Mock
    private TaskRepository taskRepository;
    
    @Mock
    private TaskMapper taskMapper;
    
    @InjectMocks
    private TaskService taskService;
    
    @Test
    void testGetTaskById() {
        // ARRANGE
        Task task = new Task("Teste", "Descrição");
        TaskDTO dto = new TaskDTO(1L, "Teste", "Descrição", false);
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(taskMapper.convertToDTO(task)).thenReturn(dto);
        
        // ACT
        Optional<TaskDTO> result = taskService.getTaskById(1L);
        
        // ASSERT
        assertTrue(result.isPresent());
        assertEquals("Teste", result.get().getTitle());
        verify(taskRepository, times(1)).findById(1L);
    }
}
```

---

## Assertions Comuns

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

// Coleções
assertEquals(3, lista.size());

// Exceções
assertThrows(ExceptionType.class, () -> metodo());
```

---

## Testes por Camada

### 1. Controller Tests (`TaskControllerTest`)

**O que testa:** Endpoints HTTP, status codes, formato JSON, validações de entrada

**Ferramentas:** `MockMvc`, `@Mock TaskService`, `ObjectMapper`

```java
@ExtendWith(MockitoExtension.class)
class TaskControllerTest {
    private MockMvc mockMvc;

    @Mock
    private TaskService taskService;

    @InjectMocks
    private TaskController taskController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(taskController).build();
    }

    @Test
    void testGetAllTasks() throws Exception {
        when(taskService.getAllTasks()).thenReturn(Arrays.asList(taskDTO));

        mockMvc.perform(get("/api/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Test Task"))
                .andExpect(jsonPath("$[0].completed").value(false));

        verify(taskService, times(1)).getAllTasks();
    }
}
```

### 2. Service Tests (`TaskServiceTest`)

**O que testa:** Lógica de negócio, interação com Repository e Mapper, tratamento de Optional

**Ferramentas:** `@Mock TaskRepository`, `@Mock TaskMapper`, `@InjectMocks TaskService`

```java
when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
when(taskMapper.convertToDTO(task)).thenReturn(dto);
Optional<TaskDTO> result = taskService.getTaskById(1L);
verify(taskRepository, times(1)).findById(1L);
```

### 3. Repository Tests (`TaskRepositoryTest`)

**O que testa:** Operações CRUD no banco, queries, integridade dos dados

**Ferramentas:** `@DataJpaTest` (banco H2 em memória), `@Autowired TaskRepository`

```java
Task savedTask = taskRepository.save(task);
assertNotNull(savedTask.getId());
```

### 4. Mapper Tests (`TaskMapperTest`)

**O que testa:** Conversão `Entity → DTO` e `DTO → Entity`, preservação de dados

```java
TaskMapper mapper = new TaskMapper();
TaskDTO dto = mapper.convertToDTO(task);
assertEquals(task.getTitle(), dto.getTitle());
```

### 5. DTO/Model Tests

**O que testa:** Construtores, getters/setters, valores padrão

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
./mvnw test

# Executar testes de uma classe específica
./mvnw test -Dtest=TaskControllerTest

# Executar um teste específico
./mvnw test -Dtest=TaskControllerTest#testGetAllTasks

# Ver relatório de cobertura
./mvnw test jacoco:report
```

### Pelo IDE (VS Code, IntelliJ)

1. **Todos os testes**: clique direito na pasta `test` → "Run Tests"
2. **Por classe**: clique direito no arquivo `...Test.java` → "Run Tests"
3. **Por método**: clique no ícone `>` ao lado do método `@Test`

---

## Cobertura de Testes

Cobertura mede quantas linhas/métodos do código foram executados pelos testes.

**Meta ideal:** 80% ou mais de cobertura

| Tipo | Descrição |
| ---- | --------- |
| Line Coverage | % de linhas executadas |
| Branch Coverage | % de condicionais (if/else) testadas |
| Method Coverage | % de métodos executados |

---

## Boas Práticas

### 1. Nomenclatura de Testes

```java
// Bom
@Test void testGetTaskById_ShouldReturnTask_WhenTaskExists() { }

// Alternativa com @DisplayName
@Test 
@DisplayName("Deve retornar tarefa quando ela existe")
void testGetTaskById() { }
```

### 2. Um Conceito por Teste

```java
// Bom — testes separados
@Test void testCreateTask() { }
@Test void testUpdateTask() { }
@Test void testDeleteTask() { }
```

### 3. Testes Independentes

Cada teste deve preparar seu próprio cenário — nunca depender do estado deixado por outro teste.

### 4. Teste Cenários Positivos e Negativos

```java
@Test void testGetTaskById_Success() { }      // Cenário feliz
@Test void testGetTaskById_NotFound() { }     // Cenário de erro
@Test void testCreateTask_WithValidData() { } // Dados válidos
@Test void testCreateTask_WithNullTitle() { } // Dados inválidos
```

---

## Debugging de Testes

Quando um teste falha:

1. Leia a mensagem de erro completa
2. Identifique a linha que falhou
3. Verifique o ASSERT — o que era esperado vs o que foi retornado
4. Adicione prints temporários: `System.out.println("Result: " + result);`
5. Use o debugger — coloque breakpoint e execute em modo debug

---

## Recursos Adicionais

- [JUnit 5](https://junit.org/junit5/docs/current/user-guide/)
- [Mockito](https://javadoc.io/doc/org.mockito/mockito-core/latest/org/mockito/Mockito.html)
- [Spring Boot Testing](https://docs.spring.io/spring-boot/docs/current/reference/html/features.html#features.testing)

---

**Última atualização:** 24 de abril de 2026
