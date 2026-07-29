package com.todolist.api.dto;

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
        assertEquals(Boolean.FALSE, taskDTO.getCompleted());  // Não está completada
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
        assertEquals(Long.valueOf(1L), taskDTO.getId());
        assertEquals("Test Task", taskDTO.getTitle());
        assertEquals("Test Description", taskDTO.getDescription());
        assertEquals(Boolean.FALSE, taskDTO.getCompleted());
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
        assertEquals(Long.valueOf(2L), taskDTO.getId());
        assertEquals("New Title", taskDTO.getTitle());
        assertEquals("New Description", taskDTO.getDescription());
        assertEquals(Boolean.TRUE, taskDTO.getCompleted());
    }

    /**
     * TESTE: 'completed' aceita null
     *
     * Objetivo: Verificar que o campo distingue "não informado" (null) de "false".
     *
     * Esta é a base da correção do PUT parcial: o campo é um Boolean, e não um boolean
     * primitivo, justamente para que o service consiga saber se o cliente enviou ou não
     * o campo e, quando não enviou, preservar o estado atual da tarefa.
     */
    @Test
    void testCompletedAcceptsNull() {
        // ACT: DTO como chegaria de um PUT que omite 'completed'
        TaskDTO taskDTO = new TaskDTO(1L, "Test Task", "Test Description", null);

        // ASSERT: null é preservado, não convertido para false
        assertNull(taskDTO.getCompleted());
    }
}
