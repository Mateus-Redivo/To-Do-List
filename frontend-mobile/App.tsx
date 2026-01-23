import { SafeAreaView, ScrollView, StyleSheet, StatusBar } from 'react-native';
import { useState } from 'react';
import TaskHeader from './src/components/TaskHeader';
import TaskForm from './src/components/TaskForm';
import TaskList from './src/components/TaskList';
import ErrorMessage from './src/components/ErrorMessage';
import { useTasks } from './src/hooks/useTasks';
import type { TaskFormData } from './src/types';

export default function App() {
  const [form, setForm] = useState<TaskFormData>({ title: "", description: "" });
  const { tasks, loading, error, submitting, createTask, toggleTask, deleteTask } = useTasks();

  function handleChange(field: string, value: string) {
    setForm({ ...form, [field]: value });
  }

  async function handleSubmit() {
    const success = await createTask(form);
    if (success) {
      setForm({ title: "", description: "" });
    }
  }

  return (
    <SafeAreaView style={styles.container}>
      <StatusBar barStyle="dark-content" />
      <ScrollView style={styles.scrollView}>
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
      </ScrollView>
    </SafeAreaView>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#f3f4f6',
  },
  scrollView: {
    flex: 1,
    padding: 16,
  },
});