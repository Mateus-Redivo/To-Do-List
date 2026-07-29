package com.todolist.api.mapper;

import com.todolist.api.dto.TaskDTO;
import com.todolist.api.model.Task;

import org.springframework.stereotype.Component;

/**
 * Converte entre a entidade {@link Task} e o DTO {@link TaskDTO}, para que a estrutura da
 * tabela não vaze para o contrato da API.
 */
@Component
public class TaskMapper {

    /** Converte uma entidade persistida em DTO de resposta. */
    public TaskDTO convertToDTO(Task task) {
        return new TaskDTO(
            task.getId(),
            task.getTitle(),
            task.getDescription(),
            task.getCompleted()
        );
    }

    /**
     * Converte um DTO de requisição em uma entidade nova.
     *
     * <p>O id do DTO é ignorado — quem gera o identificador é o banco. Um {@code completed}
     * nulo vira {@code false}, já que a coluna correspondente não aceita nulos.</p>
     */
    public Task convertToEntity(TaskDTO taskDTO) {
        Task task = new Task();
        task.setTitle(taskDTO.getTitle());
        task.setDescription(taskDTO.getDescription());
        task.setCompleted(Boolean.TRUE.equals(taskDTO.getCompleted()));
        return task;
    }
}
