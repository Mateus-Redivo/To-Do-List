package com.todolist.api.model;

import jakarta.persistence.*;

/**
 * Entidade JPA que representa uma tarefa na tabela {@code tasks}.
 *
 * <p>Os tamanhos das colunas espelham as validações de {@link com.todolist.api.dto.TaskDTO}.
 * Se os dois lados divergirem, um valor aprovado pela validação da API pode ser rejeitado
 * pelo banco no INSERT.</p>
 */
@Entity
@Table(name = "tasks")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(length = 500)
    private String description;

    @Column(nullable = false)
    private boolean completed;

    /** Construtor sem argumentos exigido pelo JPA. */
    public Task() {
        this.title = "";
        this.completed = false;
    }

    /** Cria uma tarefa nova, sempre não concluída. */
    public Task(String title, String description) {
        this.title = title;
        this.description = description;
        this.completed = false;
    }

    /** @return o id, ou {@code null} se a tarefa ainda não foi persistida */
    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public boolean getCompleted() {
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

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
}
