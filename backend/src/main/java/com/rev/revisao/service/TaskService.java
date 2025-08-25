package com.rev.revisao.service;

import org.springframework.stereotype.Service;

import com.rev.revisao.dto.TaskDTO;
import com.rev.revisao.mapper.TaskMapper;
import com.rev.revisao.model.Task;
import com.rev.revisao.repository.TaskRepository;

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

    public Optional<TaskDTO> getTaskById(Long id){
        return taskRepository.findById(id)
               .map(taskMapper::convertToDTO);
    }

    public TaskDTO createTask(TaskDTO taskDTO) {
        Task task = taskMapper.convertToEntity(taskDTO);
        Task savedTask = taskRepository.save(task);
        return taskMapper.convertToDTO(savedTask);
    }

    public Optional <TaskDTO> updateTask(Long id, TaskDTO taskDTO){
        return taskRepository.findById(id)
        .map(existingTask -> {
            existingTask.setTitle(taskDTO.getTitle());
            existingTask.setDescription(taskDTO.getDescription());
            existingTask.setCompleted(taskDTO.getCompleted());
            Task updatedTask = taskRepository.save(existingTask);
            return taskMapper.convertToDTO(updatedTask);
        });
    }

    public boolean deleteTask(Long id){
        if (taskRepository.existsById(id)){
            taskRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public Optional <TaskDTO> toggleTaskCompletion(Long id){
        return taskRepository.findById(id)
               .map(task -> {
                task.setCompleted(!task.getCompleted());
                Task updateTask = taskRepository.save(task);
                return taskMapper.convertToDTO(updateTask);
               });
    }
}
