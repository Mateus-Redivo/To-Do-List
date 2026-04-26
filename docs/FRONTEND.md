# Frontend Web - To Do List

Frontend web do projeto de gerenciamento de tarefas desenvolvido com React e TypeScript.

## Tecnologias Utilizadas

- **React 19**: Biblioteca principal para construção da interface de usuário
- **TypeScript**: Adiciona tipagem estática ao JavaScript
- **Vite**: Ferramenta de build e servidor de desenvolvimento
- **Tailwind CSS**: Framework CSS utilitário para estilização
- **ESLint**: Análise de código para manter qualidade e padrões

## Como Começar

### Requisitos do Sistema

- Node.js 18 ou superior
- npm para gerenciamento de pacotes
- Git para controle de versão

### Configuração Inicial

```bash
# Entrar no diretório do frontend
cd frontend

# Instalar as dependências
npm install

# Iniciar o servidor de desenvolvimento
npm run dev
```

O servidor de desenvolvimento ficará disponível em `http://localhost:5173`.

## Comandos Principais

```bash
# Iniciar o servidor de desenvolvimento
npm run dev

# Criar build para produção
npm run build

# Visualizar o build de produção
npm run preview

# Verificar problemas no código
npm run lint
```

## Configuração do Backend

A API é consumida via URL relativa, com proxy configurado pelo Vite. O arquivo `src/utils/constants.ts` define a URL:

```typescript
export const API_URL = "/api/tasks";
```

Em desenvolvimento, as requisições para `/api/tasks` são automaticamente encaminhadas para `http://localhost:8080/api/tasks` pelo proxy do Vite. Em produção via Docker, o Nginx roteia os caminhos `/api/*` para o backend.

### Endpoints Consumidos

- **GET /api/tasks** — Lista todas as tarefas
- **POST /api/tasks** — Cria uma nova tarefa
- **PUT /api/tasks/:id** — Atualiza título, descrição e status
- **PATCH /api/tasks/:id/toggle** — Alterna status de conclusão
- **DELETE /api/tasks/:id** — Remove uma tarefa

### Formato de Dados

```json
{
  "id": 1,
  "title": "Minha tarefa",
  "description": "Descrição da tarefa",
  "completed": false
}
```

## Estrutura do Projeto

```plaintext
frontend/
├── src/
│   ├── components/       # Componentes React
│   │   ├── TaskApp.tsx       # Componente raiz da aplicação
│   │   ├── TaskHeader.tsx    # Cabeçalho
│   │   ├── TaskForm.tsx      # Formulário de criação
│   │   ├── TaskList.tsx      # Lista de tarefas
│   │   ├── TaskItem.tsx      # Item individual com edição inline
│   │   ├── TaskFooter.tsx    # Rodapé com estatísticas
│   │   └── ErrorMessage.tsx  # Exibição de erros
│   ├── hooks/
│   │   └── useTasks.ts       # Hook de gerenciamento de estado
│   ├── types/
│   │   └── index.ts          # Interfaces e tipos TypeScript
│   └── utils/
│       └── constants.ts      # API_URL e mensagens
├── index.html
└── package.json
```

## Tipos e Interfaces

Definidos em `src/types/index.ts`:

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

## Hook Personalizado (useTasks)

O hook `src/hooks/useTasks.ts` centraliza toda a lógica de negócio:

**Funções exportadas:**

- `fetchTasks()` — Carrega todas as tarefas da API
- `createTask(taskData)` — Cria nova tarefa, retorna `boolean`
- `updateTask(id, taskData)` — Atualiza tarefa existente, retorna `boolean`
- `toggleTask(id)` — Alterna status de conclusão
- `deleteTask(id)` — Remove tarefa
- `clearError()` — Limpa a mensagem de erro atual

**Estado gerenciado:**

- `tasks` — Array de tarefas carregadas
- `loading` — Indica carregamento inicial
- `error` — Mensagem de erro atual ou `null`
- `submitting` — Indica operação de escrita em andamento

**Tratamento de erros do backend:**

```typescript
if (!response.ok) {
  let message = MESSAGES.ERROR_CREATE;
  try {
    const errorData = await response.json();
    if (errorData.error) message = errorData.error;
  } catch { /* usa mensagem padrão */ }
  setError(message);
  return false;
}
```

## Componentes

### TaskApp

Componente principal que orquestra a aplicação. Gerencia o estado do formulário e conecta todos os componentes ao hook `useTasks`.

### TaskForm

Formulário para criar novas tarefas com campos de título e descrição. O botão de submit é desabilitado enquanto `submitting` for `true`.

### TaskList

Renderiza a lista de tarefas recebida como prop. Exibe estado de carregamento e estado vazio.

### TaskItem

Item individual da lista com dois modos:

- **Visualização**: Exibe título, descrição e botões de ação
- **Edição inline**: Inputs para editar título e descrição com validação local

Usa `window.confirm()` antes de deletar uma tarefa.

### ErrorMessage

Exibe erros globais com botão de fechar (`onDismiss`). Aparece abaixo do cabeçalho.

## Validação de Dados

O campo título é **obrigatório** tanto ao criar quanto ao editar tarefas:

- **Ao criar**: Validado no hook antes de chamar a API
- **Ao editar inline**: Validado localmente dentro do `TaskItem`

### Mensagens de Erro

Definidas em `src/utils/constants.ts`:

| Constante | Situação |
| --------- | -------- |
| `ERROR_LOAD` | Falha ao carregar tarefas |
| `ERROR_CREATE` | Falha ao criar tarefa |
| `ERROR_UPDATE` | Falha ao atualizar tarefa |
| `ERROR_DELETE` | Falha ao remover tarefa |
| `ERROR_EMPTY_TITLE` | Título vazio ao criar ou editar |
| `ERROR_CONNECTION` | Sem conexão com o servidor |

---

**Última atualização:** 24 de abril de 2026
