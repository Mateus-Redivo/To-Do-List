package com.rev.revisao.mapper;

import org.springframework.stereotype.Component;
import com.rev.revisao.dto.TaskDTO;
import com.rev.revisao.model.Task;

@Component
public class TaskMapper {

    public TaskDTO toDTO(Task task) {
        if (task == null) {
            return null;
        }
        
        return new TaskDTO(
            task.getId(),
            task.getTitle(),
            task.getDescription(),
            task.getCompleted()
        );
    }

    public Task toEntity(TaskDTO taskDTO) {
        if (taskDTO == null) {
            return null;
        }
        
        Task task = new Task();
        task.setTitle(taskDTO.getTitle());
        task.setDescription(taskDTO.getDescription());
        task.setCompleted(taskDTO.getCompleted());
        return task;
    }

    public void updateEntityFromDTO(TaskDTO taskDTO, Task task) {
        if (taskDTO == null || task == null) {
            return;
        }
        
        if (taskDTO.getTitle() != null) {
            task.setTitle(taskDTO.getTitle());
        }
        if (taskDTO.getDescription() != null) {
            task.setDescription(taskDTO.getDescription());
        }
        if (taskDTO.getCompleted() != null) {
            task.setCompleted(taskDTO.getCompleted());
        }
    }
}
