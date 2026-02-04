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
 * - findAll(): Buscar todas
 * - deleteById(): Deletar por ID
 * - existsById(): Verificar se existe
 * 
 * @DataJpaTest: Anotação especial para testes de repository
 * - Configura um banco em memória H2 para testes
 * - Aplica transações que são revertidas após cada teste
 * - Não carrega o contexto completo Spring (mais rápido)
 * 
 * @ActiveProfiles("test"): Usa application-test.properties com SQLite
 * 
 * @AutoConfigureTestDatabase(replace = NONE): Usa o banco configurado (SQLite)
 * ao invés de substituir por um banco H2
 * 
 * @Autowired: Injeta automaticamente o repository real (não é mock)
 */
@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@SuppressWarnings("null")
class TaskRepositoryTest {

    // Injeta o repository real (conectado ao banco H2 de teste)
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
     * Note: Como o banco é limpo entre testes (@DataJpaTest),
     * garantimos que apenas as tarefas deste teste existem.
     */
    @Test
    void testFindAll() {
        // ARRANGE: Salva duas tarefas
        taskRepository.save(new Task("Task 1", "Description 1"));
        taskRepository.save(new Task("Task 2", "Description 2"));
        
        // ACT: Busca todas
        List<Task> tasks = taskRepository.findAll();
        
        // ASSERT: Verifica se retornou pelo menos as 2 que criamos
        assertTrue(tasks.size() >= 2);
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
        assertTrue(taskRepository.existsById(savedTask.getId()));  // Deve existir
        assertFalse(taskRepository.existsById(999L));  // ID inexistente
    }
}
