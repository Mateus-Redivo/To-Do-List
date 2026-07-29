package com.todolist.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Contrato público da API para uma tarefa, usado como corpo de requisição e de resposta.
 *
 * <p>{@code completed} é {@link Boolean} e não {@code boolean} de propósito: só assim dá para
 * distinguir "campo não enviado" de "enviado como false". Num PUT, omitir o campo preserva o
 * estado atual; com um primitivo, a omissão marcaria a tarefa como não concluída sem querer.</p>
 */
@Schema(description = "Representação de uma tarefa")
public class TaskDTO {

    @Schema(description = "Identificador da tarefa, atribuído pelo servidor", example = "1",
            accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @NotBlank(message = "Title is required")
    @Size(max = 100, message = "Title must be less than 100 characters")
    @Schema(description = "Título da tarefa", example = "Estudar Spring Boot", maxLength = 100,
            requiredMode = Schema.RequiredMode.REQUIRED)
    private String title;

    @Size(max = 500, message = "Description must be less than 500 characters")
    @Schema(description = "Descrição detalhada da tarefa", example = "Completar o tutorial de Spring Boot",
            maxLength = 500)
    private String description;

    @Schema(description = "Estado de conclusão. Se omitido em um PUT, o estado atual é preservado",
            example = "false")
    private Boolean completed;

    /** Construtor sem argumentos exigido pelo Jackson. */
    public TaskDTO() {
        this.title = "";
        this.completed = false;
    }

    public TaskDTO(Long id, String title, String description, Boolean completed) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.completed = completed;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    /** @return o estado de conclusão, ou {@code null} se o campo não foi informado */
    public Boolean getCompleted() {
        return completed;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCompleted(Boolean completed) {
        this.completed = completed;
    }
}
