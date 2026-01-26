import { useState } from 'react';
import { View, Text, TextInput, TouchableOpacity, StyleSheet } from 'react-native';
import type { TaskItemProps, TaskFormData } from '../types';

function TaskItem({ task, onToggle, onDelete, onEdit }: Readonly<TaskItemProps>) {
  const [isEditing, setIsEditing] = useState(false);
  const [editForm, setEditForm] = useState<TaskFormData>({
    title: task.title,
    description: task.description
  });
  const [editError, setEditError] = useState<string>('');

  function handleEdit() {
    setIsEditing(true);
    setEditError('');
  }

  function handleCancel() {
    setIsEditing(false);
    setEditForm({ title: task.title, description: task.description });
    setEditError('');
  }

  function handleSave() {
    if (!editForm.title.trim()) {
      setEditError('O título da tarefa é obrigatório.');
      return;
    }
    
    setEditError('');
    onEdit(task.id, editForm);
    setIsEditing(false);
  }

  // Modo de edição
  if (isEditing) {
    return (
      <View style={styles.container}>
        <View style={styles.editForm}>
          {/* Mensagem de erro */}
          {editError ? (
            <View style={styles.errorContainer}>
              <Text style={styles.errorText}>{editError}</Text>
            </View>
          ) : null}
          
          {/* Campo de título */}
          <TextInput
            style={styles.editInput}
            value={editForm.title}
            onChangeText={(text) => setEditForm({ ...editForm, title: text })}
            placeholder="Título da tarefa"
            placeholderTextColor="#9ca3af"
          />
          
          {/* Campo de descrição */}
          <TextInput
            style={styles.editTextarea}
            value={editForm.description}
            onChangeText={(text) => setEditForm({ ...editForm, description: text })}
            placeholder="Descrição (opcional)"
            placeholderTextColor="#9ca3af"
            multiline
            numberOfLines={3}
          />
          
          {/* Botões de ação da edição */}
          <View style={styles.editActions}>
            <TouchableOpacity
              onPress={handleSave}
              style={[styles.button, styles.saveButton]}
            >
              <Text style={styles.buttonText}>Salvar</Text>
            </TouchableOpacity>
            <TouchableOpacity
              onPress={handleCancel}
              style={[styles.button, styles.cancelButton]}
            >
              <Text style={styles.buttonText}>Cancelar</Text>
            </TouchableOpacity>
          </View>
        </View>
      </View>
    );
  }

  // Modo de visualização
  return (
    <View style={[styles.container, task.completed && styles.completed]}>
      {/* Área de conteúdo da tarefa */}
      <View style={styles.content}>
        {/* Título com estilo riscado se completado */}
        <Text style={[styles.title, task.completed && styles.titleCompleted]}>
          {task.title}
        </Text>
        {/* Descrição opcional */}
        {task.description ? (
          <Text style={[styles.description, task.completed && styles.descriptionCompleted]}>
            {task.description}
          </Text>
        ) : null }
      </View>
      
      {/* Área de botões de ação */}
      <View style={styles.actions}>
        {/* Botão para editar */}
        <TouchableOpacity
          onPress={handleEdit}
          style={[styles.button, styles.editButton]}
        >
          <Text style={styles.buttonText}>Editar</Text>
        </TouchableOpacity>
        
        {/* Botão para concluir ou desfazer */}
        <TouchableOpacity
          onPress={() => onToggle(task.id)}
          style={[styles.button, task.completed ? styles.undoButton : styles.completeButton]}
        >
          <Text style={styles.buttonText}>
            {task.completed ? "Desfazer" : "Concluir"}
          </Text>
        </TouchableOpacity>
        
        {/* Botão para remover tarefa */}
        <TouchableOpacity
          onPress={() => onDelete(task.id)}
          style={[styles.button, styles.deleteButton]}
        >
          <Text style={styles.buttonText}>Remover</Text>
        </TouchableOpacity>
      </View>
    </View>
  );
}

// Estilos do componente
const styles = StyleSheet.create({
  // Card da tarefa com borda colorida à esquerda
  container: {
    backgroundColor: '#fff',
    padding: 16,
    borderRadius: 8,
    marginBottom: 12,
    borderLeftWidth: 4,
    borderLeftColor: '#3b82f6',
  },
  // Estilo quando tarefa está concluída
  completed: {
    borderLeftColor: '#10b981',
    opacity: 0.7,
  },
  // Container do conteúdo textual
  content: {
    marginBottom: 12,
  },
  // Título da tarefa
  title: {
    fontSize: 16,
    fontWeight: '600',
    color: '#111',
  },
  // Título riscado quando completado
  titleCompleted: {
    textDecorationLine: 'line-through',
    color: '#6b7280',
  },
  // Descrição opcional
  description: {
    fontSize: 14,
    color: '#6b7280',
    marginTop: 4,
  },
  descriptionCompleted: {
    textDecorationLine: 'line-through',
  },
  // Container dos botões de ação
  actions: {
    flexDirection: 'row',
    gap: 8,
  },
  // Estilo base dos botões
  button: {
    flex: 1,
    padding: 10,
    borderRadius: 6,
    alignItems: 'center',
  },
  // Botão azul para editar
  editButton: {
    backgroundColor: '#3b82f6',
  },
  // Botão verde para concluir
  completeButton: {
    backgroundColor: '#10b981',
  },
  // Botão laranja para desfazer
  undoButton: {
    backgroundColor: '#f59e0b',
  },
  // Botão vermelho para remover
  deleteButton: {
    backgroundColor: '#ef4444',
  },
  buttonText: {
    color: '#fff',
    fontSize: 14,
    fontWeight: '600',
  },
  // Estilos para o modo de edição
  editForm: {
    gap: 12,
  },
  errorContainer: {
    backgroundColor: '#fee2e2',
    padding: 8,
    borderRadius: 4,
    borderWidth: 1,
    borderColor: '#fecaca',
  },
  errorText: {
    color: '#ef4444',
    fontSize: 14,
  },
  editInput: {
    backgroundColor: '#f9fafb',
    borderWidth: 1,
    borderColor: '#d1d5db',
    borderRadius: 6,
    padding: 12,
    fontSize: 16,
    color: '#111',
  },
  editTextarea: {
    backgroundColor: '#f9fafb',
    borderWidth: 1,
    borderColor: '#d1d5db',
    borderRadius: 6,
    padding: 12,
    fontSize: 14,
    color: '#111',
    minHeight: 80,
    textAlignVertical: 'top',
  },
  editActions: {
    flexDirection: 'row',
    gap: 8,
  },
  saveButton: {
    backgroundColor: '#10b981',
  },
  cancelButton: {
    backgroundColor: '#6b7280',
  },
});

export default TaskItem;