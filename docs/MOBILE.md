# Frontend Mobile - To Do List

Aplicação mobile desenvolvida com React Native e Expo para gerenciamento de tarefas.

## Tecnologias Utilizadas

- **React Native 0.76.5**: Framework para desenvolvimento mobile
- **Expo ~52.0.0**: Plataforma para desenvolvimento React Native
- **TypeScript ~5.6.2**: Linguagem com tipagem estática
- **React 18.3.1**: Biblioteca JavaScript para interfaces de usuário
- **react-native-safe-area-context**: Suporte a áreas seguras (notch, barra de status)

## Como Começar

### Requisitos do Sistema

- Node.js 18 ou superior
- npm ou yarn
- Expo CLI (instalado automaticamente via npx)
- Para testes: app Expo Go no dispositivo móvel, ou emulador Android/iOS

### Instalação

```bash
cd mobile
npm install
npm start
```

O Metro Bundler será iniciado e você poderá escanear o QR Code com o Expo Go.

## Comandos Principais

```bash
# Iniciar o servidor de desenvolvimento
npm start

# Executar no Android
npm run android

# Executar no iOS
npm run ios

# Executar na web
npm run web

# Limpar cache (se necessário)
npx expo start --clear
```

## Estrutura do Projeto

```plaintext
mobile/
├── App.tsx                   # Componente raiz da aplicação
├── src/
│   ├── components/
│   │   ├── TaskHeader.tsx    # Cabeçalho
│   │   ├── TaskForm.tsx      # Formulário (criação e edição)
│   │   ├── TaskList.tsx      # Lista de tarefas (FlatList)
│   │   ├── TaskItem.tsx      # Item individual
│   │   ├── TaskFooter.tsx    # Estatísticas
│   │   ├── ErrorMessage.tsx  # Tratamento de erros
│   │   ├── ListEmpty.tsx     # Estado vazio
│   │   └── ListHeader.tsx    # Contador de itens
│   ├── hooks/
│   │   └── useTasks.ts       # Gerenciamento de estado
│   ├── services/
│   │   └── taskApi.ts        # Funções de chamada à API
│   ├── styles/
│   │   └── theme.ts          # Cores, espaçamentos, border radius
│   ├── types/
│   │   └── index.ts          # Interfaces e tipos TypeScript
│   └── utils/
│       └── constants.ts      # API_URL e mensagens
└── package.json
```

## Modelo de Dados

A interface principal `Task` em `src/types/index.ts`:

```typescript
export interface Task {
  id: number;
  title: string;
  description: string;
  completed: boolean;
}

export interface TaskFormData {
  title: string;
  description: string;
}

export interface UseTasksReturn {
  tasks: Task[];
  loading: boolean;
  error: string | null;
  submitting: boolean;
  clearError: () => void;
  createTask: (taskData: TaskFormData) => Promise<boolean>;
  updateTask: (id: number, taskData: TaskFormData) => Promise<boolean>;
  toggleTask: (id: number) => Promise<void>;
  deleteTask: (id: number) => Promise<void>;
  fetchTasks: () => Promise<void>;
}
```

## Conexão com Backend

Configurado em `src/utils/constants.ts`. A URL base vem do `app.json` (campo `extra.apiUrl`), com fallback para o IP padrão do emulador Android:

```typescript
import Constants from 'expo-constants';

const apiUrl = Constants.expoConfig?.extra?.apiUrl || 'http://10.0.2.2:8080';
export const API_URL = `${apiUrl}/api/tasks`;
```

**IPs por ambiente:**

| Ambiente | IP |
| -------- | -- |
| Emulador Android | `http://10.0.2.2:8080` |
| Dispositivo físico | IP da máquina na rede local (ex: `http://192.168.1.100:8080`) |
| Web (Expo) | `http://localhost:8080` |

## Camada de Serviço (taskApi.ts)

As chamadas HTTP ficam em `src/services/taskApi.ts`, separadas do hook de estado:

```typescript
export async function fetchAllTasks(signal?: AbortSignal): Promise<Task[]>
export async function createTaskRequest(taskData: TaskFormData): Promise<Task>
export async function updateTaskRequest(id: number, taskData: TaskFormData): Promise<Task>
export async function toggleTaskRequest(id: number): Promise<Task>
export async function deleteTaskRequest(id: number): Promise<void>
```

Erros de API são lançados como `Error` com a mensagem retornada pelo backend, para que o hook possa capturá-los e exibi-los ao usuário.

## Hook Customizado (useTasks)

O hook `src/hooks/useTasks.ts` centraliza toda a lógica de negócio, consumindo o `taskApi`:

- Usa `useCallback` em todas as funções para evitar re-renders desnecessários
- Usa `AbortController` no `useEffect` para cancelar requisições ao desmontar
- Atualiza o estado local otimisticamente (sem re-fetch) após criação, edição, toggle e deleção

## Componente Principal (App.tsx)

`App.tsx` é o componente raiz, que:

- Envolve tudo com `SafeAreaProvider` e `SafeAreaView`
- Gerencia o estado do formulário (`form`) e o ID da tarefa em edição (`editingTaskId`)
- Conecta todos os componentes ao hook `useTasks`
- O `TaskForm` funciona tanto para criação quanto para edição (diferenciado por `editingTaskId`)

```typescript
export default function App() {
  const [form, setForm] = useState<TaskFormData>({ title: "", description: "" });
  const [editingTaskId, setEditingTaskId] = useState<number | null>(null);
  const { tasks, loading, error, submitting, clearError,
          createTask, updateTask, toggleTask, deleteTask } = useTasks();
  // ...
}
```

## Componentes

### TaskForm

Formulário compartilhado para criação e edição de tarefas:

- Quando `editingTaskId` é `null`, o submit cria uma nova tarefa
- Quando `editingTaskId` tem valor, o submit atualiza a tarefa correspondente
- Exibe botão "Cancelar" durante edição

### TaskList

Usa `FlatList` do React Native para renderização otimizada:

- Componente de lista vazia (`ListEmpty`)
- Header com contador (`ListHeader`)
- Scroll automático

### TaskItem

Exibe uma tarefa com botões de ação. Usa `Alert.alert()` nativo para confirmar exclusão:

```typescript
Alert.alert(
  'Confirmar exclusão',
  'Tem certeza que deseja remover esta tarefa?',
  [
    { text: 'Cancelar', style: 'cancel' },
    { text: 'Remover', style: 'destructive', onPress: () => onDelete(task.id) }
  ]
);
```

### ErrorMessage

Exibe erros com botão de fechar (`onDismiss`).

## Estilização

### Tema Centralizado

`src/styles/theme.ts` centraliza cores e espaçamentos:

```typescript
export const theme = {
  colors: {
    primary: "#007AFF",
    danger: "#FF3B30",
    success: "#34C759",
    background: "#F2F2F7",
    surface: "#FFFFFF",
    text: { primary: "#000000", secondary: "#8E8E93" },
    border: "#D1D1D6",
  },
  spacing: { xs: 4, sm: 8, md: 16, lg: 24, xl: 32 },
  borderRadius: { sm: 8, md: 12, lg: 16 },
};
```

Todos os componentes usam `StyleSheet.create()` referenciando o tema.

## Tratamento de Erros

A aplicação trata erros em três níveis:

1. **Validação**: Título vazio bloqueado no hook antes de chamar a API
2. **Rede**: Falhas de conexão capturadas no `catch` do hook
3. **API**: Mensagens de erro do backend propagadas via `Error.message`

Todos os erros são exibidos no componente `ErrorMessage`.

## Testando em Dispositivo Físico

1. Instale o **Expo Go** na Play Store ou App Store
2. Execute `npm start`
3. Escaneie o QR Code exibido no terminal
4. Configure o `apiUrl` no `app.json` com o IP da sua máquina

## Solução de Problemas

### Erro de Conexão com API

- **Emulador Android**: use `http://10.0.2.2:8080`
- **Dispositivo físico**: use o IP da máquina (`ipconfig` no Windows, `ifconfig` no Linux/Mac)
- Verifique se o backend está rodando e a porta 8080 está acessível na rede

### App não carrega após mudanças

```bash
npx expo start --clear
```

### Versões Testadas

```json
{
  "expo": "~52.0.0",
  "react": "18.3.1",
  "react-native": "0.76.5",
  "@types/react": "~18.3.12",
  "typescript": "~5.6.2"
}
```

---

**Última atualização:** 24 de abril de 2026
