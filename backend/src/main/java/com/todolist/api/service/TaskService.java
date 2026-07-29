package com.todolist.api.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.todolist.api.dto.TaskDTO;
import com.todolist.api.mapper.TaskMapper;
import com.todolist.api.model.Task;
import com.todolist.api.repository.TaskRepository;

import java.util.List;
import java.util.Optional;

/**
 * Regras de negócio para o gerenciamento de tarefas. É a única camada que enxerga a entidade
 * {@link Task} — o que entra e sai daqui é sempre {@link TaskDTO}.
 *
 * <p>Quando uma tarefa não existe, os métodos retornam {@link Optional#empty()} em vez de
 * lançar exceção; cabe ao controller traduzir isso em 404.</p>
 *
 * <p>As escritas são transacionais para que a leitura e a gravação seguinte sejam atômicas —
 * sem isso, duas requisições concorrentes sobre a mesma tarefa poderiam sobrescrever uma à outra.</p>
 */
@Service
public class TaskService {

    private static final Logger logger = LoggerFactory.getLogger(TaskService.class);

    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;

    public TaskService(TaskRepository taskRepository, TaskMapper taskMapper) {
        this.taskRepository = taskRepository;
        this.taskMapper = taskMapper;
    }

    /** Lista todas as tarefas, ou uma lista vazia se não houver nenhuma. */
    @Transactional(readOnly = true)
    public List<TaskDTO> getAllTasks() {
        logger.debug("Fetching all tasks");
        return taskRepository.findAll()
                .stream()
                .map(taskMapper::convertToDTO)
                .toList();
    }

    /** Busca uma tarefa pelo id, ou {@link Optional#empty()} se ela não existir. */
    @Transactional(readOnly = true)
    public Optional<TaskDTO> getTaskById(Long id) {
        logger.debug("Fetching task with id={}", id);
        return taskRepository.findById(id)
                .map(taskMapper::convertToDTO);
    }

    /** Cria uma tarefa nova e a retorna já com o id atribuído pelo banco. */
    @Transactional
    public TaskDTO createTask(TaskDTO taskDTO) {
        logger.info("Creating task with title='{}'", taskDTO.getTitle());
        Task task = taskMapper.convertToEntity(taskDTO);
        Task savedTask = taskRepository.save(task);
        return taskMapper.convertToDTO(savedTask);
    }

    /**
     * Atualiza título, descrição e, se informado, o estado de conclusão.
     *
     * <p>{@code completed} só é sobrescrito quando o cliente envia o campo. Caso contrário um
     * PUT que só quisesse renomear a tarefa a marcaria como não concluída.</p>
     *
     * @return a tarefa atualizada, ou {@link Optional#empty()} se ela não existir
     */
    @Transactional
    public Optional<TaskDTO> updateTask(Long id, TaskDTO taskDTO) {
        logger.info("Updating task with id={}", id);
        return taskRepository.findById(id)
                .map(existingTask -> {
                    existingTask.setTitle(taskDTO.getTitle());
                    existingTask.setDescription(taskDTO.getDescription());
                    if (taskDTO.getCompleted() != null) {
                        existingTask.setCompleted(taskDTO.getCompleted());
                    }
                    Task updatedTask = taskRepository.save(existingTask);
                    return taskMapper.convertToDTO(updatedTask);
                });
    }

    /** @return {@code true} se a tarefa existia e foi removida */
    @Transactional
    public boolean deleteTask(Long id) {
        logger.info("Deleting task with id={}", id);
        if (taskRepository.existsById(id)) {
            taskRepository.deleteById(id);
            return true;
        }
        return false;
    }

    /** Inverte o estado de conclusão, ou {@link Optional#empty()} se a tarefa não existir. */
    @Transactional
    public Optional<TaskDTO> toggleTaskCompletion(Long id) {
        logger.info("Toggling completion for task with id={}", id);
        return taskRepository.findById(id)
                .map(task -> {
                    task.setCompleted(!task.getCompleted());
                    Task updatedTask = taskRepository.save(task);
                    return taskMapper.convertToDTO(updatedTask);
                });
    }
}
