package com.todolist.api.mapper;

import com.todolist.api.dto.TaskDTO;
import com.todolist.api.model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * CLASSE DE TESTE DO MAPPER
 *
 * Mapper é responsável por converter entre diferentes representações de dados:
 * - Entity (Task): Representa dados no banco
 * - DTO (TaskDTO): Representa dados transferidos pela API
 *
 * Por que usar Mappers?
 * - Separação de responsabilidades: banco de dados vs API
 * - Segurança: não expor estrutura interna do banco
 * - Flexibilidade: API pode ter formato diferente do banco
 *
 * Exemplo prático:
 * Cliente faz POST → JSON → DTO → Mapper → Entity → Banco
 * Banco → Entity → Mapper → DTO → JSON → Cliente
 */
class TaskMapperTest {

    private TaskMapper taskMapper;

    @BeforeEach
    void setUp() {
        // Cria uma nova instância do mapper para cada teste
        taskMapper = new TaskMapper();
    }

    /**
     * TESTE: Converter Entity para DTO
     *
     * Objetivo: Verificar se conseguimos transformar uma Task (banco) em TaskDTO (API)
     *
     * Cenário: Buscamos uma tarefa no banco e precisamos enviá-la pela API
     */
    @Test
    void testConvertToDTO() {
        // ARRANGE: Cria uma Task (entidade do banco)
        Task task = new Task("Test Task", "Test Description");
        task.setId(1L);  // Simula ID gerado pelo banco
        task.setCompleted(false);

        // ACT: Converte para DTO
        TaskDTO taskDTO = taskMapper.convertToDTO(task);

        // ASSERT: Verifica se todos os campos foram copiados corretamente
        // Os valores esperados são comparados como objetos (Long/Boolean) porque os
        // campos do DTO são tipos de referência e podem, em tese, ser nulos
        assertNotNull(taskDTO);
        assertEquals(Long.valueOf(1L), taskDTO.getId());
        assertEquals("Test Task", taskDTO.getTitle());
        assertEquals("Test Description", taskDTO.getDescription());
        assertEquals(Boolean.FALSE, taskDTO.getCompleted());
    }

    /**
     * TESTE: Converter DTO para Entity
     *
     * Objetivo: Verificar se conseguimos transformar TaskDTO (API) em Task (banco)
     *
     * Cenário: Cliente envia dados pela API e precisamos salvar no banco
     *
     * Note: O ID é null porque ainda não foi salvo no banco
     */
    @Test
    void testConvertToEntity() {
        // ARRANGE: Cria um DTO (dados vindos da API)
        TaskDTO taskDTO = new TaskDTO(null, "Test Task", "Test Description", true);

        // ACT: Converte para Entity
        Task task = taskMapper.convertToEntity(taskDTO);

        // ASSERT: Verifica a conversão
        assertNotNull(task);
        assertEquals("Test Task", task.getTitle());
        assertEquals("Test Description", task.getDescription());
        assertTrue(task.getCompleted());
    }

    /**
     * TESTE: Converter DTO sem 'completed' informado
     *
     * Objetivo: Verificar que um 'completed' nulo vira false na entidade.
     *
     * Por que importa: no DTO, 'completed' é um Boolean que aceita null para representar
     * "campo não enviado pelo cliente". Já a coluna do banco não aceita nulos, então o
     * mapper precisa converter esse null em false — caso contrário a conversão estouraria
     * com NullPointerException ao criar uma tarefa sem informar o campo.
     */
    @Test
    void testConvertToEntityWithNullCompleted() {
        // ARRANGE: DTO como chegaria de um POST que omite 'completed'
        TaskDTO taskDTO = new TaskDTO(null, "Test Task", "Test Description", null);

        // ACT
        Task task = taskMapper.convertToEntity(taskDTO);

        // ASSERT: Tarefa criada como pendente, sem exceção
        assertNotNull(task);
        assertFalse(task.getCompleted());
    }

    /**
     * TESTE: Conversão bidirecional (ida e volta)
     *
     * Objetivo: Verificar que não perdemos dados ao converter Entity→DTO→Entity
     *
     * Este teste garante a integridade dos dados durante conversões múltiplas.
     * É importante para garantir que operações de update não percam informações.
     */
    @Test
    void testConvertToDTOAndBack() {
        // ARRANGE: Cria uma Task original
        Task originalTask = new Task("Original", "Original Description");
        originalTask.setId(5L);
        originalTask.setCompleted(true);

        // ACT: Converte para DTO e depois de volta para Entity
        TaskDTO taskDTO = taskMapper.convertToDTO(originalTask);
        Task convertedTask = taskMapper.convertToEntity(taskDTO);

        // ASSERT: Verifica se os dados permaneceram iguais
        // Note: O ID não é preservado na conversão DTO→Entity, pois quem o gera é o banco
        assertEquals(originalTask.getTitle(), convertedTask.getTitle());
        assertEquals(originalTask.getDescription(), convertedTask.getDescription());
        assertEquals(originalTask.getCompleted(), convertedTask.getCompleted());
        assertNull(convertedTask.getId());
    }
}
