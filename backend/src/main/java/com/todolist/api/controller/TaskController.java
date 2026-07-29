package com.todolist.api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;

import com.todolist.api.dto.TaskDTO;
import com.todolist.api.service.TaskService;

import jakarta.validation.Valid;

import java.util.List;
import java.util.Optional;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * Endpoints REST de gerenciamento de tarefas, sob {@code /api/tasks}.
 *
 * <p>Não contém regra de negócio: delega ao {@link TaskService} e traduz o resultado em status
 * HTTP — um {@link Optional} vazio vira 404. As exceções são tratadas pelo
 * {@link com.todolist.api.exceptions.GlobalExceptionHandler}.</p>
 */
@RestController
@RequestMapping("/api/tasks")
@Tag(name = "Tasks", description = "Task management API")
public class TaskController {

    private static final String VALIDATION_ERROR_EXAMPLE = "{\"title\": \"Title is required\"}";
    private static final String GENERIC_ERROR_EXAMPLE = "{\"error\": \"Invalid value for parameter 'id'\"}";
    private static final String SERVER_ERROR_EXAMPLE = "{\"error\": \"Internal server error. Please try again.\"}";

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    /** Lista todas as tarefas. */
    @Operation(summary = "List all tasks")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Task list returned successfully"),
        @ApiResponse(responseCode = "500", description = "Unexpected server error",
            content = @Content(mediaType = "application/json",
                schema = @Schema(example = SERVER_ERROR_EXAMPLE)))
    })
    @GetMapping
    public ResponseEntity<List<TaskDTO>> getAllTasks() {
        List<TaskDTO> tasks = taskService.getAllTasks();
        return ResponseEntity.ok(tasks);
    }

    /** Busca uma tarefa por id. */
    @Operation(summary = "Get task by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Task found"),
        @ApiResponse(responseCode = "400", description = "The id is not a valid number",
            content = @Content(mediaType = "application/json",
                schema = @Schema(example = GENERIC_ERROR_EXAMPLE))),
        @ApiResponse(responseCode = "404", description = "Task not found", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<TaskDTO> getTaskById(@PathVariable Long id) {
        Optional<TaskDTO> task = taskService.getTaskById(id);
        return task.map(ResponseEntity::ok)
               .orElse(ResponseEntity.notFound().build());
    }

    /** Cria uma tarefa nova. O {@code id} enviado no corpo é ignorado. */
    @Operation(summary = "Create a new task")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Task created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request body",
            content = @Content(mediaType = "application/json",
                schema = @Schema(example = VALIDATION_ERROR_EXAMPLE)))
    })
    @PostMapping
    public ResponseEntity<TaskDTO> createTask(@Valid @RequestBody TaskDTO taskDTO) {
        TaskDTO createdTask = taskService.createTask(taskDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTask);
    }

    /** Atualiza uma tarefa. Omitir {@code completed} preserva o estado de conclusão atual. */
    @Operation(summary = "Update task by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Task updated successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request body",
            content = @Content(mediaType = "application/json",
                schema = @Schema(example = VALIDATION_ERROR_EXAMPLE))),
        @ApiResponse(responseCode = "404", description = "Task not found", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<TaskDTO> updateTask(@PathVariable Long id, @Valid @RequestBody TaskDTO taskDTO) {
        Optional<TaskDTO> updatedTask = taskService.updateTask(id, taskDTO);
        return updatedTask.map(ResponseEntity::ok)
               .orElse(ResponseEntity.notFound().build());
    }

    /** Remove uma tarefa. */
    @Operation(summary = "Delete task by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Task deleted successfully", content = @Content),
        @ApiResponse(responseCode = "404", description = "Task not found", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        boolean deleted = taskService.deleteTask(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    /** Inverte o estado de conclusão de uma tarefa. */
    @Operation(summary = "Toggle task completion status")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Task completion status toggled successfully"),
        @ApiResponse(responseCode = "404", description = "Task not found", content = @Content)
    })
    @PatchMapping("/{id}/toggle")
    public ResponseEntity<TaskDTO> toggleTaskCompletion(@PathVariable Long id) {
        Optional<TaskDTO> updatedTask = taskService.toggleTaskCompletion(id);
        return updatedTask.map(ResponseEntity::ok)
               .orElse(ResponseEntity.notFound().build());
    }
}
