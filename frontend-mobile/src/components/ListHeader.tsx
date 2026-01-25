/**
 * Componente de cabeçalho da lista
 * Mostra contador de tarefas total e concluídas
 */

import { View, Text, StyleSheet } from 'react-native';
import type { ListHeaderProps } from '../types';

export const ListHeader = ({ tasksCount, completedCount }: ListHeaderProps) => (
  <View style={styles.header}>
    <Text style={styles.title}>Suas Tarefas ({tasksCount})</Text>
    {tasksCount > 0 && (
      <Text style={styles.count}>{completedCount} concluídas</Text>
    )}
  </View>
);

const styles = StyleSheet.create({
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
});