package com.todolist.api.model;

import jakarta.persistence.*;
import org.springframework.lang.NonNull;

@Entity
@Table(name = "tasks")
public class Task {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private @NonNull String title;

    @Column
    private String description;

    @Column(nullable = false)
    private @NonNull Boolean completed = false;

    //Constructors
    public Task() {
        this.title = "";
        this.completed = false;
    }

    public Task(@NonNull String title, String description) {
        this.title = title;
        this.description = description;
        this.completed = false;
    }

    //Getters and Setters

    public Long getId() { 
        return id;
    }

    public @NonNull String getTitle() { 
        return title;
    }
    
    public String getDescription() { 
        return description;
    }

    public @NonNull Boolean getCompleted() { 
        return completed;
    }

    public void setId(Long id){
        this.id = id;
    }

    public void setTitle(@NonNull String title){
        this.title = title;
    }

    public void setDescription(String description){
        this.description = description;
    }

    public void setCompleted(@NonNull Boolean completed){
        this.completed = completed;
    }
}
