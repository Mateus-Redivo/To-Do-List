package com.todolist.api.service;

import com.todolist.api.dto.TaskDTO;
import com.todolist.api.mapper.TaskMapper;
import com.todolist.api.model.Task;
import com.todolist.api.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

/**
 * CLASSE DE TESTE DO SERVICE (CAMADA DE NEGÓCIO)
 * 
 * Esta classe testa a camada de serviço da aplicação.
 * O Service contém a lógica de negócio e faz a ponte entre o Controller e o Repository.
 * 
 * Diferença entre testar Controller e Service:
 * - Controller: Testa requisições HTTP e respostas
 * - Service: Testa a lógica de negócio e interação com o banco de dados
 */
@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    // @Mock: Cria versões simuladas das dependências
    @Mock
    private TaskRepository taskRepository;  // Simula o acesso ao banco de dados

    @Mock
    private TaskMapper taskMapper;  // Simula a conversão entre Entity e DTO

    // @InjectMocks: Cria o service e injeta os mocks nele
    @InjectMocks
    private TaskService taskService;

    // Objetos de exemplo usados nos testes
    private Task task;  // Entidade do banco de dados
    private TaskDTO taskDTO;  // Objeto de transferência de dados

    /**
     * Método executado ANTES de cada teste
     * Inicializa os objetos de exemplo
     */
    @BeforeEach
    void setUp() {
        // Cria uma Task (entidade do banco)
        task = new Task("Test Task", "Test Description");
        task.setId(1L);
        task.setCompleted(false);

        // Cria um TaskDTO (objeto para transferência de dados)
        taskDTO = new TaskDTO(1L, "Test Task", "Test Description", false);
    }

    /**
     * TESTE: Buscar todas as tarefas
     * 
     * Objetivo: Verificar se o service retorna todas as tarefas do banco
     * 
     * Fluxo testado:
     * 1. Repository busca todas as tasks no banco
     * 2. Mapper converte cada Task em TaskDTO
     * 3. Service retorna a lista de DTOs
     */
    @Test
    void testGetAllTasks() {
        // ARRANGE: Configura os mocks
        List<Task> tasks = Arrays.asList(task);  // Lista com uma tarefa
        when(taskRepository.findAll()).thenReturn(tasks);  // Mock do repository
        when(taskMapper.convertToDTO(any(Task.class))).thenReturn(taskDTO);  // Mock do mapper

        // ACT: Executa o método que estamos testando
        List<TaskDTO> result = taskService.getAllTasks();

        // ASSERT: Verifica os resultados
        assertEquals(1, result.size());  // Verifica se retornou 1 tarefa
        verify(taskRepository, times(1)).findAll();  // Confirma que o repository foi chamado
    }

    /**
     * TESTE: Buscar tarefa por ID - Caso de sucesso
     * 
     * Objetivo: Verificar se o service encontra e retorna uma tarefa específica
     * 
     * Optional.isPresent(): Verifica se o Optional contém um valor
     */
    @Test
    void testGetTaskById() {
        // ARRANGE: Repository retorna uma tarefa
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(taskMapper.convertToDTO(task)).thenReturn(taskDTO);

        // ACT
        Optional<TaskDTO> result = taskService.getTaskById(1L);

        // ASSERT
        assertTrue(result.isPresent());  // Verifica se encontrou a tarefa
        assertEquals("Test Task", result.get().getTitle());  // Verifica o título
        verify(taskRepository, times(1)).findById(1L);
    }

    /**
     * TESTE: Buscar tarefa por ID - Caso de falha
     * 
     * Objetivo: Verificar comportamento quando a tarefa não existe
     * 
     * Optional.empty(): Representa ausência de valor
     */
    @Test
    void testGetTaskByIdNotFound() {
        // ARRANGE: Repository retorna Optional vazio
        when(taskRepository.findById(999L)).thenReturn(Optional.empty());

        // ACT
        Optional<TaskDTO> result = taskService.getTaskById(999L);

        // ASSERT
        assertFalse(result.isPresent());  // Verifica que não encontrou nada
    }

    /**
     * TESTE: Criar nova tarefa
     * 
     * Objetivo: Verificar se o service cria e salva uma tarefa corretamente
     * 
     * Fluxo:
     * 1. Mapper converte DTO em Entity
     * 2. Repository salva no banco
     * 3. Mapper converte Entity salva em DTO
     * 4. Retorna o DTO
     */
    @Test
    void testCreateTask() {
        // ARRANGE: Configura os mocks para simular criação
        when(taskMapper.convertToEntity(taskDTO)).thenReturn(task);
        when(taskRepository.save(task)).thenReturn(task);
        when(taskMapper.convertToDTO(task)).thenReturn(taskDTO);

        // ACT
        TaskDTO result = taskService.createTask(taskDTO);

        // ASSERT
        assertNotNull(result);  // Verifica que não é nulo
        assertEquals("Test Task", result.getTitle());
        verify(taskRepository, times(1)).save(any(Task.class));  // Confirma que salvou
    }

    /**
     * TESTE: Atualizar tarefa - Caso de sucesso
     * 
     * Objetivo: Verificar se o service atualiza uma tarefa existente
     * 
     * Fluxo:
     * 1. Repository busca a tarefa existente
     * 2. Service atualiza os campos da tarefa
     * 3. Repository salva as alterações
     */
    @Test
    void testUpdateTask() {
        // ARRANGE: Cria DTO com dados atualizados
        TaskDTO updatedDTO = new TaskDTO(1L, "Updated Title", "Updated Description", true);
        
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(taskRepository.save(any(Task.class))).thenReturn(task);
        when(taskMapper.convertToDTO(any(Task.class))).thenReturn(updatedDTO);

        // ACT
        Optional<TaskDTO> result = taskService.updateTask(1L, updatedDTO);

        // ASSERT
        assertTrue(result.isPresent());
        verify(taskRepository, times(1)).findById(1L);  // Confirma que buscou a tarefa
        verify(taskRepository, times(1)).save(any(Task.class));  // Confirma que salvou
    }

    /**
     * TESTE: Atualizar tarefa - Caso de falha
     * 
     * Objetivo: Verificar que não salva quando a tarefa não existe
     * 
     * verify(mock, never()): Confirma que um método NUNCA foi chamado
     */
    @Test
    void testUpdateTaskNotFound() {
        // ARRANGE: Repository não encontra a tarefa
        when(taskRepository.findById(999L)).thenReturn(Optional.empty());

        // ACT
        Optional<TaskDTO> result = taskService.updateTask(999L, taskDTO);

        // ASSERT
        assertFalse(result.isPresent());
        verify(taskRepository, never()).save(any(Task.class));  // Não deve salvar
    }

    /**
     * TESTE: Deletar tarefa - Caso de sucesso
     * 
     * Objetivo: Verificar se o service deleta uma tarefa existente
     * 
     * existsById(): Verifica se existe uma tarefa com o ID antes de deletar
     */
    @Test
    void testDeleteTask() {
        // ARRANGE: Tarefa existe no banco
        when(taskRepository.existsById(1L)).thenReturn(true);

        // ACT
        boolean result = taskService.deleteTask(1L);

        // ASSERT
        assertTrue(result);  // Retornou true (sucesso)
        verify(taskRepository, times(1)).deleteById(1L);  // Confirma que deletou
    }

    /**
     * TESTE: Deletar tarefa - Caso de falha
     * 
     * Objetivo: Verificar que não deleta quando a tarefa não existe
     */
    @Test
    void testDeleteTaskNotFound() {
        // ARRANGE: Tarefa não existe
        when(taskRepository.existsById(999L)).thenReturn(false);

        // ACT
        boolean result = taskService.deleteTask(999L);

        // ASSERT
        assertFalse(result);  // Retornou false (falha)
        // anyLong() em vez de any(): any() produziria null, que o repository não aceita
        verify(taskRepository, never()).deleteById(anyLong());  // Não tentou deletar
    }

    /**
     * TESTE: Alternar status de conclusão - Caso de sucesso
     * 
     * Objetivo: Verificar se alterna o campo 'completed' da tarefa
     * 
     * Este método inverte o valor booleano: false -> true ou true -> false
     */
    @Test
    void testToggleTaskCompletion() {
        // ARRANGE
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(taskRepository.save(any(Task.class))).thenReturn(task);
        when(taskMapper.convertToDTO(any(Task.class))).thenReturn(taskDTO);

        // ACT
        Optional<TaskDTO> result = taskService.toggleTaskCompletion(1L);

        // ASSERT
        assertTrue(result.isPresent());
        verify(taskRepository, times(1)).findById(1L);
        verify(taskRepository, times(1)).save(any(Task.class));
    }

    /**
     * TESTE: Alternar status de conclusão - Caso de falha
     * 
     * Objetivo: Verificar comportamento quando tarefa não existe
     */
    @Test
    void testToggleTaskCompletionNotFound() {
        // ARRANGE
        when(taskRepository.findById(999L)).thenReturn(Optional.empty());

        // ACT
        Optional<TaskDTO> result = taskService.toggleTaskCompletion(999L);

        // ASSERT
        assertFalse(result.isPresent());
        verify(taskRepository, never()).save(any(Task.class));
    }

    /**
     * TESTE DE REGRESSÃO: Atualizar sem informar 'completed' preserva o estado atual
     *
     * Objetivo: Garantir que um PUT que omite o campo 'completed' não desmarque uma
     * tarefa que já estava concluída.
     *
     * Por que este teste existe: 'completed' era um boolean primitivo no DTO, então um
     * campo ausente no JSON virava false — indistinguível de um false enviado de propósito.
     * Na prática, renomear uma tarefa concluída a marcava como pendente silenciosamente.
     * Com Boolean, null significa "não informado" e o service preserva o valor atual.
     *
     * ArgumentCaptor: captura o objeto realmente passado ao mock, permitindo inspecionar
     * o estado da entidade no momento em que o service mandou salvá-la.
     */
    @Test
    void testUpdateTaskWithoutCompletedKeepsCurrentState() {
        // ARRANGE: Tarefa já concluída no banco
        task.setCompleted(true);
        // DTO só com título e descrição — 'completed' não informado
        TaskDTO partialDTO = new TaskDTO(1L, "Renamed Title", "Renamed Description", null);

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(taskRepository.save(any(Task.class))).thenReturn(task);
        when(taskMapper.convertToDTO(any(Task.class))).thenReturn(partialDTO);

        // ACT
        Optional<TaskDTO> result = taskService.updateTask(1L, partialDTO);

        // ASSERT: Título e descrição mudaram, conclusão permaneceu true
        assertTrue(result.isPresent());

        ArgumentCaptor<Task> savedTask = ArgumentCaptor.forClass(Task.class);
        verify(taskRepository).save(savedTask.capture());
        assertEquals("Renamed Title", savedTask.getValue().getTitle());
        assertTrue(savedTask.getValue().getCompleted());  // Continua concluída
    }

    /**
     * TESTE: Atualizar informando 'completed' sobrescreve o estado
     *
     * Contraparte do teste acima: quando o cliente envia o campo, o valor deve ser aplicado.
     */
    @Test
    void testUpdateTaskWithCompletedOverwritesState() {
        // ARRANGE: Tarefa concluída que será marcada como pendente
        task.setCompleted(true);
        TaskDTO fullDTO = new TaskDTO(1L, "Test Task", "Test Description", false);

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(taskRepository.save(any(Task.class))).thenReturn(task);
        when(taskMapper.convertToDTO(any(Task.class))).thenReturn(fullDTO);

        // ACT
        taskService.updateTask(1L, fullDTO);

        // ASSERT: O false enviado foi aplicado
        ArgumentCaptor<Task> savedTask = ArgumentCaptor.forClass(Task.class);
        verify(taskRepository).save(savedTask.capture());
        assertFalse(savedTask.getValue().getCompleted());
    }
}
