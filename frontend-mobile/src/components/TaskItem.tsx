import { View, Text, TouchableOpacity, StyleSheet } from 'react-native';
import type { TaskItemProps } from '../types';

function TaskItem({ task, onToggle, onDelete }: Readonly<TaskItemProps>) {
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
});

export default TaskItem;