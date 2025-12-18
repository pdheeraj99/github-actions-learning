package com.example.todo.controller;

import com.example.todo.entity.Todo;
import com.example.todo.service.TodoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Todo Controller Tests
 * 
 * Unit tests for REST API endpoints.
 * Uses MockMvc to test controller layer in isolation.
 */
@WebMvcTest(TodoController.class)
class TodoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TodoService todoService;

    private Todo sampleTodo;

    @BeforeEach
    void setUp() {
        sampleTodo = new Todo();
        sampleTodo.setId(1L);
        sampleTodo.setTitle("Test Todo");
        sampleTodo.setDescription("Test Description");
        sampleTodo.setCompleted(false);
        sampleTodo.setCreatedAt(LocalDateTime.now());
        sampleTodo.setUpdatedAt(LocalDateTime.now());
    }

    @Test
    @DisplayName("GET /api/todos - Should return all todos")
    void getAllTodos_ShouldReturnTodoList() throws Exception {
        when(todoService.getAllTodos()).thenReturn(Arrays.asList(sampleTodo));

        mockMvc.perform(get("/api/todos"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].title").value("Test Todo"));
    }

    @Test
    @DisplayName("GET /api/todos/{id} - Should return todo by ID")
    void getTodoById_ShouldReturnTodo() throws Exception {
        when(todoService.getTodoById(1L)).thenReturn(Optional.of(sampleTodo));

        mockMvc.perform(get("/api/todos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Test Todo"));
    }

    @Test
    @DisplayName("GET /api/todos/{id} - Should return 404 when not found")
    void getTodoById_NotFound_ShouldReturn404() throws Exception {
        when(todoService.getTodoById(999L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/todos/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("POST /api/todos - Should create new todo")
    void createTodo_ShouldCreateAndReturnTodo() throws Exception {
        Todo newTodo = new Todo();
        newTodo.setTitle("New Todo");
        newTodo.setDescription("New Description");

        when(todoService.createTodo(any(Todo.class))).thenReturn(sampleTodo);

        mockMvc.perform(post("/api/todos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newTodo)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    @DisplayName("POST /api/todos - Should return 400 for invalid input")
    void createTodo_InvalidInput_ShouldReturn400() throws Exception {
        Todo invalidTodo = new Todo();
        invalidTodo.setTitle(""); // Empty title - validation should fail

        mockMvc.perform(post("/api/todos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidTodo)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("PUT /api/todos/{id} - Should update todo")
    void updateTodo_ShouldUpdateAndReturnTodo() throws Exception {
        Todo updatedTodo = new Todo();
        updatedTodo.setTitle("Updated Title");
        updatedTodo.setDescription("Updated Description");

        when(todoService.updateTodo(eq(1L), any(Todo.class))).thenReturn(Optional.of(sampleTodo));

        mockMvc.perform(put("/api/todos/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedTodo)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("DELETE /api/todos/{id} - Should delete todo")
    void deleteTodo_ShouldReturn204() throws Exception {
        when(todoService.deleteTodo(1L)).thenReturn(true);

        mockMvc.perform(delete("/api/todos/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("DELETE /api/todos/{id} - Should return 404 when not found")
    void deleteTodo_NotFound_ShouldReturn404() throws Exception {
        when(todoService.deleteTodo(999L)).thenReturn(false);

        mockMvc.perform(delete("/api/todos/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("PATCH /api/todos/{id}/toggle - Should toggle completion")
    void toggleTodo_ShouldToggleAndReturn() throws Exception {
        sampleTodo.setCompleted(true);
        when(todoService.toggleTodo(1L)).thenReturn(Optional.of(sampleTodo));

        mockMvc.perform(patch("/api/todos/1/toggle"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.completed").value(true));
    }
}
