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
- **PUT /api/tasks/:id**: Atualiza uma tarefa existente
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
  ERROR_LOAD: "Erro ao carregar tarefas.",
  // Adicione outras mensagens conforme necessário
};
```

### 3. Criar Hook Personalizado

Implemente a lógica de gerenciamento de estado em `src/hooks/useTasks.ts`:
- Gerenciar o estado das tarefas
- Implementar operações CRUD
- Tratar erros e estados de carregamento

### 4. Desenvolver Componentes

Desenvolva os componentes nesta ordem sugerida:

1. **ErrorMessage.tsx**: Para exibir mensagens de erro
2. **TaskHeader.tsx**: Cabeçalho da aplicação
3. **TaskForm.tsx**: Formulário para criar novas tarefas
4. **TaskItem.tsx**: Componente para cada tarefa individual
5. **TaskList.tsx**: Lista que renderiza todas as tarefas
6. **TaskFooter.tsx**: Área com estatísticas e filtros
7. **TaskApp.tsx**: Componente principal que integra tudo

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
