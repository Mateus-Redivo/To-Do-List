package com.rev.revisao.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * CLASSE DE TESTE DO DTO (Data Transfer Object)
 * 
 * DTO é um objeto usado para transferir dados entre camadas da aplicação.
 * 
 * Por que testar DTOs?
 * - Garantir que construtores funcionam corretamente
 * - Verificar que getters e setters estão funcionando
 * - Validar valores padrão
 * 
 * Estes são testes mais simples, focados em verificar a estrutura da classe.
 */
class TaskDTOTest {

    /**
     * TESTE: Construtor padrão (sem parâmetros)
     * 
     * Objetivo: Verificar se o construtor vazio inicializa valores padrão
     * 
     * Valores padrão esperados:
     * - title: string vazia ""
     * - completed: false
     */
    @Test
    void testCreateTaskDTOWithDefaultConstructor() {
        // ACT: Cria um DTO usando o construtor padrão
        TaskDTO taskDTO = new TaskDTO();
        
        // ASSERT: Verifica os valores padrão
        assertNotNull(taskDTO);  // Objeto não é nulo
        assertEquals("", taskDTO.getTitle());  // Título é string vazia
        assertFalse(taskDTO.getCompleted());  // Não está completada
    }

    /**
     * TESTE: Construtor com parâmetros
     * 
     * Objetivo: Verificar se o construtor aceita e armazena valores corretamente
     */
    @Test
    void testCreateTaskDTOWithParameters() {
        // ACT: Cria um DTO com valores específicos
        TaskDTO taskDTO = new TaskDTO(1L, "Test Task", "Test Description", false);
        
        // ASSERT: Verifica se os valores foram atribuídos corretamente
        assertEquals(1L, taskDTO.getId());
        assertEquals("Test Task", taskDTO.getTitle());
        assertEquals("Test Description", taskDTO.getDescription());
        assertFalse(taskDTO.getCompleted());
    }

    /**
     * TESTE: Getters e Setters
     * 
     * Objetivo: Verificar se conseguimos modificar e recuperar valores
     * 
     * Padrão JavaBean:
     * - Setter: método que define um valor (setTitle)
     * - Getter: método que retorna um valor (getTitle)
     */
    @Test
    void testSettersAndGetters() {
        // ARRANGE: Cria um DTO vazio
        TaskDTO taskDTO = new TaskDTO();
        
        // ACT: Define valores usando setters
        taskDTO.setId(2L);
        taskDTO.setTitle("New Title");
        taskDTO.setDescription("New Description");
        taskDTO.setCompleted(true);
        
        // ASSERT: Recupera valores usando getters e verifica
        assertEquals(2L, taskDTO.getId());
        assertEquals("New Title", taskDTO.getTitle());
        assertEquals("New Description", taskDTO.getDescription());
        assertTrue(taskDTO.getCompleted());
    }
}
