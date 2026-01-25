/**
 * Componente de cabeçalho principal
 * Exibe título e descrição da aplicação
 */

import { Text, View, StyleSheet } from "react-native";
import { colors, spacing, typography } from "../styles/theme";

function TaskHeader() {
  return (
    <View style={styles.taskHeader}>
      <Text style={styles.textHeader}>Lista de Tarefas</Text>
      <Text style={styles.textSubtitle}>
        Organize suas atividades de forma simples e eficiente
      </Text>
    </View>
  );
}

const styles = StyleSheet.create({
  taskHeader: {
    backgroundColor: colors.primary,
    padding: spacing.lg,
    paddingTop: spacing.xl * 1.5,
  },
  textHeader: {
    color: colors.text,
    fontSize: typography.sizes.xxl,
  },
  textSubtitle: {
    color: colors.textSecondary,
    fontSize: typography.sizes.md,
    marginTop: spacing.sm,
  },
});

export default TaskHeader;
