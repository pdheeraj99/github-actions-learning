package com.example.todo.service;

import com.example.todo.entity.Todo;
import com.example.todo.repository.TodoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Todo Service
 * 
 * Business logic layer for Todo operations.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class TodoService {

    private final TodoRepository todoRepository;

    // Get all todos
    public List<Todo> getAllTodos() {
        return todoRepository.findAll();
    }

    // Get todo by ID
    public Optional<Todo> getTodoById(Long id) {
        return todoRepository.findById(id);
    }

    // Create new todo
    public Todo createTodo(Todo todo) {
        return todoRepository.save(todo);
    }

    // Update existing todo
    public Optional<Todo> updateTodo(Long id, Todo todoDetails) {
        return todoRepository.findById(id)
                .map(existingTodo -> {
                    existingTodo.setTitle(todoDetails.getTitle());
                    existingTodo.setDescription(todoDetails.getDescription());
                    existingTodo.setCompleted(todoDetails.isCompleted());
                    return todoRepository.save(existingTodo);
                });
    }

    // Toggle todo completion status
    public Optional<Todo> toggleTodo(Long id) {
        return todoRepository.findById(id)
                .map(todo -> {
                    todo.setCompleted(!todo.isCompleted());
                    return todoRepository.save(todo);
                });
    }

    // Delete todo
    public boolean deleteTodo(Long id) {
        if (todoRepository.existsById(id)) {
            todoRepository.deleteById(id);
            return true;
        }
        return false;
    }

    // Get completed/incomplete todos
    public List<Todo> getTodosByStatus(boolean completed) {
        return todoRepository.findByCompleted(completed);
    }

    // Search todos by title
    public List<Todo> searchTodos(String title) {
        return todoRepository.findByTitleContainingIgnoreCase(title);
    }
}
