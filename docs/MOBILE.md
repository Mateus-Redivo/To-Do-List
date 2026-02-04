# Frontend Mobile - To Do List

Aplicação mobile desenvolvida com React Native e Expo para gerenciamento de tarefas.

## Tecnologias Utilizadas

- **React Native 0.76.5**: Framework para desenvolvimento mobile
- **Expo ~52.0.0**: Plataforma para desenvolvimento React Native
- **TypeScript ~5.6.2**: Linguagem de programação com tipagem estática
- **React 18.3.1**: Biblioteca JavaScript para interfaces de usuário

## Como Começar

### Requisitos do Sistema

Antes de iniciar o desenvolvimento, você precisa ter instalado em sua máquina:

- Node.js 18 ou superior
- npm ou yarn
- Expo CLI (será instalado automaticamente)
- Para testes: Expo Go app no seu dispositivo móvel ou emulador Android/iOS

### Configuração Inicial

**1. Criar o projeto:**

```bash
npx create-expo-app@latest frontend-mobile --template blank-typescript
cd frontend-mobile
```

**2. Instalar dependências adicionais:**

```bash
npm install react-native-safe-area-context
```

**3. Estrutura de pastas:**

O projeto segue uma organização modular:

```txt
frontend-mobile/
├── src/
│   ├── components/    # Componentes reutilizáveis
│   ├── hooks/         # Hooks customizados
│   ├── styles/        # Temas e estilos globais
│   ├── types/         # Definições TypeScript
│   └── utils/         # Funções utilitárias e constantes
├── assets/            # Imagens e recursos estáticos
├── App.tsx            # Componente principal
└── index.ts           # Ponto de entrada
```

## Comandos Principais

Durante o desenvolvimento, você usará principalmente estes comandos:

```bash
# Iniciar o servidor de desenvolvimento
npm start

# Executar no Android
npm run android

# Executar no iOS
npm run ios

# Executar na web
npm run web

# Instalar dependências
npm install

# Limpar cache (se necessário)
npx expo start --clear
```

O servidor Metro Bundler ficará disponível e você poderá escanear o QR Code com o app Expo Go.

## Arquitetura da Aplicação

### Estrutura em Camadas

A aplicação segue o padrão de componentes reativos:

```plaintext
src/
├── components/       # Componentes visuais
│   ├── TaskApp.tsx       # Container principal
│   ├── TaskHeader.tsx    # Cabeçalho
│   ├── TaskForm.tsx      # Formulário de entrada
│   ├── TaskList.tsx      # Lista de tarefas
│   ├── TaskItem.tsx      # Item individual
│   ├── TaskFooter.tsx    # Estatísticas
│   ├── ErrorMessage.tsx  # Tratamento de erros
│   ├── ListEmpty.tsx     # Estado vazio
│   └── ListHeader.tsx    # Contador de itens
├── hooks/            # Lógica de negócio
│   └── useTasks.ts       # Gerenciamento de estado
├── styles/           # Temas e estilos
│   └── theme.ts          # Cores, espaçamentos, etc
├── types/            # Definições TypeScript
│   └── index.ts          # Interfaces e tipos
└── utils/            # Utilitários
    └── constants.ts      # Configurações da API
```

### Modelo de Dados

A interface principal `Task` representa uma tarefa:

```typescript
interface Task {
  id: number;
  title: string;
  description: string;
  completed: boolean;
  createdAt?: string;
  updatedAt?: string;
}
```

**Outras interfaces importantes:**

- `TaskFormData`: Dados do formulário (title, description)
- `TaskStats`: Estatísticas (total, completed, pending)
- `TaskItemProps`: Props do componente de item

## Guia de Implementação

### 1. Configuração de Tipos

Crie `src/types/index.ts` com as interfaces necessárias:

```typescript
export interface Task {
  id: number;
  title: string;
  description: string;
  completed: boolean;
  createdAt?: string;
  updatedAt?: string;
}

export interface TaskFormData {
  title: string;
  description: string;
}

export interface TaskStats {
  total: number;
  completed: number;
  pending: number;
}

export interface TaskItemProps {
  task: Task;
  onToggle: (id: number) => void;
  onDelete: (id: number) => void;
  onEdit: (id: number, taskData: TaskFormData) => void;
}
```

**Confirmação de exclusão:**

Use `Alert.alert()` nativo do React Native para confirmar antes de deletar:

```typescript
import { Alert } from 'react-native';

<TouchableOpacity
  onPress={() => {
    Alert.alert(
      'Confirmar exclusão',
      'Tem certeza que deseja remover esta tarefa?',
      [
        { text: 'Cancelar', style: 'cancel' },
        { text: 'Remover', style: 'destructive', onPress: () => onDelete(task.id) }
      ]
    );
  }}
>
  <Text>Remover</Text>
</TouchableOpacity>
```

### 2. Constantes e Configuração

Crie `src/utils/constants.ts`:

```typescript
import Constants from 'expo-constants';

// Para emulador Android: use 10.0.2.2
// Para dispositivo físico: use o IP da sua máquina
const apiUrl = Constants.expoConfig?.extra?.apiUrl || 'http://10.0.2.2:8080';
export const API_URL = `${apiUrl}/api/tasks`;

export const MESSAGES = {
  ERROR_LOAD: "Erro ao carregar tarefas.",
  ERROR_CREATE: "Erro ao adicionar tarefa.",
  ERROR_UPDATE: "Erro ao atualizar tarefa.",
  ERROR_DELETE: "Erro ao remover tarefa.",
  ERROR_EMPTY_TITLE: "O título é obrigatório."
};
```

**Tratamento de erros do backend:**

O hook `useTasks` lê mensagens de erro enviadas pela API:

```typescript
if (!response.ok) {
  const errorData = await response.json();
  setError(errorData.error || MESSAGES.ERROR_CREATE);
  return false;
}
```

### 3. Tema de Estilos

Crie `src/styles/theme.ts` para centralizar cores e espaçamentos:

```typescript
export const theme = {
  colors: {
    primary: "#007AFF",
    danger: "#FF3B30",
    success: "#34C759",
    background: "#F2F2F7",
    surface: "#FFFFFF",
    text: {
      primary: "#000000",
      secondary: "#8E8E93",
    },
    border: "#D1D1D6",
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

### 4. Hook Customizado (useTasks)

O hook `src/hooks/useTasks.ts` centraliza toda a lógica de negócio:

**Principais funcionalidades:**

- `fetchTasks()`: Carrega todas as tarefas da API
- `addTask(title)`: Cria nova tarefa
- `updateTask(id, taskData)`: Atualiza tarefa existente
- `toggleTask(id)`: Alterna status de conclusão
- `deleteTask(id)`: Remove tarefa
- `getStats()`: Calcula estatísticas

**Estrutura básica:**

```typescript
export const useTasks = () => {
  const [tasks, setTasks] = useState<Task[]>([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const fetchTasks = useCallback(async () => {
    // Lógica para buscar tarefas
  }, []);

  // ... outras funções

  useEffect(() => {
    fetchTasks();
  }, [fetchTasks]);

  return {
    tasks,
    loading,
    error,
    addTask,
    updateTask,
    toggleTask,
    deleteTask,
    getStats,
    refetch: fetchTasks,
  };
};
```

### 5. Componentes

#### TaskApp (Container Principal)

Orquestra todos os componentes e conecta com o hook:

```typescript
export const TaskApp: React.FC = () => {
  const { tasks, loading, error, addTask, updateTask,
          toggleTask, deleteTask, getStats, refetch } = useTasks();

  return (
    <View style={styles.container}>
      <TaskHeader />
      <TaskForm onSubmit={addTask} loading={loading} />
      {error ? <ErrorMessage message={error} onRetry={refetch} /> : (
        <>
          <TaskList tasks={tasks} onToggle={toggleTask}
                    onDelete={deleteTask} onEdit={updateTask} />
          <TaskFooter stats={getStats()} />
        </>
      )}
    </View>
  );
};
```

#### TaskForm (Entrada de Dados)

Formulário para adicionar novas tarefas:

- `TextInput` para entrada de texto
- Botão de submit
- Desabilitação durante loading
- Limpeza automática após submit

#### TaskList (Lista de Tarefas)

Usa `FlatList` do React Native:

- Renderização otimizada
- Componente de lista vazia
- Header com contador
- Scroll automático

#### TaskItem (Item Individual)

Componente com dois modos:

**Modo visualização:**

- Exibe título e descrição
- Botões: Editar, Concluir/Desfazer, Remover
- Visual diferenciado para tarefas concluídas

**Modo edição:**

- Inputs para título e descrição
- Validação de título obrigatório
- Botões: Salvar, Cancelar

#### Componentes Auxiliares

- **TaskHeader**: Cabeçalho com título da aplicação
- **TaskFooter**: Estatísticas (total, concluídas, pendentes)
- **ErrorMessage**: Exibe erros com opção de retry
- **ListEmpty**: Mensagem quando não há tarefas
- **ListHeader**: Contador de tarefas

### 6. Arquivo Principal (App.tsx)

Envolve a aplicação com `SafeAreaView` e configura a `StatusBar`:

```typescript
import { StatusBar } from 'expo-status-bar';
import { SafeAreaView, StyleSheet } from 'react-native';
import { TaskApp } from './src/components/TaskApp';

export default function App() {
  return (
    <SafeAreaView style={styles.container}>
      <TaskApp />
      <StatusBar style="light" />
    </SafeAreaView>
  );
}
```

### 7. Ponto de Entrada (index.ts)

Registra o componente raiz:

```typescript
import { registerRootComponent } from "expo";
import App from "./App";

registerRootComponent(App);
```

## Conexão com Backend

### Configuração de IP

**Emulador Android:**

```typescript
export const API_URL = "http://10.0.2.2:3000";
```

**Dispositivo Físico:**

1. Descubra o IP da sua máquina:
   - Windows: `ipconfig`
   - Linux/Mac: `ifconfig`

2. Atualize a constante:

   ```typescript
   export const API_URL = "http://192.168.1.100:3000";
   ```

**Importante:** Certifique-se de que o backend está rodando e acessível na rede.

## Estilização

### Padrão StyleSheet

Todos os componentes usam `StyleSheet.create()` do React Native:

```typescript
const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: theme.colors.background,
  },
  // ... outros estilos
});
```

### Boas Práticas

- Use o tema centralizado para cores e espaçamentos
- Prefira Flexbox para layouts
- Use `Platform.select()` para estilos específicos de plataforma
- Evite estilos inline, prefira StyleSheet

## Tratamento de Erros

A aplicação trata erros em três níveis:

1. **Network Errors**: Problemas de conexão com a API
2. **Validation Errors**: Dados inválidos (ex: título vazio)
3. **API Errors**: Erros retornados pelo backend

Todos os erros são exibidos através do componente `ErrorMessage` com opção de retry.

## Executando a Aplicação

### Desenvolvimento Local

```bash
# Iniciar Metro Bundler
npm start

# Ou usar modo específico
npm run android  # Android
npm run ios      # iOS
npm run web      # Web
```

### Testando em Dispositivo Físico

1. Instale o **Expo Go** na Play Store ou App Store
2. Execute `npm start`
3. Escaneie o QR Code exibido no terminal
4. Aguarde o carregamento da aplicação

### Testando em Emulador

**Android Studio:**

- Inicie o emulador Android
- Execute `npm run android`

**Xcode (macOS):**

- Execute `npm run ios`

## Exemplo de Uso

### Fluxo Completo

1. **Adicionar tarefa**: Digite o título no campo e pressione "+"
2. **Visualizar lista**: Todas as tarefas são exibidas automaticamente
3. **Editar tarefa**: Toque em "Editar", modifique e salve
4. **Concluir tarefa**: Toque em "Concluir" (ou "Desfazer")
5. **Remover tarefa**: Toque em "Remover"
6. **Ver estatísticas**: Footer mostra total, concluídas e pendentes

## Recursos Adicionais

- **Hot Reload**: Alterações no código são refletidas automaticamente
- **Type Safety**: TypeScript garante tipagem em toda a aplicação
- **Component Reusability**: Componentes modulares e reutilizáveis
- **Custom Hooks**: Lógica de negócio separada da UI
- **Theme System**: Estilos centralizados e consistentes
- **Error Handling**: Tratamento robusto de erros
- **Loading States**: Feedback visual durante operações
- **Empty States**: Mensagens quando não há dados

## Solução de Problemas

### Erro: "JSX element class does not support attributes"

**Causa:** Incompatibilidade entre versões do React e React Native.

**Solução:**

1. Verifique as versões no `package.json`:
   - React: 18.3.1
   - React Native: 0.76.5
   - @types/react: ~18.3.12

2. Remova e reinstale as dependências:

   ```bash
   rm -rf node_modules package-lock.json
   npm install
   ```

3. Se persistir:

   ```bash
   npm audit fix --force
   ```

### Erro: "Install @expo/ngrok@^4.1.0 and try again"

**Causa:** Expo solicita `@expo/ngrok` para túneis de rede.

**Solução:**

Instale como dependência de desenvolvimento:

```bash
npm install --save-dev @expo/ngrok
```

### Erro de Conexão com API

**Sintomas:** Requisições falhando, timeout, ou "Network request failed"

**Soluções:**

1. **Verificar backend:**
   - Backend está rodando?
   - Porta 3000 está acessível?
   - CORS configurado corretamente?

2. **Ajustar API_URL:**
   - Emulador Android: `http://10.0.2.2:3000`
   - Dispositivo físico: Use IP da máquina
   - Localhost: Apenas para web

3. **Testar conexão:**

   ```bash
   # No terminal da sua máquina
   curl http://localhost:3000/api/tasks
   ```

### App não carrega após mudanças

**Solução:**

Limpe o cache e reinicie:

```bash
npx expo start --clear
```

### Versões Testadas e Funcionais

```json
{
  "expo": "~52.0.0",
  "react": "18.3.1",
  "react-native": "0.76.5",
  "@types/react": "~18.3.12",
  "typescript": "~5.6.2"
}
```
