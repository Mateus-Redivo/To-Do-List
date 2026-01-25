package com.rev.revisao.mapper;

import com.rev.revisao.dto.TaskDTO;
import com.rev.revisao.model.Task;
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
@SuppressWarnings("null")
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
        assertNotNull(taskDTO);
        assertEquals(1L, taskDTO.getId());
        assertEquals("Test Task", taskDTO.getTitle());
        assertEquals("Test Description", taskDTO.getDescription());
        assertFalse(taskDTO.getCompleted());
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
        // Note: O ID pode não ser preservado na conversão DTO→Entity
        assertEquals(originalTask.getTitle(), convertedTask.getTitle());
        assertEquals(originalTask.getDescription(), convertedTask.getDescription());
        assertEquals(originalTask.getCompleted(), convertedTask.getCompleted());
    }
}
