/**
 * Componente de lista vazia
 * Exibido quando não há tarefas
 */

import { View, Text, StyleSheet } from 'react-native';
import { MESSAGES } from '../utils/constants';
import type { ListEmptyProps } from '../types';

export const ListEmpty = ({ title, description }: ListEmptyProps) => (
  // Container centralizado para estado vazio
  <View style={styles.emptyState}>
    {/* Usa título customizado ou padrão */}
    <Text style={styles.emptyTitle}>{title || MESSAGES.EMPTY_TITLE}</Text>
    {/* Usa descrição customizada ou padrão */}
    <Text style={styles.emptyDescription}>
      {description || MESSAGES.EMPTY_DESCRIPTION}
    </Text>
  </View>
);

const styles = StyleSheet.create({
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