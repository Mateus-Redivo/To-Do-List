/**
 * Hook customizado para gerenciamento de tarefas
 * Gerencia estado, requisições à API e operações CRUD
 */

import { useState, useEffect } from 'react';
import { API_URL, MESSAGES } from '../utils/constants';
import type { Task, TaskFormData, UseTasksReturn } from '../types';

export function useTasks(): UseTasksReturn {
  // Estados do hook
  const [tasks, setTasks] = useState<Task[]>([]);
  const [loading, setLoading] = useState<boolean>(false);
  const [error, setError] = useState<string | null>(null);
  const [submitting, setSubmitting] = useState<boolean>(false);

  // Busca todas as tarefas da API
  async function fetchTasks(): Promise<void> { 
    setLoading(true); // Inicia loading
    setError(null); // Limpa erros anteriores
    try {
      const response = await fetch(API_URL); // GET request
      if (!response.ok) throw new Error('Erro ao carregar tarefas');
      const data: Task[] = await response.json(); // Parse JSON
      setTasks(data); // Atualiza estado com tarefas
    } catch (err) {
      setError(MESSAGES.ERROR_LOAD); // Define mensagem de erro
      console.error('Erro:', err); // Log para debug
    } finally {
      setLoading(false); // Finaliza loading
    }
  }

  // Cria uma nova tarefa
  async function createTask(taskData: TaskFormData): Promise<boolean> {
    if (!taskData.title.trim()) {
      setError(MESSAGES.ERROR_EMPTY_TITLE);
      return false;
    }
    
    setSubmitting(true); // Indica envio em andamento
    setError(null);
    try {
      // Envia POST request para criar tarefa
      const response = await fetch(API_URL, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ ...taskData, completed: false }), // Nova tarefa não concluída
      });
      if (!response.ok) throw new Error('Erro ao criar tarefa');
      
      await fetchTasks(); // Recarrega lista atualizada
      return true; // Sucesso
    } catch (err) {
      setError(MESSAGES.ERROR_CREATE);
      console.error('Erro:', err);
      return false;
    } finally {
      setSubmitting(false);
    }
  }

  // Alterna o status de conclusão da tarefa
  async function toggleTask(id: number): Promise<void> {
    try {
      // PATCH request para alternar status
      const response = await fetch(`${API_URL}/${id}/toggle`, { method: "PATCH" });
      if (!response.ok) throw new Error('Erro ao atualizar tarefa');
      await fetchTasks(); // Recarrega lista
    } catch (err) {
      setError(MESSAGES.ERROR_UPDATE);
      console.error('Erro:', err);
    }
  }

  // Remove uma tarefa
  async function deleteTask(id: number): Promise<void> {
    try {
      // DELETE request para remover tarefa
      const response = await fetch(`${API_URL}/${id}`, { method: "DELETE" });
      if (!response.ok) throw new Error('Erro ao deletar tarefa');
      await fetchTasks(); // Recarrega lista
    } catch (err) {
      setError(MESSAGES.ERROR_DELETE);
      console.error('Erro:', err);
    }
  }

  // Carrega as tarefas quando o componente é montado
  useEffect(() => {
    fetchTasks(); // Executa apenas uma vez na montagem
  }, []); // Array vazio = executa só na montagem

  // Retorna estados e funções para os componentes
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