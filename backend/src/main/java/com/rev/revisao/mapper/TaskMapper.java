package com.rev.revisao.mapper;

import com.rev.revisao.dto.TaskDTO;
import com.rev.revisao.model.Task;

import org.springframework.stereotype.Component;

@Component
public class TaskMapper {
    
    public TaskDTO convertToDTO(Task task){
        return new TaskDTO(
            task.getId(),
            task.getTitle(),
            task.getDescription(),
            task.getCompleted()
        );
    }

    public Task convertToEntity(TaskDTO taskDTO){
        Task task = new Task();
        task.setTitle(taskDTO.getTitle());
        task.setDescription(taskDTO.getDescription());
        task.setCompleted(taskDTO.getCompleted());
        return task;
    }
}
