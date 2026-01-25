import { useState } from "react";
import "./TaskApp.css";

// Componentes
import TaskHeader from './TaskHeader';
import TaskForm from './TaskForm';
import TaskList from './TaskList';
import TaskFooter from './TaskFooter';
import ErrorMessage from './ErrorMessage';

// Hook personalizado
import { useTasks } from '../hooks/useTasks';

// Types
import type { TaskFormData } from '../types';

function TaskApp() {
  const [form, setForm] = useState<TaskFormData>({ title: "", description: "" });
  const { 
    tasks, 
    loading, 
    error, 
    submitting, 
    createTask, 
    toggleTask, 
    deleteTask 
  } = useTasks();

  function handleChange(e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) {
    setForm({ ...form, [e.target.name]: e.target.value });
  }

  async function handleSubmit(e: React.FormEvent<HTMLFormElement>) {
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