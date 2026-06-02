import { useState, useEffect, useCallback } from "react";
import { MESSAGES } from "../utils/constants";
import {
  fetchAllTasks,
  createTaskRequest,
  updateTaskRequest,
  toggleTaskRequest,
  deleteTaskRequest,
} from "../services/taskApi";
import type { Task, TaskFormData, UseTasksReturn } from "../types";

export function useTasks(): UseTasksReturn {
  const [tasks, setTasks] = useState<Task[]>([]);
  const [loading, setLoading] = useState<boolean>(false);
  const [error, setError] = useState<string | null>(null);
  const [submitting, setSubmitting] = useState<boolean>(false);

  const clearError = useCallback(() => setError(null), []);

  const fetchTasks = useCallback(
    async (signal?: AbortSignal): Promise<void> => {
      setLoading(true);
      setError(null);
      try {
        const data = await fetchAllTasks(signal);
        setTasks(data);
      } catch (err) {
        if (err instanceof Error && err.name === "AbortError") return;
        setError(MESSAGES.ERROR_LOAD);
        console.error("Erro:", err);
      } finally {
        setLoading(false);
      }
    },
    [],
  );

  const createTask = useCallback(
    async (taskData: TaskFormData): Promise<boolean> => {
      if (!taskData.title.trim()) {
        setError(MESSAGES.ERROR_EMPTY_TITLE);
        return false;
      }
      setSubmitting(true);
      setError(null);
      try {
        const newTask = await createTaskRequest(taskData);
        setTasks((prev) => [...prev, newTask]);
        return true;
      } catch (err) {
        setError(err instanceof Error ? err.message : MESSAGES.ERROR_CREATE);
        console.error("Erro:", err);
        return false;
      } finally {
        setSubmitting(false);
      }
    },
    [],
  );

  const updateTask = useCallback(
    async (id: number, taskData: TaskFormData): Promise<boolean> => {
      if (!taskData.title.trim()) {
        setError(MESSAGES.ERROR_EMPTY_TITLE);
        return false;
      }
      setSubmitting(true);
      setError(null);
      try {
        const updated = await updateTaskRequest(id, taskData);
        setTasks((prev) => prev.map((t) => (t.id === id ? updated : t)));
        return true;
      } catch (err) {
        setError(err instanceof Error ? err.message : MESSAGES.ERROR_UPDATE);
        console.error("Erro:", err);
        return false;
      } finally {
        setSubmitting(false);
      }
    },
    [],
  );

  const toggleTask = useCallback(async (id: number): Promise<void> => {
    setSubmitting(true);
    setError(null);
    try {
      const updated = await toggleTaskRequest(id);
      setTasks((prev) => prev.map((t) => (t.id === id ? updated : t)));
    } catch (err) {
      setError(MESSAGES.ERROR_UPDATE);
      console.error("Erro:", err);
    } finally {
      setSubmitting(false);
    }
  }, []);

  const deleteTask = useCallback(async (id: number): Promise<void> => {
    setSubmitting(true);
    setError(null);
    try {
      await deleteTaskRequest(id);
      setTasks((prev) => prev.filter((t) => t.id !== id));
    } catch (err) {
      setError(MESSAGES.ERROR_DELETE);
      console.error("Erro:", err);
    } finally {
      setSubmitting(false);
    }
  }, []);

  useEffect(() => {
    const controller = new AbortController();
    fetchTasks(controller.signal);
    return () => controller.abort();
  }, [fetchTasks]);

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
