package com.rev.revisao.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * CLASSE DE TESTE DO MODEL (ENTITY)
 * 
 * Model/Entity representa uma tabela do banco de dados.
 * Cada instância da classe Task representa uma linha na tabela 'tasks'.
 * 
 * Diferença entre Model e DTO:
 * - Model: Representa dados no banco (anotado com @Entity)
 * - DTO: Usado para transferir dados entre camadas
 * 
 * Por que testar?
 * - Garantir que a entidade pode ser criada corretamente
 * - Verificar lógica de negócio na entidade (se houver)
 * - Validar valores padrão antes de persistir no banco
 */
class TaskTest {

    /**
     * TESTE: Construtor padrão
     * 
     * Objetivo: Verificar inicialização com valores padrão
     * 
     * JPA (Java Persistence API) exige um construtor sem argumentos.
     */
    @Test
    void testCreateTaskWithDefaultConstructor() {
        // ACT: Cria uma Task vazia
        Task task = new Task();
        
        // ASSERT: Verifica valores padrão
        assertNotNull(task);  // Objeto foi criado
        assertEquals("", task.getTitle());  // Título vazio
        assertFalse(task.getCompleted());  // Não completada por padrão
    }

    /**
     * TESTE: Construtor com parâmetros
     * 
     * Objetivo: Verificar criação de Task com dados iniciais
     * 
     * Note que 'completed' não é passado no construtor,
     * pois toda tarefa nova começa como não completada.
     */
    @Test
    void testCreateTaskWithParameters() {
        // ACT: Cria uma Task com título e descrição
        Task task = new Task("Test Task", "Test Description");
        
        // ASSERT: Verifica atribuição de valores
        assertEquals("Test Task", task.getTitle());
        assertEquals("Test Description", task.getDescription());
        assertFalse(task.getCompleted());  // Padrão é false
    }

    /**
     * TESTE: Getters e Setters
     * 
     * Objetivo: Verificar que podemos modificar a entidade
     * 
     * Importante: O ID geralmente é gerado automaticamente pelo banco,
     * mas podemos setá-lo manualmente em testes.
     */
    @Test
    void testSettersAndGetters() {
        // ARRANGE: Cria Task vazia
        Task task = new Task();
        
        // ACT: Define todos os campos
        task.setId(1L);
        task.setTitle("New Title");
        task.setDescription("New Description");
        task.setCompleted(true);
        
        // ASSERT: Verifica cada campo
        assertEquals(1L, task.getId());
        assertEquals("New Title", task.getTitle());
        assertEquals("New Description", task.getDescription());
        assertTrue(task.getCompleted());
    }

    /**
     * TESTE: Alternar conclusão da tarefa
     * 
     * Objetivo: Verificar a lógica de marcar/desmarcar uma tarefa como completa
     * 
     * Este teste simula o comportamento de um checkbox na interface:
     * - Usuário cria tarefa (completed = false)
     * - Usuário marca como completa (completed = true)
     */
    @Test
    void testTaskCompletion() {
        // ARRANGE: Cria uma nova tarefa
        Task task = new Task("Task", "Description");
        
        // ASSERT: Verifica estado inicial
        assertFalse(task.getCompleted());  // Começa como não completada
        
        // ACT: Marca como completada
        task.setCompleted(true);
        
        // ASSERT: Verifica mudança de estado
        assertTrue(task.getCompleted());  // Agora está completada
    }
}
