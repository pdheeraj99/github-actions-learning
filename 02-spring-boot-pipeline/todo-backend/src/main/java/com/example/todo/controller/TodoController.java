package com.example.todo.controller;

import com.example.todo.entity.Todo;
import com.example.todo.service.TodoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Todo REST Controller
 * 
 * Handles HTTP requests for Todo operations.
 * 
 * Endpoints:
 * - GET /api/todos - Get all todos
 * - GET /api/todos/{id} - Get todo by ID
 * - POST /api/todos - Create new todo
 * - PUT /api/todos/{id} - Update todo
 * - PATCH /api/todos/{id}/toggle - Toggle completion
 * - DELETE /api/todos/{id} - Delete todo
 */
@RestController
@RequestMapping("/api/todos")
@RequiredArgsConstructor
@CrossOrigin(origins = "*") // Allow React frontend (will configure properly later)
public class TodoController {

    private final TodoService todoService;

    // GET /api/todos - Get all todos
    @GetMapping
    public ResponseEntity<List<Todo>> getAllTodos() {
        return ResponseEntity.ok(todoService.getAllTodos());
    }

    // GET /api/todos/{id} - Get todo by ID
    @GetMapping("/{id}")
    public ResponseEntity<Todo> getTodoById(@PathVariable Long id) {
        return todoService.getTodoById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // POST /api/todos - Create new todo
    @PostMapping
    public ResponseEntity<Todo> createTodo(@Valid @RequestBody Todo todo) {
        Todo createdTodo = todoService.createTodo(todo);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTodo);
    }

    // PUT /api/todos/{id} - Update todo
    @PutMapping("/{id}")
    public ResponseEntity<Todo> updateTodo(
            @PathVariable Long id,
            @Valid @RequestBody Todo todoDetails) {
        return todoService.updateTodo(id, todoDetails)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // PATCH /api/todos/{id}/toggle - Toggle completion status
    @PatchMapping("/{id}/toggle")
    public ResponseEntity<Todo> toggleTodo(@PathVariable Long id) {
        return todoService.toggleTodo(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // DELETE /api/todos/{id} - Delete todo
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTodo(@PathVariable Long id) {
        if (todoService.deleteTodo(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    // GET /api/todos/completed - Get completed todos
    @GetMapping("/completed")
    public ResponseEntity<List<Todo>> getCompletedTodos() {
        return ResponseEntity.ok(todoService.getTodosByStatus(true));
    }

    // GET /api/todos/pending - Get pending todos
    @GetMapping("/pending")
    public ResponseEntity<List<Todo>> getPendingTodos() {
        return ResponseEntity.ok(todoService.getTodosByStatus(false));
    }

    // GET /api/todos/search?title=xxx - Search todos
    @GetMapping("/search")
    public ResponseEntity<List<Todo>> searchTodos(@RequestParam String title) {
        return ResponseEntity.ok(todoService.searchTodos(title));
    }
}
