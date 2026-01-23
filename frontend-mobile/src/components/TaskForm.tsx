import { View, Text, TextInput, TouchableOpacity, StyleSheet } from 'react-native';
import type { TaskFormProps } from '../types';

function TaskForm({ form, onSubmit, onChange, submitting = false }: Readonly<TaskFormProps>) {
  return (
    <View style={styles.container}>
      <Text style={styles.title}>Adicionar Nova Tarefa</Text>
      
      <View style={styles.formGroup}>
        <Text style={styles.label}>Título</Text>
        <TextInput
          style={styles.input}
          placeholder="Digite o título da tarefa..."
          value={form.title}
          onChangeText={(text) => onChange('title', text)}
          editable={!submitting}
        />
      </View>
      
      <View style={styles.formGroup}>
        <Text style={styles.label}>Descrição</Text>
        <TextInput
          style={[styles.input, styles.textarea]}
          placeholder="Adicione uma descrição (opcional)..."
          value={form.description}
          onChangeText={(text) => onChange('description', text)}
          multiline
          numberOfLines={3}
          editable={!submitting}
        />
      </View>
      
      <TouchableOpacity
        style={[styles.button, (submitting || !form.title.trim()) && styles.buttonDisabled]}
        onPress={onSubmit}
        disabled={submitting || !form.title.trim()}
      >
        <Text style={styles.buttonText}>
          {submitting ? "Adicionando..." : "Adicionar Tarefa"}
        </Text>
      </TouchableOpacity>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    backgroundColor: '#fff',
    padding: 16,
    borderRadius: 8,
    marginBottom: 16,
    shadowColor: '#000',
    shadowOffset: { width: 0, height: 2 },
    shadowOpacity: 0.1,
    shadowRadius: 4,
    elevation: 3,
  },
  title: {
    fontSize: 18,
    fontWeight: 'bold',
    marginBottom: 16,
  },
  formGroup: {
    marginBottom: 16,
  },
  label: {
    fontSize: 14,
    fontWeight: '600',
    marginBottom: 8,
    color: '#333',
  },
  input: {
    borderWidth: 1,
    borderColor: '#ddd',
    borderRadius: 6,
    padding: 12,
    fontSize: 16,
  },
  textarea: {
    minHeight: 80,
    textAlignVertical: 'top',
  },
  button: {
    backgroundColor: '#3b82f6',
    padding: 14,
    borderRadius: 6,
    alignItems: 'center',
  },
  buttonDisabled: {
    backgroundColor: '#93c5fd',
  },
  buttonText: {
    color: '#fff',
    fontSize: 16,
    fontWeight: '600',
  },
});

export default TaskForm;