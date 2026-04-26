import { useState } from "react";
import "./TaskApp.css";

import TaskHeader from './TaskHeader';
import TaskForm from './TaskForm';
import TaskList from './TaskList';
import TaskFooter from './TaskFooter';
import ErrorMessage from './ErrorMessage';

import { useTasks } from '../hooks/useTasks';

import type { TaskFormData } from '../types';

function TaskApp() {
  const [form, setForm] = useState<TaskFormData>({ title: "", description: "" });
  const {
    tasks,
    loading,
    error,
    submitting,
    clearError,
    createTask,
    updateTask,
    toggleTask,
    deleteTask,
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

  async function handleEdit(id: number, taskData: TaskFormData): Promise<boolean> {
    return updateTask(id, taskData);
  }

  return (
    <div className="task-app">
      <div className="task-app-content">
        <TaskHeader />

        {error && <ErrorMessage error={error} onDismiss={clearError} />}

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
          onEdit={handleEdit}
        />

        <TaskFooter />
      </div>
    </div>
  );
}

export default TaskApp;
