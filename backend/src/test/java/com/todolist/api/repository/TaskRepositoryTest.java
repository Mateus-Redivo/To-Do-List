package com.todolist.api.repository;

import com.todolist.api.model.Task;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * CLASSE DE TESTE DO REPOSITORY (CAMADA DE DADOS)
 *
 * Repository é responsável por interagir com o banco de dados.
 * Estende JpaRepository que fornece métodos prontos para CRUD:
 * - save(): Salvar/Atualizar
 * - findById(): Buscar por ID
 * - findAll(): Buscar todos
 * - deleteById(): Deletar por ID
 * - existsById(): Verificar se existe
 *
 * ANOTAÇÕES DESTA CLASSE
 *
 * @DataJpaTest: Anotação para testes de repository
 * - Carrega apenas a fatia JPA do contexto Spring, não a aplicação inteira (mais rápido)
 * - Envolve cada teste em uma transação revertida ao final, isolando um teste do outro
 *
 * @ActiveProfiles("test"): Ativa src/test/resources/application-test.properties,
 * que aponta o datasource para um banco H2 em memória. Sem isso os testes usariam
 * o MySQL de desenvolvimento configurado em src/main/resources/application.properties.
 *
 * @AutoConfigureTestDatabase(replace = NONE): Impede o Spring de substituir o datasource
 * por um banco embarcado escolhido automaticamente. Queremos exatamente o H2 configurado
 * no profile 'test', com MODE=MySQL — assim o banco de teste se comporta como o de produção.
 *
 * @Autowired: Injeta o repository real (não é mock)
 */
@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class TaskRepositoryTest {

    // Injeta o repository real, conectado ao banco H2 em memória
    @Autowired
    private TaskRepository taskRepository;

    /**
     * TESTE: Salvar tarefa no banco
     *
     * Objetivo: Verificar se o repository consegue persistir dados
     *
     * Observações:
     * - O ID é gerado automaticamente pelo banco (@GeneratedValue)
     * - Após salvar, o ID não será mais null
     */
    @Test
    void testSaveTask() {
        // ARRANGE: Cria uma nova tarefa (sem ID)
        Task task = new Task("Test Task", "Test Description");

        // ACT: Salva no banco
        Task savedTask = taskRepository.save(task);

        // ASSERT: Verifica se foi salva corretamente
        assertNotNull(savedTask.getId());  // Banco gerou um ID
        assertEquals("Test Task", savedTask.getTitle());
        assertEquals("Test Description", savedTask.getDescription());
        assertFalse(savedTask.getCompleted());  // Padrão é false
    }

    /**
     * TESTE: Buscar tarefa por ID
     *
     * Objetivo: Verificar se conseguimos recuperar uma tarefa específica
     *
     * Optional: Tipo que pode conter ou não um valor
     * - isPresent(): true se encontrou, false se não encontrou
     * - get(): retorna o valor (use apenas se isPresent() é true)
     */
    @Test
    void testFindById() {
        // ARRANGE: Salva uma tarefa primeiro
        Task task = new Task("Find Task", "Description");
        Task savedTask = taskRepository.save(task);

        // ACT: Busca pelo ID gerado
        Optional<Task> foundTask = taskRepository.findById(savedTask.getId());

        // ASSERT: Verifica se encontrou
        assertTrue(foundTask.isPresent());  // Encontrou a tarefa
        assertEquals("Find Task", foundTask.get().getTitle());
    }

    /**
     * TESTE: Buscar todas as tarefas
     *
     * Objetivo: Verificar se conseguimos listar todas as tarefas
     *
     * Note: Como cada teste roda dentro de uma transação revertida ao final
     * (@DataJpaTest), apenas as tarefas criadas neste teste existem aqui.
     */
    @Test
    void testFindAll() {
        // ARRANGE: Salva duas tarefas
        taskRepository.save(new Task("Task 1", "Description 1"));
        taskRepository.save(new Task("Task 2", "Description 2"));

        // ACT: Busca todas
        List<Task> tasks = taskRepository.findAll();

        // ASSERT: Verifica se retornou as 2 que criamos
        assertEquals(2, tasks.size());
    }

    /**
     * TESTE: Atualizar tarefa existente
     *
     * Objetivo: Verificar se conseguimos modificar uma tarefa salva
     *
     * JPA detecta que a entidade já tem um ID e faz UPDATE ao invés de INSERT
     */
    @Test
    void testUpdateTask() {
        // ARRANGE: Salva uma tarefa
        Task task = new Task("Original Title", "Original Description");
        Task savedTask = taskRepository.save(task);

        // ACT: Modifica e salva novamente
        savedTask.setTitle("Updated Title");
        savedTask.setCompleted(true);
        Task updatedTask = taskRepository.save(savedTask);

        // ASSERT: Verifica se as mudanças foram persistidas
        assertEquals("Updated Title", updatedTask.getTitle());
        assertTrue(updatedTask.getCompleted());
    }

    /**
     * TESTE: Deletar tarefa
     *
     * Objetivo: Verificar se conseguimos remover uma tarefa do banco
     *
     * Após deletar, tentar buscar deve retornar Optional vazio
     */
    @Test
    void testDeleteTask() {
        // ARRANGE: Salva uma tarefa
        Task task = new Task("Delete Task", "Description");
        Task savedTask = taskRepository.save(task);
        Long taskId = savedTask.getId();

        // ACT: Deleta a tarefa
        taskRepository.deleteById(taskId);

        // ASSERT: Verifica se foi deletada
        Optional<Task> deletedTask = taskRepository.findById(taskId);
        assertFalse(deletedTask.isPresent());  // Não deve encontrar mais
    }

    /**
     * TESTE: Verificar existência de tarefa
     *
     * Objetivo: Verificar o método existsById() que é mais eficiente
     * que buscar e verificar Optional
     *
     * Mais eficiente porque:
     * - Retorna apenas boolean (não carrega a entidade completa)
     * - SQL gerado é mais simples: SELECT COUNT(*)
     */
    @Test
    void testExistsById() {
        // ARRANGE: Salva uma tarefa
        Task task = new Task("Exists Task", "Description");
        Task savedTask = taskRepository.save(task);

        // ACT & ASSERT: Verifica existência
        assertTrue(taskRepository.existsById(savedTask.getId()));
        assertFalse(taskRepository.existsById(999L));  // ID inexistente
    }

    /**
     * TESTE DE REGRESSÃO: Descrição no tamanho máximo permitido pela API
     *
     * Objetivo: Garantir que a coluna 'description' comporta os 500 caracteres que a
     * validação do TaskDTO aceita (@Size(max = 500)).
     *
     * Por que este teste existe: a coluna era criada com o tamanho padrão de 255
     * caracteres, então uma descrição de 300 caracteres passava na validação e só
     * estourava no INSERT — devolvendo 500 Internal Server Error para o cliente.
     * O alinhamento entre @Size no DTO e @Column(length) na entidade é o que evita isso.
     */
    @Test
    void testSaveTaskWithMaximumLengthDescription() {
        // ARRANGE: Descrição no limite exato aceito pela validação da API
        String maxLengthDescription = "a".repeat(500);
        Task task = new Task("Long Description Task", maxLengthDescription);

        // ACT: Persiste e força a escrita imediata no banco
        Task savedTask = taskRepository.saveAndFlush(task);

        // ASSERT: Descrição gravada por inteiro, sem truncamento
        assertEquals(500, savedTask.getDescription().length());
        assertEquals(maxLengthDescription, savedTask.getDescription());
    }
}
