import { View, Text, FlatList, ActivityIndicator, StyleSheet } from 'react-native';
import TaskItem from './TaskItem';
import { MESSAGES } from '../utils/constants';
import type { TaskListProps } from '../types';

function TaskList({ tasks, loading, onToggle, onDelete }: Readonly<TaskListProps>) {
  const completedCount = tasks.filter(task => task.completed).length;

  if (loading) {
    return (
      <View style={styles.loadingContainer}>
        <ActivityIndicator size="large" color="#3b82f6" />
        <Text style={styles.loadingText}>{MESSAGES.LOADING}</Text>
      </View>
    );
  }

  const ListHeader = () => (
    <View style={styles.header}>
      <Text style={styles.title}>Suas Tarefas ({tasks.length})</Text>
      {tasks.length > 0 && (
        <Text style={styles.count}>{completedCount} concluídas</Text>
      )}
    </View>
  );

  const ListEmpty = () => (
    <View style={styles.emptyState}>
      <Text style={styles.emptyTitle}>{MESSAGES.EMPTY_TITLE}</Text>
      <Text style={styles.emptyDescription}>
        {MESSAGES.EMPTY_DESCRIPTION}
      </Text>
    </View>
  );

  return (
    <FlatList
      style={styles.container}
      data={tasks}
      keyExtractor={(item) => item.id.toString()}
      renderItem={({ item }) => (
        <TaskItem
          task={item}
          onToggle={onToggle}
          onDelete={onDelete}
        />
      )}
      ListHeaderComponent={ListHeader}
      ListEmptyComponent={ListEmpty}
      showsVerticalScrollIndicator={false}
    />
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
  },
  header: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    marginBottom: 16,
  },
  title: {
    fontSize: 18,
    fontWeight: 'bold',
  },
  count: {
    fontSize: 14,
    color: '#6b7280',
  },
  loadingContainer: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
  },
  loadingText: {
    marginTop: 12,
    fontSize: 14,
    color: '#6b7280',
  },
  emptyState: {
    alignItems: 'center',
    padding: 32,
  },
  emptyTitle: {
    fontSize: 20,
    fontWeight: 'bold',
    marginBottom: 8,
  },
  emptyDescription: {
    fontSize: 14,
    color: '#6b7280',
    textAlign: 'center',
  },
});

export default TaskList;