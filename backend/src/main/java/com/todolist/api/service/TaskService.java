package com.todolist.api.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.todolist.api.dto.TaskDTO;
import com.todolist.api.mapper.TaskMapper;
import com.todolist.api.model.Task;
import com.todolist.api.repository.TaskRepository;

import java.util.List;
import java.util.Optional;

@Service
public class TaskService {

    private static final Logger logger = LoggerFactory.getLogger(TaskService.class);

    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;

    public TaskService(TaskRepository taskRepository, TaskMapper taskMapper) {
        this.taskRepository = taskRepository;
        this.taskMapper = taskMapper;
    }

    public List<TaskDTO> getAllTasks() {
        logger.debug("Fetching all tasks");
        return taskRepository.findAll()
                .stream()
                .map(taskMapper::convertToDTO)
                .toList();
    }

    public Optional<TaskDTO> getTaskById(Long id) {
        logger.debug("Fetching task with id={}", id);
        return taskRepository.findById(id)
                .map(taskMapper::convertToDTO);
    }

    public TaskDTO createTask(TaskDTO taskDTO) {
        logger.info("Creating task with title='{}'", taskDTO.getTitle());
        Task task = taskMapper.convertToEntity(taskDTO);
        Task savedTask = taskRepository.save(task);
        return taskMapper.convertToDTO(savedTask);
    }

    public Optional<TaskDTO> updateTask(Long id, TaskDTO taskDTO) {
        logger.info("Updating task with id={}", id);
        return taskRepository.findById(id)
                .map(existingTask -> {
                    existingTask.setTitle(taskDTO.getTitle());
                    existingTask.setDescription(taskDTO.getDescription());
                    existingTask.setCompleted(taskDTO.getCompleted());
                    Task updatedTask = taskRepository.save(existingTask);
                    return taskMapper.convertToDTO(updatedTask);
                });
    }

    public boolean deleteTask(Long id) {
        logger.info("Deleting task with id={}", id);
        if (taskRepository.existsById(id)) {
            taskRepository.deleteById(id);
            return true;
        }
        return false;
    }

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
