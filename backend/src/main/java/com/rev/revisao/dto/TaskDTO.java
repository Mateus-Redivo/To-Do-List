package com.rev.revisao.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class TaskDTO {
    
    @JsonProperty("id")
    private Long id;

    @NotBlank(message = "Title is required")
    @Size(max = 100, message = "Title must be less than 100 characters")
    @JsonProperty("title")
    private String title;

    @Size(max = 500, message = "Description must be less than 500 characters")
    @JsonProperty("description")
    private String description;

    @JsonProperty("completed")
    private Boolean completed;

    //Constructors

    public TaskDTO() {}

    public TaskDTO(Long id, String title, String description, Boolean completed){
        this.id = id;
        this.title = title;
        this.description = description;
        this.completed = completed;
    }

    //Getter and Setters

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
