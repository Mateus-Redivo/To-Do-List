package com.todolist.api.mapper;

import com.todolist.api.dto.TaskDTO;
import com.todolist.api.model.Task;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
public class TaskMapper {
    
    public TaskDTO convertToDTO(@NonNull Task task){
        return new TaskDTO(
            task.getId(),
            task.getTitle(),
            task.getDescription(),
            task.getCompleted()
        );
    }

    public @NonNull Task convertToEntity(@NonNull TaskDTO taskDTO){
        Task task = new Task();
        task.setTitle(taskDTO.getTitle());
        task.setDescription(taskDTO.getDescription());
        task.setCompleted(taskDTO.getCompleted());
        return task;
    }
}
