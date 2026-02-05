package com.todolist.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.todolist.api.model.Task;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long>{
}
