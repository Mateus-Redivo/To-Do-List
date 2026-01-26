package com.todolist.api.service;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import com.todolist.api.dto.TaskDTO;
import com.todolist.api.mapper.TaskMapper;
import com.todolist.api.model.Task;
import com.todolist.api.repository.TaskRepository;

import java.util.List;
import java.util.Optional;

@Service
public class TaskService {
    
    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;

    public TaskService(TaskRepository taskRepository, TaskMapper taskMapper) {
        this.taskRepository = taskRepository;
        this.taskMapper = taskMapper;
    }

    public List<TaskDTO> getAllTasks() {
        return taskRepository.findAll()
        .stream()
        .map(taskMapper::convertToDTO)
        .toList();
    }

    public Optional<TaskDTO> getTaskById(@NonNull Long id){
        return taskRepository.findById(id)
               .map(taskMapper::convertToDTO);
    }

    public TaskDTO createTask(@NonNull TaskDTO taskDTO) {
        Task task = taskMapper.convertToEntity(taskDTO);
        
        // Encontra o menor ID disponível
        Long lowestAvailableId = findLowestAvailableId();
        task.setId(lowestAvailableId);
        
        Task savedTask = taskRepository.save(task);
        return taskMapper.convertToDTO(savedTask);
    }

    private Long findLowestAvailableId() {
        List<Long> existingIds = taskRepository.findAllIds();
        
        if (existingIds.isEmpty()) {
            return 1L;
        }
        
        // Procura o primeiro gap nos IDs
        long expectedId = 1L;
        for (Long id : existingIds) {
            if (id != expectedId) {
                return expectedId;
            }
            expectedId++;
        }
        
        // Se não há gap, retorna o próximo ID após o último
        return expectedId;
    }

    public Optional <TaskDTO> updateTask(@NonNull Long id, @NonNull TaskDTO taskDTO){
        return taskRepository.findById(id)
        .map(existingTask -> {
            existingTask.setTitle(taskDTO.getTitle());
            existingTask.setDescription(taskDTO.getDescription());
            existingTask.setCompleted(taskDTO.getCompleted());
            Task updatedTask = taskRepository.save(existingTask);
            return taskMapper.convertToDTO(updatedTask);
        });
    }

    public boolean deleteTask(@NonNull Long id){
        if (taskRepository.existsById(id)){
            taskRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public Optional <TaskDTO> toggleTaskCompletion(@NonNull Long id){
        return taskRepository.findById(id)
               .map(task -> {
                task.setCompleted(!task.getCompleted());
                Task updateTask = taskRepository.save(task);
                return taskMapper.convertToDTO(updateTask);
               });
    }
}
