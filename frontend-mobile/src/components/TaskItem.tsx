import { View, Text, TouchableOpacity, StyleSheet } from 'react-native';
import type { TaskItemProps } from '../types';

function TaskItem({ task, onToggle, onDelete }: TaskItemProps) {
  return (
    <View style={[styles.container, task.completed && styles.completed]}>
      <View style={styles.content}>
        <Text style={[styles.title, task.completed && styles.titleCompleted]}>
          {task.title}
        </Text>
        {task.description && (
          <Text style={[styles.description, task.completed && styles.descriptionCompleted]}>
            {task.description}
          </Text>
        )}
      </View>
      
      <View style={styles.actions}>
        <TouchableOpacity
          onPress={() => onToggle(task.id)}
          style={[styles.button, task.completed ? styles.undoButton : styles.completeButton]}
        >
          <Text style={styles.buttonText}>
            {task.completed ? "Desfazer" : "Concluir"}
          </Text>
        </TouchableOpacity>
        
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

const styles = StyleSheet.create({
  container: {
    backgroundColor: '#fff',
    padding: 16,
    borderRadius: 8,
    marginBottom: 12,
    borderLeftWidth: 4,
    borderLeftColor: '#3b82f6',
  },
  completed: {
    borderLeftColor: '#10b981',
    opacity: 0.7,
  },
  content: {
    marginBottom: 12,
  },
  title: {
    fontSize: 16,
    fontWeight: '600',
    color: '#111',
  },
  titleCompleted: {
    textDecorationLine: 'line-through',
    color: '#6b7280',
  },
  description: {
    fontSize: 14,
    color: '#6b7280',
    marginTop: 4,
  },
  descriptionCompleted: {
    textDecorationLine: 'line-through',
  },
  actions: {
    flexDirection: 'row',
    gap: 8,
  },
  button: {
    flex: 1,
    padding: 10,
    borderRadius: 6,
    alignItems: 'center',
  },
  completeButton: {
    backgroundColor: '#10b981',
  },
  undoButton: {
    backgroundColor: '#f59e0b',
  },
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