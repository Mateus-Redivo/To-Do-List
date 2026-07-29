package com.todolist.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.todolist.api.model.Task;

/**
 * Acesso a dados da entidade {@link Task}.
 *
 * <p>Estender {@link JpaRepository} já fornece todas as operações usadas pela aplicação, com a
 * implementação gerada pelo Spring Data em tempo de execução — por isso a interface é vazia.
 * Consultas específicas entram aqui como métodos derivados do nome ou anotados com {@code @Query}.</p>
 */
@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
}
