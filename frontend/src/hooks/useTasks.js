import { useState, useEffect } from 'react';
import { API_URL, MESSAGES } from '../utils/constants';

export function useTasks() {
  const [tasks, setTasks] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const [submitting, setSubmitting] = useState(false);

  // Função para carregar tarefas
  async function fetchTasks() {
    setLoading(true);
    setError(null);
    try {
      const response = await fetch(API_URL);
      if (!response.ok) throw new Error('Erro ao carregar tarefas');
      const data = await response.json();
      setTasks(data);
    } catch (err) {
      setError(MESSAGES.ERROR_LOAD);
      console.error('Erro:', err);
    } finally {
      setLoading(false);
    }
  }

  // Função para criar nova tarefa
  async function createTask(taskData) {
    if (!taskData.title.trim()) return false;
    
    setSubmitting(true);
    setError(null);
    try {
      const response = await fetch(API_URL, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ ...taskData, completed: false }),
      });
      if (!response.ok) throw new Error('Erro ao criar tarefa');
      
      await fetchTasks();
      return true;
    } catch (err) {
      setError(MESSAGES.ERROR_CREATE);
      console.error('Erro:', err);
      return false;
    } finally {
      setSubmitting(false);
    }
  }

  // Função para alternar status da tarefa
  async function toggleTask(id) {
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
  async function deleteTask(id) {
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
    toggleTask,
    deleteTask,
    fetchTasks
  };
}
