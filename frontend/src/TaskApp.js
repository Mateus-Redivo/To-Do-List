import React, { useState } from "react";
import "./TaskApp.css";

// Componentes
import TaskHeader from './components/TaskHeader';
import TaskForm from './components/TaskForm';
import TaskList from './components/TaskList';
import TaskFooter from './components/TaskFooter';
import ErrorMessage from './components/ErrorMessage';

// Hook personalizado
import { useTasks } from './hooks/useTasks';

function TaskApp() {
  const [form, setForm] = useState({ title: "", description: "" });
  const { 
    tasks, 
    loading, 
    error, 
    submitting, 
    createTask, 
    toggleTask, 
    deleteTask 
  } = useTasks();

  function handleChange(e) {
    setForm({ ...form, [e.target.name]: e.target.value });
  }

  async function handleSubmit(e) {
    e.preventDefault();
    const success = await createTask(form);
    if (success) {
      setForm({ title: "", description: "" });
    }
  }

  return (
    <div className="task-app">
      <div className="task-app-content">
        <TaskHeader />
        
        {error && <ErrorMessage error={error} />}
        
        <TaskForm 
          form={form}
          onSubmit={handleSubmit}
          onChange={handleChange}
          submitting={submitting}
        />
        
        <TaskList 
          tasks={tasks}
          loading={loading}
          onToggle={toggleTask}
          onDelete={deleteTask}
        />
        
        <TaskFooter />
      </div>
    </div>
  );
}

export default TaskApp;