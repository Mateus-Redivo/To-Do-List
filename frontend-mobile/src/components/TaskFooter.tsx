import { View, Text, StyleSheet } from "react-native";
import { colors, spacing } from '../styles/theme';

function TaskFooter() {
  return (
    <View style={styles.taskFooter}>
      <Text>Feito para organizar suas tarefas</Text>
    </View>
  );
}

const styles = StyleSheet.create({
  taskFooter: {
    padding: spacing.md, 
    alignItems: 'center',
    justifyContent: 'center',
    backgroundColor: colors.background,
  },
});

export default TaskFooter;