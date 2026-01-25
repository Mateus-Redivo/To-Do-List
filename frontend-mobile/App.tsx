import { View, StyleSheet, StatusBar } from 'react-native';
import { SafeAreaProvider, SafeAreaView } from 'react-native-safe-area-context';
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
    <SafeAreaProvider>
      <SafeAreaView style={styles.container} edges={['top']}>
        <StatusBar barStyle="dark-content" />
        <TaskHeader />
        
        <View style={styles.content}>
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
        </View>
      </SafeAreaView>
    </SafeAreaProvider>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#f3f4f6',
  },
  content: {
    flex: 1,
    padding: 16,
  },
});