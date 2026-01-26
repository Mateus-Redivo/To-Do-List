package com.todolist.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.todolist.api.model.Task;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long>{
    
    @Query("SELECT t.id FROM Task t ORDER BY t.id ASC")
    List<Long> findAllIds();
}
