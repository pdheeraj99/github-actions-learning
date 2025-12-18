import { useState } from 'react'
import './App.css'

interface Todo {
  id: number
  text: string
  completed: boolean
  createdAt: Date
}

function App() {
  const [todos, setTodos] = useState<Todo[]>([
    { id: 1, text: 'Learn GitHub Actions 🚀', completed: false, createdAt: new Date() },
    { id: 2, text: 'Build Docker Image 🐳', completed: false, createdAt: new Date() },
    { id: 3, text: 'Deploy to Kubernetes ☸️', completed: false, createdAt: new Date() },
  ])
  const [newTodo, setNewTodo] = useState('')

  const addTodo = () => {
    if (newTodo.trim()) {
      setTodos([
        ...todos,
        {
          id: Date.now(),
          text: newTodo,
          completed: false,
          createdAt: new Date(),
        },
      ])
      setNewTodo('')
    }
  }

  const toggleTodo = (id: number) => {
    setTodos(
      todos.map((todo) =>
        todo.id === id ? { ...todo, completed: !todo.completed } : todo
      )
    )
  }

  const deleteTodo = (id: number) => {
    setTodos(todos.filter((todo) => todo.id !== id))
  }

  const completedCount = todos.filter((t) => t.completed).length
  const totalCount = todos.length

  return (
    <div className="app">
      <div className="container">
        <header className="header">
          <h1>📋 Todo Master</h1>
          <p className="subtitle">Organize your DevOps learning journey</p>
          <div className="stats">
            <span className="stat completed">{completedCount} done</span>
            <span className="stat total">{totalCount - completedCount} remaining</span>
          </div>
        </header>

        <div className="input-section">
          <input
            type="text"
            value={newTodo}
            onChange={(e) => setNewTodo(e.target.value)}
            onKeyPress={(e) => e.key === 'Enter' && addTodo()}
            placeholder="What's your next task? 🎯"
            className="todo-input"
          />
          <button onClick={addTodo} className="add-btn">
            Add Task
          </button>
        </div>

        <ul className="todo-list">
          {todos.map((todo) => (
            <li
              key={todo.id}
              className={`todo-item ${todo.completed ? 'completed' : ''}`}
            >
              <div className="todo-content" onClick={() => toggleTodo(todo.id)}>
                <span className="checkbox">
                  {todo.completed ? '✅' : '⬜'}
                </span>
                <span className="todo-text">{todo.text}</span>
              </div>
              <button
                className="delete-btn"
                onClick={() => deleteTodo(todo.id)}
              >
                🗑️
              </button>
            </li>
          ))}
        </ul>

        {todos.length === 0 && (
          <div className="empty-state">
            <p>🎉 All tasks completed! Add more tasks above.</p>
          </div>
        )}

        <footer className="footer">
          <p>Built with React + TypeScript + Vite</p>
          <p className="tech-stack">🐳 Docker Ready | ☸️ K8s Ready | 🔄 CI/CD Ready</p>
        </footer>
      </div>
    </div>
  )
}

export default App
