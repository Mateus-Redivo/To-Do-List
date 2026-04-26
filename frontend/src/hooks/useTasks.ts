import { useState, useEffect } from 'react';
import { API_URL, MESSAGES } from '../utils/constants';
import type { Task, TaskFormData, UseTasksReturn } from '../types';

export function useTasks(): UseTasksReturn {
  const [tasks, setTasks] = useState<Task[]>([]);
  const [loading, setLoading] = useState<boolean>(false);
  const [error, setError] = useState<string | null>(null);
  const [submitting, setSubmitting] = useState<boolean>(false);

  function clearError() {
    setError(null);
  }

  async function fetchTasks(): Promise<void> {
    setLoading(true);
    setError(null);
    try {
      const response = await fetch(API_URL);
      if (!response.ok) throw new Error();
      const data: Task[] = await response.json();
      setTasks(data);
    } catch {
      setError(MESSAGES.ERROR_LOAD);
    } finally {
      setLoading(false);
    }
  }

  async function createTask(taskData: TaskFormData): Promise<boolean> {
    if (!taskData.title.trim()) {
      setError(MESSAGES.ERROR_EMPTY_TITLE);
      return false;
    }

    setSubmitting(true);
    setError(null);
    try {
      const response = await fetch(API_URL, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ ...taskData, completed: false }),
      });

      if (!response.ok) {
        let message = MESSAGES.ERROR_CREATE;
        try {
          const errorData = await response.json();
          if (errorData.error) message = errorData.error;
        } catch { /* use default message */ }
        setError(message);
        return false;
      }

      await fetchTasks();
      return true;
    } catch {
      setError(MESSAGES.ERROR_CONNECTION);
      return false;
    } finally {
      setSubmitting(false);
    }
  }

  async function updateTask(id: number, taskData: TaskFormData): Promise<boolean> {
    if (!taskData.title.trim()) {
      setError(MESSAGES.ERROR_EMPTY_TITLE);
      return false;
    }

    setSubmitting(true);
    setError(null);
    try {
      const response = await fetch(`${API_URL}/${id}`, {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(taskData),
      });

      if (!response.ok) {
        let message = MESSAGES.ERROR_UPDATE;
        try {
          const errorData = await response.json();
          if (errorData.error) message = errorData.error;
        } catch { /* use default message */ }
        setError(message);
        return false;
      }

      await fetchTasks();
      return true;
    } catch {
      setError(MESSAGES.ERROR_CONNECTION);
      return false;
    } finally {
      setSubmitting(false);
    }
  }

  async function toggleTask(id: number): Promise<void> {
    try {
      const response = await fetch(`${API_URL}/${id}/toggle`, { method: 'PATCH' });
      if (!response.ok) throw new Error();
      await fetchTasks();
    } catch {
      setError(MESSAGES.ERROR_UPDATE);
    }
  }

  async function deleteTask(id: number): Promise<void> {
    try {
      const response = await fetch(`${API_URL}/${id}`, { method: 'DELETE' });
      if (!response.ok) throw new Error();
      await fetchTasks();
    } catch {
      setError(MESSAGES.ERROR_DELETE);
    }
  }

  useEffect(() => {
    fetchTasks();
  }, []);

  return {
    tasks,
    loading,
    error,
    submitting,
    clearError,
    createTask,
    updateTask,
    toggleTask,
    deleteTask,
    fetchTasks,
  };
}
