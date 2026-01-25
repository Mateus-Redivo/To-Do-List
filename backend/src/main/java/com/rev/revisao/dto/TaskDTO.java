package com.rev.revisao.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.lang.NonNull;

public class TaskDTO {
    
    @JsonProperty("id")
    private Long id;

    @NotBlank(message = "Title is required")
    @Size(max = 100, message = "Title must be less than 100 characters")
    @JsonProperty("title")
    private @NonNull String title;

    @Size(max = 500, message = "Description must be less than 500 characters")
    @JsonProperty("description")
    private String description;

    @JsonProperty("completed")
    private @NonNull Boolean completed;

    //Constructors

    public TaskDTO() {
        this.title = "";
        this.completed = false;
    }

    public TaskDTO(Long id, @NonNull String title, String description, @NonNull Boolean completed){
        this.id = id;
        this.title = title;
        this.description = description;
        this.completed = completed;
    }

    //Getter and Setters

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
