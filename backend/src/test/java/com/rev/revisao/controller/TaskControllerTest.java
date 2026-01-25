package com.rev.revisao.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rev.revisao.dto.TaskDTO;
import com.rev.revisao.service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * CLASSE DE TESTE DO CONTROLLER
 * 
 * Esta classe testa a camada de controle (Controller) da aplicação.
 * O Controller é responsável por receber as requisições HTTP e retornar respostas.
 * 
 * Conceitos importantes:
 * - @ExtendWith(MockitoExtension.class): Habilita o uso do Mockito para criar mocks
 * - Mock: Objeto simulado que imita o comportamento de um objeto real
 * - MockMvc: Permite testar controllers sem iniciar um servidor HTTP real
 */
@ExtendWith(MockitoExtension.class)
class TaskControllerTest {

    // MockMvc: Simula requisições HTTP para testar o controller
    private MockMvc mockMvc;

    // @Mock: Cria uma versão simulada do TaskService
    // Não vamos testar o service aqui, apenas simular seu comportamento
    @Mock
    private TaskService taskService;

    // @InjectMocks: Cria uma instância do TaskController e injeta os mocks nele
    @InjectMocks
    private TaskController taskController;

    // ObjectMapper: Converte objetos Java para JSON e vice-versa
    private ObjectMapper objectMapper;

    // Objeto de exemplo que será usado em vários testes
    private TaskDTO taskDTO;

    /**
     * Método executado ANTES de cada teste
     * Prepara o ambiente de teste (setup)
     */
    @BeforeEach
    void setUp() {
        // Configura o MockMvc para testar o controller
        mockMvc = MockMvcBuilders.standaloneSetup(taskController).build();
        
        // Inicializa o ObjectMapper para converter JSON
        objectMapper = new ObjectMapper();
        
        // Cria uma tarefa de exemplo para usar nos testes
        taskDTO = new TaskDTO(1L, "Test Task", "Test Description", false);
    }

    /**
     * TESTE: Buscar todas as tarefas
     * 
     * Objetivo: Verificar se o endpoint GET /api/tasks retorna a lista de tarefas
     * 
     * Passos do teste:
     * 1. Configurar o mock para retornar uma lista com uma tarefa
     * 2. Fazer uma requisição GET para /api/tasks
     * 3. Verificar se a resposta tem status 200 (OK)
     * 4. Verificar se os dados da tarefa estão corretos no JSON retornado
     * 5. Confirmar que o service foi chamado exatamente 1 vez
     */
    @Test
    void testGetAllTasks() throws Exception {
        // ARRANGE (Preparar): Configura o comportamento esperado do mock
        List<TaskDTO> tasks = Arrays.asList(taskDTO);
        when(taskService.getAllTasks()).thenReturn(tasks);

        // ACT (Agir): Executa a requisição HTTP simulada
        // ASSERT (Verificar): Valida os resultados
        mockMvc.perform(get("/api/tasks"))
                .andExpect(status().isOk())  // Verifica se retornou status 200
                .andExpect(jsonPath("$[0].title").value("Test Task"))  // Verifica o título no JSON
                .andExpect(jsonPath("$[0].completed").value(false));  // Verifica o status de conclusão

        // Verifica se o service foi chamado corretamente
        verify(taskService, times(1)).getAllTasks();
    }

    /**
     * TESTE: Buscar tarefa por ID - Caso de sucesso
     * 
     * Objetivo: Verificar se o endpoint GET /api/tasks/{id} retorna a tarefa correta
     * 
     * Conceito: Optional é usado para representar um valor que pode ou não existir
     */
    @Test
    void testGetTaskById() throws Exception {
        // ARRANGE: Configura o mock para retornar uma tarefa quando buscar ID 1
        when(taskService.getTaskById(1L)).thenReturn(Optional.of(taskDTO));

        // ACT & ASSERT: Executa e verifica
        mockMvc.perform(get("/api/tasks/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Test Task"))
                .andExpect(jsonPath("$.description").value("Test Description"));

        verify(taskService, times(1)).getTaskById(1L);
    }

    /**
     * TESTE: Buscar tarefa por ID - Caso de falha (não encontrada)
     * 
     * Objetivo: Verificar se retorna erro 404 quando a tarefa não existe
     * 
     * Este é um teste de caminho negativo (testa o que acontece quando algo dá errado)
     */
    @Test
    void testGetTaskByIdNotFound() throws Exception {
        // ARRANGE: Configura o mock para retornar Optional vazio (tarefa não existe)
        when(taskService.getTaskById(999L)).thenReturn(Optional.empty());

        // ACT & ASSERT: Verifica se retorna status 404 (Not Found)
        mockMvc.perform(get("/api/tasks/999"))
                .andExpect(status().isNotFound());

        verify(taskService, times(1)).getTaskById(999L);
    }

    /**
     * TESTE: Criar nova tarefa
     * 
     * Objetivo: Verificar se o endpoint POST /api/tasks cria uma tarefa corretamente
     * 
     * Conceitos:
     * - contentType(): Define que estamos enviando JSON
     * - content(): O corpo da requisição (JSON da tarefa)
     */
    @Test
    void testCreateTask() throws Exception {
        // ARRANGE: Configura o mock para retornar a tarefa criada
        when(taskService.createTask(any(TaskDTO.class))).thenReturn(taskDTO);

        // ACT & ASSERT: Faz requisição POST com JSON no corpo
        mockMvc.perform(post("/api/tasks")
                .contentType(MediaType.APPLICATION_JSON)  // Indica que enviamos JSON
                .content(objectMapper.writeValueAsString(taskDTO)))  // Converte objeto para JSON
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Test Task"));

        // Verifica que o service foi chamado com qualquer TaskDTO
        verify(taskService, times(1)).createTask(any(TaskDTO.class));
    }

    /**
     * TESTE: Atualizar tarefa - Caso de sucesso
     * 
     * Objetivo: Verificar se o endpoint PUT /api/tasks/{id} atualiza a tarefa
     * 
     * Conceito: eq() garante que o ID seja exatamente 1L
     */
    @Test
    void testUpdateTask() throws Exception {
        // ARRANGE: Cria uma tarefa atualizada
        TaskDTO updatedDTO = new TaskDTO(1L, "Updated Task", "Updated Description", true);
        // Configura o mock para retornar a tarefa atualizada
        when(taskService.updateTask(eq(1L), any(TaskDTO.class))).thenReturn(Optional.of(updatedDTO));

        // ACT & ASSERT: Faz requisição PUT
        mockMvc.perform(put("/api/tasks/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated Task"));

        verify(taskService, times(1)).updateTask(eq(1L), any(TaskDTO.class));
    }

    /**
     * TESTE: Atualizar tarefa - Caso de falha (não encontrada)
     * 
     * Objetivo: Verificar se retorna 404 ao tentar atualizar tarefa inexistente
     */
    @Test
    void testUpdateTaskNotFound() throws Exception {
        // ARRANGE: Mock retorna Optional vazio
        when(taskService.updateTask(eq(999L), any(TaskDTO.class))).thenReturn(Optional.empty());

        // ACT & ASSERT
        mockMvc.perform(put("/api/tasks/999")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(taskDTO)))
                .andExpect(status().isNotFound());
    }

    /**
     * TESTE: Deletar tarefa - Caso de sucesso
     * 
     * Objetivo: Verificar se o endpoint DELETE /api/tasks/{id} deleta corretamente
     * 
     * Retorno: true indica que a tarefa foi deletada com sucesso
     */
    @Test
    void testDeleteTask() throws Exception {
        // ARRANGE: Mock retorna true (tarefa foi deletada)
        when(taskService.deleteTask(1L)).thenReturn(true);

        // ACT & ASSERT: Verifica se retorna status 200
        mockMvc.perform(delete("/api/tasks/1"))
                .andExpect(status().isOk());

        verify(taskService, times(1)).deleteTask(1L);
    }

    /**
     * TESTE: Deletar tarefa - Caso de falha (não encontrada)
     * 
     * Retorno: false indica que a tarefa não foi encontrada
     */
    @Test
    void testDeleteTaskNotFound() throws Exception {
        // ARRANGE: Mock retorna false (tarefa não existe)
        when(taskService.deleteTask(999L)).thenReturn(false);

        // ACT & ASSERT: Verifica se retorna status 404
        mockMvc.perform(delete("/api/tasks/999"))
                .andExpect(status().isNotFound());

        verify(taskService, times(1)).deleteTask(999L);
    }

    /**
     * TESTE: Alternar status de conclusão - Caso de sucesso
     * 
     * Objetivo: Verificar se o endpoint PATCH /api/tasks/{id}/toggle alterna o status
     * 
     * PATCH é usado para atualizações parciais (apenas o campo 'completed')
     */
    @Test
    void testToggleTaskCompletion() throws Exception {
        // ARRANGE: Cria tarefa com status alternado (completed = true)
        TaskDTO toggledDTO = new TaskDTO(1L, "Test Task", "Test Description", true);
        when(taskService.toggleTaskCompletion(1L)).thenReturn(Optional.of(toggledDTO));

        // ACT & ASSERT: Verifica se o status mudou
        mockMvc.perform(patch("/api/tasks/1/toggle"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.completed").value(true));

        verify(taskService, times(1)).toggleTaskCompletion(1L);
    }

    /**
     * TESTE: Alternar status de conclusão - Caso de falha
     * 
     * Objetivo: Verificar comportamento quando tarefa não existe
     */
    @Test
    void testToggleTaskCompletionNotFound() throws Exception {
        // ARRANGE: Mock retorna Optional vazio
        when(taskService.toggleTaskCompletion(999L)).thenReturn(Optional.empty());

        // ACT & ASSERT: Verifica se retorna 404
        mockMvc.perform(patch("/api/tasks/999/toggle"))
                .andExpect(status().isNotFound());
    }
}
