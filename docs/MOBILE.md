# Frontend Mobile - To Do List

Aplicação mobile desenvolvida com React Native e Expo para gerenciamento de tarefas.

## Criação do Projeto

### 1. Inicializar projeto Expo

```bash
npx create-expo-app@latest frontend-mobile --template blank-typescript
cd frontend-mobile
```

### 2. Instalar dependências compatíveis

Edite o `package.json` para garantir versões compatíveis:

```json
{
  "dependencies": {
    "expo": "~52.0.0",
    "expo-status-bar": "~2.0.0",
    "react": "18.3.1",
    "react-native": "0.76.5"
  },
  "devDependencies": {
    "@types/react": "~18.3.12",
    "typescript": "~5.6.2"
  }
}
```

Execute a instalação:

```bash
npm install
```

### 3. Configurar TypeScript

Certifique-se que o `tsconfig.json` tenha a configuração:

```json
{
  "extends": "expo/tsconfig.base",
  "compilerOptions": {
    "strict": true,
    "skipLibCheck": true
  }
}
```

### 4. Estrutura de pastas

```txt
frontend-mobile/
├── src/
│   ├── components/
│   ├── hooks/
│   ├── styles/
│   ├── types/
│   └── utils/
├── assets/
├── App.tsx
├── index.ts
└── package.json
```

### 5. Criar arquivos de tipos

Crie o arquivo `src/types/index.ts`:

```typescript
export interface Task {
  id: number;
  title: string;
  completed: boolean;
}

export interface TaskStats {
  total: number;
  completed: number;
  pending: number;
}
```

### 6. Criar constantes

Crie o arquivo `src/utils/constants.ts`:

```typescript
export const API_URL = 'http://localhost:3000';
```

### 7. Criar tema de estilos

Crie o arquivo `src/styles/theme.ts`:

```typescript
export const theme = {
  colors: {
    primary: '#007AFF',
    danger: '#FF3B30',
    success: '#34C759',
    background: '#F2F2F7',
    surface: '#FFFFFF',
    text: {
      primary: '#000000',
      secondary: '#8E8E93',
    },
    border: '#D1D1D6',
  },
  spacing: {
    xs: 4,
    sm: 8,
    md: 16,
    lg: 24,
    xl: 32,
  },
  borderRadius: {
    sm: 8,
    md: 12,
    lg: 16,
  },
};
```

### 8. Criar hook customizado para gerenciamento de tarefas

Crie o arquivo `src/hooks/useTasks.ts`:

```typescript
import { useState, useEffect, useCallback } from 'react';
import { Task, TaskStats } from '../types';
import { API_URL } from '../utils/constants';

export const useTasks = () => {
  const [tasks, setTasks] = useState<Task[]>([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const fetchTasks = useCallback(async () => {
    setLoading(true);
    setError(null);
    try {
      const response = await fetch(`${API_URL}/tasks`);
      if (!response.ok) throw new Error('Erro ao carregar tarefas');
      const data = await response.json();
      setTasks(data);
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Erro desconhecido');
    } finally {
      setLoading(false);
    }
  }, []);

  const addTask = async (title: string) => {
    if (!title.trim()) return;
    setLoading(true);
    setError(null);
    try {
      const response = await fetch(`${API_URL}/tasks`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ title, completed: false }),
      });
      if (!response.ok) throw new Error('Erro ao adicionar tarefa');
      await fetchTasks();
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Erro desconhecido');
    } finally {
      setLoading(false);
    }
  };

  const toggleTask = async (id: number) => {
    const task = tasks.find((t) => t.id === id);
    if (!task) return;

    setLoading(true);
    setError(null);
    try {
      const response = await fetch(`${API_URL}/tasks/${id}`, {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ ...task, completed: !task.completed }),
      });
      if (!response.ok) throw new Error('Erro ao atualizar tarefa');
      await fetchTasks();
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Erro desconhecido');
    } finally {
      setLoading(false);
    }
  };

  const deleteTask = async (id: number) => {
    setLoading(true);
    setError(null);
    try {
      const response = await fetch(`${API_URL}/tasks/${id}`, {
        method: 'DELETE',
      });
      if (!response.ok) throw new Error('Erro ao excluir tarefa');
      await fetchTasks();
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Erro desconhecido');
    } finally {
      setLoading(false);
    }
  };

  const getStats = (): TaskStats => ({
    total: tasks.length,
    completed: tasks.filter((t) => t.completed).length,
    pending: tasks.filter((t) => !t.completed).length,
  });

  useEffect(() => {
    fetchTasks();
  }, [fetchTasks]);

  return {
    tasks,
    loading,
    error,
    addTask,
    toggleTask,
    deleteTask,
    getStats,
    refetch: fetchTasks,
  };
};
```

### 9. Criar componentes reutilizáveis

#### 9.1. ErrorMessage (`src/components/ErrorMessage.tsx`)

```typescript
import React from 'react';
import { View, Text, StyleSheet, TouchableOpacity } from 'react-native';
import { theme } from '../styles/theme';

interface ErrorMessageProps {
  message: string;
  onRetry?: () => void;
}

export const ErrorMessage: React.FC<ErrorMessageProps> = ({ message, onRetry }) => (
  <View style={styles.container}>
    <Text style={styles.message}>{message}</Text>
    {onRetry && (
      <TouchableOpacity style={styles.button} onPress={onRetry}>
        <Text style={styles.buttonText}>Tentar Novamente</Text>
      </TouchableOpacity>
    )}
  </View>
);

const styles = StyleSheet.create({
  container: {
    padding: theme.spacing.md,
    backgroundColor: theme.colors.surface,
    borderRadius: theme.borderRadius.md,
    alignItems: 'center',
  },
  message: {
    color: theme.colors.danger,
    marginBottom: theme.spacing.sm,
  },
  button: {
    backgroundColor: theme.colors.primary,
    paddingHorizontal: theme.spacing.md,
    paddingVertical: theme.spacing.sm,
    borderRadius: theme.borderRadius.sm,
  },
  buttonText: {
    color: '#FFFFFF',
    fontWeight: '600',
  },
});
```

#### 9.2. ListEmpty (`src/components/ListEmpty.tsx`)

```typescript
import React from 'react';
import { View, Text, StyleSheet } from 'react-native';
import { theme } from '../styles/theme';

export const ListEmpty: React.FC = () => (
  <View style={styles.container}>
    <Text style={styles.text}>Nenhuma tarefa encontrada</Text>
  </View>
);

const styles = StyleSheet.create({
  container: {
    padding: theme.spacing.xl,
    alignItems: 'center',
  },
  text: {
    color: theme.colors.text.secondary,
    fontSize: 16,
  },
});
```

#### 9.3. ListHeader (`src/components/ListHeader.tsx`)

```typescript
import React from 'react';
import { View, Text, StyleSheet } from 'react-native';
import { theme } from '../styles/theme';

interface ListHeaderProps {
  count: number;
}

export const ListHeader: React.FC<ListHeaderProps> = ({ count }) => (
  <View style={styles.container}>
    <Text style={styles.text}>
      {count} {count === 1 ? 'tarefa' : 'tarefas'}
    </Text>
  </View>
);

const styles = StyleSheet.create({
  container: {
    paddingVertical: theme.spacing.sm,
  },
  text: {
    color: theme.colors.text.secondary,
    fontSize: 14,
  },
});
```

#### 9.4. TaskFooter (`src/components/TaskFooter.tsx`)

```typescript
import React from 'react';
import { View, Text, StyleSheet } from 'react-native';
import { TaskStats } from '../types';
import { theme } from '../styles/theme';

interface TaskFooterProps {
  stats: TaskStats;
}

export const TaskFooter: React.FC<TaskFooterProps> = ({ stats }) => (
  <View style={styles.container}>
    <View style={styles.stat}>
      <Text style={styles.value}>{stats.total}</Text>
      <Text style={styles.label}>Total</Text>
    </View>
    <View style={styles.stat}>
      <Text style={styles.value}>{stats.completed}</Text>
      <Text style={styles.label}>Concluídas</Text>
    </View>
    <View style={styles.stat}>
      <Text style={styles.value}>{stats.pending}</Text>
      <Text style={styles.label}>Pendentes</Text>
    </View>
  </View>
);

const styles = StyleSheet.create({
  container: {
    flexDirection: 'row',
    justifyContent: 'space-around',
    padding: theme.spacing.md,
    backgroundColor: theme.colors.surface,
    borderTopWidth: 1,
    borderTopColor: theme.colors.border,
  },
  stat: {
    alignItems: 'center',
  },
  value: {
    fontSize: 24,
    fontWeight: 'bold',
    color: theme.colors.primary,
  },
  label: {
    fontSize: 12,
    color: theme.colors.text.secondary,
    marginTop: theme.spacing.xs,
  },
});
```

#### 9.5. TaskForm (`src/components/TaskForm.tsx`)

```typescript
import React, { useState } from 'react';
import { View, TextInput, TouchableOpacity, Text, StyleSheet } from 'react-native';
import { theme } from '../styles/theme';

interface TaskFormProps {
  onSubmit: (title: string) => void;
  loading: boolean;
}

export const TaskForm: React.FC<TaskFormProps> = ({ onSubmit, loading }) => {
  const [title, setTitle] = useState('');

  const handleSubmit = () => {
    if (title.trim()) {
      onSubmit(title);
      setTitle('');
    }
  };

  return (
    <View style={styles.container}>
      <TextInput
        style={styles.input}
        placeholder="Nova tarefa..."
        value={title}
        onChangeText={setTitle}
        onSubmitEditing={handleSubmit}
        editable={!loading}
      />
      <TouchableOpacity
        style={[styles.button, loading && styles.buttonDisabled]}
        onPress={handleSubmit}
        disabled={loading}
      >
        <Text style={styles.buttonText}>+</Text>
      </TouchableOpacity>
    </View>
  );
};

const styles = StyleSheet.create({
  container: {
    flexDirection: 'row',
    padding: theme.spacing.md,
    backgroundColor: theme.colors.surface,
    gap: theme.spacing.sm,
  },
  input: {
    flex: 1,
    height: 48,
    borderWidth: 1,
    borderColor: theme.colors.border,
    borderRadius: theme.borderRadius.sm,
    paddingHorizontal: theme.spacing.md,
    fontSize: 16,
  },
  button: {
    width: 48,
    height: 48,
    backgroundColor: theme.colors.primary,
    borderRadius: theme.borderRadius.sm,
    justifyContent: 'center',
    alignItems: 'center',
  },
  buttonDisabled: {
    opacity: 0.5,
  },
  buttonText: {
    color: '#FFFFFF',
    fontSize: 24,
    fontWeight: 'bold',
  },
});
```

#### 9.6. TaskHeader (`src/components/TaskHeader.tsx`)

```typescript
import React from 'react';
import { View, Text, StyleSheet } from 'react-native';
import { theme } from '../styles/theme';

export const TaskHeader: React.FC = () => (
  <View style={styles.container}>
    <Text style={styles.title}>Minhas Tarefas</Text>
  </View>
);

const styles = StyleSheet.create({
  container: {
    padding: theme.spacing.md,
    backgroundColor: theme.colors.primary,
  },
  title: {
    fontSize: 24,
    fontWeight: 'bold',
    color: '#FFFFFF',
  },
});
```

#### 9.7. TaskItem (`src/components/TaskItem.tsx`)

```typescript
import React from 'react';
import { View, Text, TouchableOpacity, StyleSheet } from 'react-native';
import { Task } from '../types';
import { theme } from '../styles/theme';

interface TaskItemProps {
  task: Task;
  onToggle: (id: number) => void;
  onDelete: (id: number) => void;
}

export const TaskItem: React.FC<TaskItemProps> = ({ task, onToggle, onDelete }) => (
  <View style={styles.container}>
    <TouchableOpacity
      style={styles.content}
      onPress={() => onToggle(task.id)}
      activeOpacity={0.7}
    >
      <View style={[styles.checkbox, task.completed && styles.checkboxCompleted]}>
        {task.completed && <Text style={styles.checkmark}>✓</Text>}
      </View>
      <Text style={[styles.title, task.completed && styles.titleCompleted]}>
        {task.title}
      </Text>
    </TouchableOpacity>
    <TouchableOpacity
      style={styles.deleteButton}
      onPress={() => onDelete(task.id)}
      activeOpacity={0.7}
    >
      <Text style={styles.deleteText}>×</Text>
    </TouchableOpacity>
  </View>
);

const styles = StyleSheet.create({
  container: {
    flexDirection: 'row',
    alignItems: 'center',
    backgroundColor: theme.colors.surface,
    padding: theme.spacing.md,
    borderBottomWidth: 1,
    borderBottomColor: theme.colors.border,
  },
  content: {
    flex: 1,
    flexDirection: 'row',
    alignItems: 'center',
  },
  checkbox: {
    width: 24,
    height: 24,
    borderRadius: 12,
    borderWidth: 2,
    borderColor: theme.colors.border,
    marginRight: theme.spacing.md,
    justifyContent: 'center',
    alignItems: 'center',
  },
  checkboxCompleted: {
    backgroundColor: theme.colors.success,
    borderColor: theme.colors.success,
  },
  checkmark: {
    color: '#FFFFFF',
    fontSize: 16,
    fontWeight: 'bold',
  },
  title: {
    fontSize: 16,
    color: theme.colors.text.primary,
    flex: 1,
  },
  titleCompleted: {
    textDecorationLine: 'line-through',
    color: theme.colors.text.secondary,
  },
  deleteButton: {
    width: 32,
    height: 32,
    justifyContent: 'center',
    alignItems: 'center',
  },
  deleteText: {
    fontSize: 28,
    color: theme.colors.danger,
    fontWeight: '300',
  },
});
```

#### 9.8. TaskList (`src/components/TaskList.tsx`)

```typescript
import React from 'react';
import { FlatList, StyleSheet } from 'react-native';
import { Task } from '../types';
import { TaskItem } from './TaskItem';
import { ListEmpty } from './ListEmpty';
import { ListHeader } from './ListHeader';

interface TaskListProps {
  tasks: Task[];
  onToggle: (id: number) => void;
  onDelete: (id: number) => void;
}

export const TaskList: React.FC<TaskListProps> = ({ tasks, onToggle, onDelete }) => (
  <FlatList
    data={tasks}
    keyExtractor={(item) => item.id.toString()}
    renderItem={({ item }) => (
      <TaskItem task={item} onToggle={onToggle} onDelete={onDelete} />
    )}
    ListEmptyComponent={ListEmpty}
    ListHeaderComponent={tasks.length > 0 ? <ListHeader count={tasks.length} /> : null}
    contentContainerStyle={tasks.length === 0 && styles.emptyContainer}
  />
);

const styles = StyleSheet.create({
  emptyContainer: {
    flex: 1,
    justifyContent: 'center',
  },
});
```

#### 9.9. TaskApp (`src/components/TaskApp.tsx`)

```typescript
import React from 'react';
import { View, StyleSheet, ActivityIndicator } from 'react-native';
import { useTasks } from '../hooks/useTasks';
import { TaskHeader } from './TaskHeader';
import { TaskForm } from './TaskForm';
import { TaskList } from './TaskList';
import { TaskFooter } from './TaskFooter';
import { ErrorMessage } from './ErrorMessage';
import { theme } from '../styles/theme';

export const TaskApp: React.FC = () => {
  const { tasks, loading, error, addTask, toggleTask, deleteTask, getStats, refetch } = useTasks();

  return (
    <View style={styles.container}>
      <TaskHeader />
      <TaskForm onSubmit={addTask} loading={loading} />
      {error ? (
        <ErrorMessage message={error} onRetry={refetch} />
      ) : loading && tasks.length === 0 ? (
        <View style={styles.loading}>
          <ActivityIndicator size="large" color={theme.colors.primary} />
        </View>
      ) : (
        <>
          <TaskList tasks={tasks} onToggle={toggleTask} onDelete={deleteTask} />
          <TaskFooter stats={getStats()} />
        </>
      )}
    </View>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: theme.colors.background,
  },
  loading: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
  },
});
```

### 10. Atualizar App.tsx

Edite o arquivo `App.tsx`:

```typescript
import { StatusBar } from 'expo-status-bar';
import { SafeAreaView, StyleSheet } from 'react-native';
import { TaskApp } from './src/components/TaskApp';
import { theme } from './src/styles/theme';

export default function App() {
  return (
    <SafeAreaView style={styles.container}>
      <TaskApp />
      <StatusBar style="light" />
    </SafeAreaView>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: theme.colors.background,
  },
});
```

### 11. Criar arquivo index.ts

Crie o arquivo `index.ts` na raiz do projeto:

```typescript
import { registerRootComponent } from 'expo';
import App from './App';

registerRootComponent(App);
```

### 12. Instalar dependência adicional

Instale o pacote `react-native-safe-area-context`:

```bash
npm install react-native-safe-area-context
```

### 13. Executar a aplicação

Inicie o servidor de desenvolvimento:

```bash
npm start
```

Para executar no Android:

```bash
npm run android
```

Para executar no iOS:

```bash
npm run ios
```

Para executar na web:

```bash
npm run web
```

### 14. Configurar conexão com backend

Certifique-se de que o backend está rodando em `http://localhost:3000`. Se você estiver testando em um dispositivo físico, atualize a constante `API_URL` em `src/utils/constants.ts` com o IP da sua máquina:

```typescript
// Substitua pelo IP da sua máquina
export const API_URL = 'http://192.168.1.100:3000';
```

Para descobrir o IP da sua máquina:

**Windows:**

```bash
ipconfig
```

**Linux/Mac:**

```bash
ifconfig
```

## Solução de Problemas

### Erro: "JSX element class does not support attributes"

Este erro ocorre quando há incompatibilidade entre as versões do React e React Native.

**Causa comum:**

- React 19 com React Native 0.76.x ou anterior

**Solução:**

1. Verifique as versões no `package.json`:

   - React deve ser 18.3.1
   - React Native deve ser 0.76.5
   - @types/react deve ser ~18.3.12

2. Remova node_modules e reinstale:

    ```bash
    rm -rf node_modules package-lock.json
    npm install
    ```

3. Se o erro persistir, execute:

    ```bash
    npm audit fix --force
    ```

**Versões testadas e funcionais:**

- Expo: ~52.0.0
- React: 18.3.1
- React Native: 0.76.5
- TypeScript: ~5.6.2

### Erro: "Install @expo/ngrok@^4.1.0 and try again"

Este erro ocorre ao iniciar o Metro Bundler quando o Expo solicita a instalação do pacote `@expo/ngrok` para usar túneis.

**Problema:**

- O Expo oferece instalar `@expo/ngrok` globalmente, mas você pode preferir instalá-lo localmente no projeto.

**Solução:**

Instale o `@expo/ngrok` como dependência de desenvolvimento local:

```bash
npm install --save-dev @expo/ngrok
```

Após a instalação, inicie o Metro Bundler normalmente:

```bash
npx expo start
```
