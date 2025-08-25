package com.rev.revisao.model;

import jakarta.persistence.*;

@Entity
@Table(name = "tasks")
public class Task {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column
    private String description;

    @Column(nullable = false)
    private Boolean completed = false;

    //Constructors
    public Task() {}

    public Task(String title, String description) {
        this.title = title;
        this.description = description;
        this.completed = false;
    }

    //Getters and Setters

    public Long getId() { 
        return id;
    }

    public String getTitle() { 
        return title;
    }
    
    public String getDescription() { 
        return description;
    }

    public Boolean getCompleted() { 
        return completed;
    }

    public void setId(Long id){
        this.id = id;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public void setDescription(String description){
        this.description = description;
    }

    public void setCompleted(Boolean completed){
        this.completed = completed;
    }
}
