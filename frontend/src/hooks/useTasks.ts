import { useState, useEffect } from 'react';
import { API_URL, MESSAGES } from '../utils/constants';
import type { Task, TaskFormData, UseTasksReturn } from '../types';

export function useTasks(): UseTasksReturn {
  const [tasks, setTasks] = useState<Task[]>([]);
  const [loading, setLoading] = useState<boolean>(false);
  const [error, setError] = useState<string | null>(null);
  const [submitting, setSubmitting] = useState<boolean>(false);

  // Função para carregar tarefas
  async function fetchTasks(): Promise<void> { 
    setLoading(true);
    setError(null);
    try {
      const response = await fetch(API_URL);
      if (!response.ok) throw new Error('Erro ao carregar tarefas');
      const data: Task[] = await response.json();
      setTasks(data);
    } catch (err) {
      setError(MESSAGES.ERROR_LOAD);
      console.error('Erro:', err);
    } finally {
      setLoading(false);
    }
  }

  // Função para criar nova tarefa
  async function createTask(taskData: TaskFormData): Promise<boolean> {
    if (!taskData.title.trim()) {
      setError(MESSAGES.ERROR_EMPTY_TITLE);
      return false;
    }
    
    setSubmitting(true);
    setError(null);
    try {
      const response = await fetch(API_URL, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ ...taskData, completed: false }),
      });
      
      if (!response.ok) {
        const errorData = await response.json();
        setError(errorData.error || MESSAGES.ERROR_CREATE);
        return false;
      }
      
      await fetchTasks();
      return true;
    } catch (err) {
      setError(MESSAGES.ERROR_CONNECTION);
      console.error('Erro:', err);
      return false;
    } finally {
      setSubmitting(false);
    }
  }

  // Função para atualizar tarefa existente
  async function updateTask(id: number, taskData: TaskFormData): Promise<boolean> {
    if (!taskData.title.trim()) {
      setError(MESSAGES.ERROR_EMPTY_TITLE);
      return false;
    }
    
    setSubmitting(true);
    setError(null);
    try {
      const response = await fetch(`${API_URL}/${id}`, {
        method: "PUT",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(taskData),
      });
      
      if (!response.ok) {
        const errorData = await response.json();
        setError(errorData.error || MESSAGES.ERROR_UPDATE);
        return false;
      }
      
      await fetchTasks();
      return true;
    } catch (err) {
      setError(MESSAGES.ERROR_CONNECTION);
      console.error('Erro:', err);
      return false;
    } finally {
      setSubmitting(false);
    }
  }

  // Função para alternar status da tarefa
  async function toggleTask(id: number): Promise<void> {
    try {
      const response = await fetch(`${API_URL}/${id}/toggle`, { method: "PATCH" });
      if (!response.ok) throw new Error('Erro ao atualizar tarefa');
      await fetchTasks();
    } catch (err) {
      setError(MESSAGES.ERROR_UPDATE);
      console.error('Erro:', err);
    }
  }

  // Função para deletar tarefa
  async function deleteTask(id: number): Promise<void> {
    try {
      const response = await fetch(`${API_URL}/${id}`, { method: "DELETE" });
      if (!response.ok) throw new Error('Erro ao deletar tarefa');
      await fetchTasks();
    } catch (err) {
      setError(MESSAGES.ERROR_DELETE);
      console.error('Erro:', err);
    }
  }

  // Carregar tarefas ao inicializar
  useEffect(() => {
    fetchTasks();
  }, []);

  return {
    tasks,
    loading,
    error,
    submitting,
    createTask,
    updateTask,
    toggleTask,
    deleteTask,
    fetchTasks
  };
}