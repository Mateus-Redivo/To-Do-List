/**
 * Componente principal da aplicação
 * Integra todos os componentes e gerencia o estado global
 */

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
  // Estado do formulário
  const [form, setForm] = useState<TaskFormData>({ title: "", description: "" });
  
  // Hook de gerenciamento de tarefas
  const { 
    tasks, 
    loading, 
    error, 
    submitting, 
    createTask, 
    updateTask, 
    toggleTask, 
    deleteTask 
  } = useTasks();

  // Atualiza campos do formulário
  function handleChange(field: string, value: string) {
    setForm({ ...form, [field]: value });
  }

  // Envia nova tarefa e limpa formulário
  async function handleSubmit() {
    const success = await createTask(form);
    if (success) {
      setForm({ title: "", description: "" });
    }
  }

  return (
    <div className="task-app">
      <div className="task-app-content">
        {/* Cabeçalho da aplicação */}
        <TaskHeader />
        
        {/* Exibe mensagem de erro se houver */}
        {error && <ErrorMessage error={error} />}
        
        {/* Formulário para criar novas tarefas */}
        <TaskForm 
          form={form}
          onSubmit={handleSubmit}
          onChange={handleChange}
          submitting={submitting}
        />
        
        {/* Lista de todas as tarefas */}
        <TaskList 
          tasks={tasks}
          loading={loading}
          onToggle={toggleTask}
          onDelete={deleteTask}
          onEdit={updateTask}
        />
        
        {/* Rodapé da aplicação */}
        <TaskFooter />
      </div>
    </div>
  );
}

export default TaskApp;