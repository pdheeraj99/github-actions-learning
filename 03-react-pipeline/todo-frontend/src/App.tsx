import { useState, useEffect } from 'react'
import './App.css'

// Backend API base URL
const API_URL = 'http://localhost:8080/api/todos'

interface Todo {
  id?: number
  title: string
  description?: string
  completed: boolean
  createdAt?: string
  updatedAt?: string
}

function App() {
  const [todos, setTodos] = useState<Todo[]>([])
  const [newTodo, setNewTodo] = useState('')
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState<string | null>(null)

  // Fetch todos on component mount
  useEffect(() => {
    fetchTodos()
  }, [])

  const fetchTodos = async () => {
    try {
      setLoading(true)
      setError(null)
      const response = await fetch(API_URL)
      if (!response.ok) throw new Error('Failed to fetch todos')
      const data = await response.json()
      setTodos(data)
    } catch (err) {
      setError('Failed to load todos. Make sure backend is running on port 8080.')
      console.error('Error fetching todos:', err)
    } finally {
      setLoading(false)
    }
  }

  const addTodo = async () => {
    if (newTodo.trim()) {
      try {
        setLoading(true)
        setError(null)
        const response = await fetch(API_URL, {
          method: 'POST',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify({
            title: newTodo,
            description: '',
            completed: false,
          }),
        })
        if (!response.ok) throw new Error('Failed to create todo')
        const createdTodo = await response.json()
        setTodos([...todos, createdTodo])
        setNewTodo('')
      } catch (err) {
        setError('Failed to add todo')
        console.error('Error adding todo:', err)
      } finally {
        setLoading(false)
      }
    }
  }

  const toggleTodo = async (id: number) => {
    try {
      setLoading(true)
      setError(null)
      const response = await fetch(`${API_URL}/${id}/toggle`, {
        method: 'PATCH',
      })
      if (!response.ok) throw new Error('Failed to toggle todo')
      const updatedTodo = await response.json()
      setTodos(todos.map((todo) => (todo.id === id ? updatedTodo : todo)))
    } catch (err) {
      setError('Failed to update todo')
      console.error('Error toggling todo:', err)
    } finally {
      setLoading(false)
    }
  }

  const deleteTodo = async (id: number) => {
    try {
      setLoading(true)
      setError(null)
      const response = await fetch(`${API_URL}/${id}`, {
        method: 'DELETE',
      })
      if (!response.ok) throw new Error('Failed to delete todo')
      setTodos(todos.filter((todo) => todo.id !== id))
    } catch (err) {
      setError('Failed to delete todo')
      console.error('Error deleting todo:', err)
    } finally {
      setLoading(false)
    }
  }

  const completedCount = todos.filter((t) => t.completed).length
  const totalCount = todos.length

  return (
    <div className="app">
      <div className="container">
        <header className="header">
          <h1>📋 Todo Master</h1>
          <p className="subtitle">Full-Stack App: React + Spring Boot + H2</p>
          <div className="stats">
            <span className="stat completed">{completedCount} done</span>
            <span className="stat total">{totalCount - completedCount} remaining</span>
          </div>
        </header>

        {/* Error Display */}
        {error && (
          <div style={{
            background: '#fee',
            border: '1px solid #fcc',
            padding: '12px',
            borderRadius: '8px',
            marginBottom: '16px',
            color: '#c33'
          }}>
            ⚠️ {error}
          </div>
        )}

        <div className="input-section">
          <input
            type="text"
            value={newTodo}
            onChange={(e) => setNewTodo(e.target.value)}
            onKeyPress={(e) => e.key === 'Enter' && addTodo()}
            placeholder="What's your next task? 🎯"
            className="todo-input"
            disabled={loading}
          />
          <button onClick={addTodo} className="add-btn" disabled={loading}>
            {loading ? '⏳' : 'Add Task'}
          </button>
        </div>

        {loading && todos.length === 0 && (
          <div style={{ textAlign: 'center', padding: '20px', color: '#666' }}>
            Loading todos from Spring Boot backend...
          </div>
        )}

        <ul className="todo-list">
          {todos.map((todo) => (
            <li
              key={todo.id}
              className={`todo-item ${todo.completed ? 'completed' : ''}`}
            >
              <div className="todo-content" onClick={() => toggleTodo(todo.id!)}>
                <span className="checkbox">
                  {todo.completed ? '✅' : '⬜'}
                </span>
                <span className="todo-text">{todo.title}</span>
              </div>
              <button
                className="delete-btn"
                onClick={() => deleteTodo(todo.id!)}
                disabled={loading}
              >
                🗑️
              </button>
            </li>
          ))}
        </ul>

        {todos.length === 0 && !loading && (
          <div className="empty-state">
            <p>🎉 No tasks yet! Add your first task above.</p>
            <p style={{ fontSize: '14px', color: '#666' }}>
              Data is saved in H2 database (in-memory)
            </p>
          </div>
        )}

        <footer className="footer">
          <p>✅ Connected to Spring Boot Backend (Port 8080)</p>
          <p className="tech-stack">
            React + TypeScript + Spring Boot + H2 Database
          </p>
          <p className="tech-stack">
            🐳 Docker Ready | ☸️ K8s Ready | 🔄 CI/CD Ready
          </p>
        </footer>
      </div>
    </div>
  )
}

export default App
