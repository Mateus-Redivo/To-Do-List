/**
 * Componente principal da aplicação
 * Integra todos os componentes e gerencia o estado global
 */

import { useState } from "react";
import { View, StyleSheet } from 'react-native';
import { colors, spacing } from '../styles/theme';

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
  // ID da tarefa sendo editada (null = modo criação)
  const [editingTaskId, setEditingTaskId] = useState<number | null>(null);
  
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

  // Envia nova tarefa ou atualiza existente e limpa formulário
  async function handleSubmit() {
    let success = false;
    
    if (editingTaskId === null) {
      // Modo criação
      success = await createTask(form);
    } else {
      // Modo edição
      await updateTask(editingTaskId, form);
      success = true;
    }
    
    if (success) {
      setForm({ title: "", description: "" });
      setEditingTaskId(null);
    }
  }
  
  // Inicia modo de edição - preenche formulário com dados da tarefa
  function handleStartEdit(taskId: number) {
    const task = tasks.find(t => t.id === taskId);
    if (task) {
      setForm({ 
        title: task.title || "", 
        description: task.description || "" 
      });
      setEditingTaskId(taskId);
    }
  }
  
  // Cancela edição e limpa formulário
  function handleCancelEdit() {
    setForm({ title: "", description: "" });
    setEditingTaskId(null);
  }

  return (
    <View style={styles.container}>
      <View style={styles.content}>
        {/* Cabeçalho da aplicação */}
        <TaskHeader />
        
        {/* Exibe mensagem de erro se houver */}
        {error && <ErrorMessage error={error} />}
        
        {/* Formulário para criar/editar tarefas */}
        <TaskForm 
          form={form}
          onSubmit={handleSubmit}
          onChange={handleChange}
          submitting={submitting}
          editingTaskId={editingTaskId}
          onCancelEdit={handleCancelEdit}
        />
        
        {/* Lista de todas as tarefas */}
        <TaskList 
          tasks={tasks}
          loading={loading}
          onToggle={toggleTask}
          onDelete={deleteTask}
          onEdit={(id) => handleStartEdit(id)}
        />
        
        {/* Rodapé da aplicação */}
        <TaskFooter />
      </View>
    </View>
  );
}

// Estilos do componente
const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: colors.background,
  },
  content: {
    flex: 1,
    padding: spacing.lg,
  },
});

export default TaskApp;