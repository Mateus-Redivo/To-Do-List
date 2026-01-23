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

## Desenvolvimento

(A ser continuado...)
