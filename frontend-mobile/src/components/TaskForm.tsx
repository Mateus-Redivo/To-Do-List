/**
 * Componente de formulário de tarefas
 * Permite criar novas tarefas com título e descrição
 */

import { View, Text, TextInput, TouchableOpacity, StyleSheet } from 'react-native';
import type { TaskFormProps } from '../types';

function TaskForm({ form, onSubmit, onChange, submitting = false }: Readonly<TaskFormProps>) {
  return (
    // Container principal do formulário com sombra
    <View style={styles.container}>
      <Text style={styles.title}>Adicionar Nova Tarefa</Text>
      
      {/* Campo de título (obrigatório) */}
      <View style={styles.formGroup}>
        <Text style={styles.label}>Título</Text>
        <TextInput
          style={styles.input}
          placeholder="Digite o título da tarefa..."
          value={form.title}
          onChangeText={(text) => onChange('title', text)} // Atualiza estado
          editable={!submitting} // Desabilita durante envio
        />
      </View>
      
      {/* Campo de descrição (opcional) */}
      <View style={styles.formGroup}>
        <Text style={styles.label}>Descrição</Text>
        <TextInput
          style={[styles.input, styles.textarea]}
          placeholder="Adicione uma descrição (opcional)..."
          value={form.description}
          onChangeText={(text) => onChange('description', text)}
          multiline // Permite múltiplas linhas
          numberOfLines={3} // Altura inicial
          editable={!submitting} // Desabilita durante envio
        />
      </View>
      
      {/* Botão de envio - desabilitado se título vazio ou enviando */}
      <TouchableOpacity
        style={[styles.button, (submitting || !form.title.trim()) && styles.buttonDisabled]}
        onPress={onSubmit}
        disabled={submitting || !form.title.trim()} // Validação
      >
        <Text style={styles.buttonText}>
          {submitting ? "Adicionando..." : "Adicionar Tarefa"}
        </Text>
      </TouchableOpacity>
    </View>
  );
}

// Estilos do componente
const styles = StyleSheet.create({
  // Card branco com sombra
  container: {
    backgroundColor: '#fff',
    padding: 16,
    borderRadius: 8,
    marginBottom: 16,
    shadowColor: '#000',
    shadowOffset: { width: 0, height: 2 },
    shadowOpacity: 0.1,
    shadowRadius: 4,
    elevation: 3, // Sombra no Android
  },
  title: {
    fontSize: 18,
    fontWeight: 'bold',
    marginBottom: 16,
  },
  formGroup: {
    marginBottom: 16,
  },
  // Label dos campos
  label: {
    fontSize: 14,
    fontWeight: '600',
    marginBottom: 8,
    color: '#333',
  },
  // Campo de entrada padrão
  input: {
    borderWidth: 1,
    borderColor: '#ddd',
    borderRadius: 6,
    padding: 12,
    fontSize: 16,
  },
  // Estilo adicional para área de texto
  textarea: {
    minHeight: 80,
    textAlignVertical: 'top', // Alinha texto no topo
  },
  // Botão principal azul
  button: {
    backgroundColor: '#3b82f6',
    padding: 14,
    borderRadius: 6,
    alignItems: 'center',
  },
  // Botão desabilitado (mais claro)
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