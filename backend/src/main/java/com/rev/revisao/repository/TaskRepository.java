package com.rev.revisao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rev.revisao.model.Task;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long>{
    
}
