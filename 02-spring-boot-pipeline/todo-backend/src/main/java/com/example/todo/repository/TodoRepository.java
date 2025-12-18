package com.example.todo.repository;

import com.example.todo.entity.Todo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Todo Repository
 * 
 * Spring Data JPA repository for Todo entity.
 * Provides CRUD operations automatically.
 */
@Repository
public interface TodoRepository extends JpaRepository<Todo, Long> {

    // Find all completed todos
    List<Todo> findByCompleted(boolean completed);

    // Find todos containing title (case-insensitive search)
    List<Todo> findByTitleContainingIgnoreCase(String title);
}
