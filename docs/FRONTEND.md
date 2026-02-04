# Pwini - Gerenciador de Tarefas

Este é o frontend de um projeto de gerenciamento de tarefas.

## Tecnologias Utilizadas

- **React 19**: Biblioteca principal para construção da interface de usuário
- **TypeScript**: Adiciona tipagem estática ao JavaScript, tornando o código mais seguro e maintível
- **Vite**: Ferramenta de build e servidor de desenvolvimento extremamente rápida
- **Tailwind CSS**: Framework CSS utilitário para estilização ágil e consistente
- **ESLint**: Ferramenta de análise de código para manter a qualidade e padrões

## Como Começar

### Requisitos do Sistema

Antes de iniciar o desenvolvimento, você precisa ter instalado em sua máquina:

- Node.js versão 18 ou superior
- npm ou yarn para gerenciamento de pacotes
- Git para controle de versão

### Configuração Inicial do Projeto

Se você está começando um novo projeto do zero, siga estes passos:

**Criando o projeto base:**

```bash
# Criar novo projeto React com TypeScript usando Vite
npm create vite@latest pwini -- --template react-swc-ts

# Entrar no diretório do projeto
cd pwini

# Instalar as dependências básicas
npm install
```

**Configurando o Tailwind CSS:**

```bash
# Instalar o Tailwind e suas dependências
npm install -D tailwindcss postcss autoprefixer

# Inicializar os arquivos de configuração
npx tailwindcss init -p

# Instalar tipos do Node.js (opcional)
npm install @types/node
```

**Configurar o arquivo tailwind.config.js:**

```js
/** @type {import('tailwindcss').Config} */
export default {
  content: [
    "./index.html",
    "./src/**/*.{js,ts,jsx,tsx}",
  ],
  theme: {
    extend: {},
  },
  plugins: [],
}
```

**Atualizar o arquivo src/index.css:**

```css
@tailwind base;
@tailwind components;
@tailwind utilities;
```

## Comandos Principais

Durante o desenvolvimento, você usará principalmente estes comandos:

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

O servidor de desenvolvimento ficará disponível em `http://localhost:5173`.

## Configuração do Backend

Esta aplicação frontend se conecta a uma API REST. Para configurar a conexão, edite o arquivo `src/utils/constants.ts`:

```typescript
export const API_URL = "http://localhost:8080/api/tasks";
```

### Endpoints Necessários

Sua API deve implementar os seguintes endpoints:

- **GET /api/tasks**: Retorna todas as tarefas
- **POST /api/tasks**: Cria uma nova tarefa
- **PUT /api/tasks/:id**: Atualiza uma tarefa existente (título e descrição)
- **PATCH /api/tasks/:id/toggle**: Alterna o status de conclusão da tarefa
- **DELETE /api/tasks/:id**: Remove uma tarefa

### Formato de Dados

As tarefas devem seguir este formato JSON:

```json
{
  "id": 1,
  "title": "Minha tarefa",
  "description": "Descrição da tarefa",
  "completed": false,
  "createdAt": "2024-01-15T10:30:00Z",
  "updatedAt": "2024-01-15T10:30:00Z"
}
```

## Guia de Implementação

Se você está desenvolvendo este projeto, sugerimos seguir esta ordem:

### 1. Definir Tipos e Interfaces

Comece criando as definições de tipos em `src/types/index.ts`:

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
```

### 2. Configurar Constantes

Defina as configurações em `src/utils/constants.ts`:

```typescript
export const API_URL = "http://localhost:8080/api/tasks";

export const MESSAGES = {
  LOADING: "Carregando tarefas...",
  EMPTY_TITLE: "Nenhuma tarefa encontrada",
  EMPTY_DESCRIPTION: "Adicione sua primeira tarefa acima para começar!",
  ERROR_LOAD: "Erro ao carregar tarefas. Verifique se o servidor está rodando.",
  ERROR_CREATE: "Erro ao adicionar tarefa. Tente novamente.",
  ERROR_UPDATE: "Erro ao atualizar tarefa. Tente novamente.",
  ERROR_DELETE: "Erro ao remover tarefa. Tente novamente.",
  ERROR_EMPTY_TITLE: "O título da tarefa é obrigatório."
};
```

### 3. Criar Hook Personalizado

Implemente a lógica de gerenciamento de estado em `src/hooks/useTasks.ts`:

- Gerenciar o estado das tarefas
- Implementar operações CRUD (Create, Read, Update, Delete)
- Função `updateTask()` para editar tarefas existentes
- Tratar erros e estados de carregamento
- Validar dados antes de enviar para a API (ex: título obrigatório)
- Exibir mensagens de erro recebidas do backend

**Tratamento de erros do backend:**

```typescript
if (!response.ok) {
  const errorData = await response.json();
  setError(errorData.error || "Mensagem padrão");
  return false;
}
```

O backend retorna mensagens de erro específicas que são exibidas diretamente ao usuário.

### 4. Desenvolver Componentes

Desenvolva os componentes nesta ordem sugerida:

1. **ErrorMessage.tsx**: Para exibir mensagens de erro
2. **TaskHeader.tsx**: Cabeçalho da aplicação
3. **TaskForm.tsx**: Formulário para criar novas tarefas
4. **TaskItem.tsx**: Componente para cada tarefa individual com edição inline
   - Inclui confirmação antes de deletar usando `window.confirm()`
5. **TaskList.tsx**: Lista que renderiza todas as tarefas
6. **TaskFooter.tsx**: Área com estatísticas e filtros
7. **TaskApp.tsx**: Componente principal que integra tudo

### 5. Confirmação de Exclusão

Para evitar exclusões acidentais, use confirmação nativa:

```typescript
<button
  onClick={() => {
    if (window.confirm('Tem certeza que deseja remover esta tarefa?')) {
      onDelete(task.id);
    }
  }}
  className="action-button delete-button"
>
  Remover
</button>
```

## Estrutura de Dados Principal

A interface principal que define uma tarefa:

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

## Validação e Mensagens de Erro

A aplicação implementa validação de dados e feedback ao usuário:

### Validação de Título

O campo título é **obrigatório** tanto ao criar quanto ao editar tarefas:

- **Ao criar nova tarefa**: O botão "Adicionar Tarefa" fica desabilitado se o título estiver vazio. Se tentar submeter, a mensagem de erro global é exibida.

- **Ao editar tarefa existente**: Se tentar salvar uma edição sem título, uma mensagem de erro aparece diretamente no formulário de edição inline.

### Mensagens de Erro Implementadas

- **ERROR_LOAD**: Exibida quando falha ao carregar as tarefas do servidor
- **ERROR_CREATE**: Exibida quando falha ao criar uma nova tarefa
- **ERROR_UPDATE**: Exibida quando falha ao atualizar uma tarefa existente
- **ERROR_DELETE**: Exibida quando falha ao deletar uma tarefa
- **ERROR_EMPTY_TITLE**: Exibida quando tenta criar ou editar uma tarefa sem título

### Componente ErrorMessage

O componente `ErrorMessage.tsx` exibe erros globais no topo da aplicação, logo abaixo do cabeçalho. Erros de validação no formulário de edição são exibidos localmente dentro do próprio item.
  completed: boolean;
  createdAt?: string;
  updatedAt?: string;
}
```
