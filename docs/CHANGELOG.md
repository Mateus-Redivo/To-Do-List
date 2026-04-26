# Histórico de Mudanças

Todas as mudanças notáveis no projeto TODO List serão documentadas neste arquivo.

O formato é baseado em [Keep a Changelog](https://keepachangelog.com/pt-BR/1.0.0/),
e este projeto adere ao [Versionamento Semântico](https://semver.org/lang/pt-BR/).

## [Não Lançado]

### Adicionado

- **Frontend Mobile**
  - Camada de serviço `src/services/taskApi.ts` separando chamadas HTTP do hook de estado
  - Suporte a `AbortController` no `fetchTasks` para cancelar requisições ao desmontar
  - Atualização otimista do estado local (sem re-fetch completo após mutações)

### Alterado

- **Frontend Mobile**
  - Fluxo de edição movido para `App.tsx` com `TaskForm` compartilhado entre criação e edição
  - `TaskApp.tsx` removido — lógica consolidada diretamente em `App.tsx`
  - `useTasks` refatorado para consumir `taskApi` e usar `useCallback` em todas as funções
  - `Task` simplificado: removidos campos opcionais `createdAt` e `updatedAt`

## [1.0.0] - 2026-01-26

### Lançamento Inicial

- **API Java Spring Boot** (porta 8080)
  - Endpoints CRUD para gerenciamento de tarefas
  - Endpoint PATCH para alternar status de conclusão
  - Validação de entrada com Bean Validation
  - `GlobalExceptionHandler` com tratamento para `MethodArgumentNotValidException`, `NoSuchElementException`, `IllegalArgumentException` e erros genéricos
  - Documentação Swagger/OpenAPI via SpringDoc 2.5.0
  - Spring Boot Actuator para monitoramento

- **Frontend Web React** (porta 80 em produção, 5173 em dev)
  - Interface estilizada com Tailwind CSS
  - Gerenciamento de estado com hook `useTasks`
  - Formulário de criação e edição inline de tarefas
  - Confirmação via `window.confirm()` antes de deletar
  - Propagação de mensagens de erro do backend para o usuário

- **Frontend Mobile React Native**
  - Aplicativo mobile com Expo ~52.0
  - Interface nativa para iOS e Android
  - Formulário compartilhado entre criação e edição
  - Confirmação via `Alert.alert()` antes de deletar
  - Integração com API REST

- **Banco de Dados MySQL** (`todolist_db`, porta 3406)
  - Container Docker para desenvolvimento
  - Container separado para testes (porta 3307)
  - Persistência de dados com volumes

- **Suporte Docker**
  - Dockerfile para backend
  - Dockerfile para frontend web
  - Orquestração com Docker Compose
  - Health checks para todos os serviços
  - Rede `api_network` compartilhada

- **Testes Automatizados** (backend)
  - `TaskControllerTest` — endpoints HTTP com MockMvc
  - `TaskServiceTest` — lógica de negócio com Mockito
  - `TaskRepositoryTest` — integração com banco H2
  - `TaskMapperTest` — conversões Entity↔DTO
  - `TaskTest` e `TaskDTOTest` — modelos e DTOs

- **Documentação**
  - `ARCHITECTURE.md` — diagrama e fluxos de dados
  - `BACKEND.md` — guia do backend Spring Boot
  - `FRONTEND.md` — guia do frontend web
  - `MOBILE.md` — guia do frontend mobile
  - `TESTS.md` — guia didático de testes
  - `CONTRIBUTING.md` — como contribuir
  - `CODE_OF_CONDUCT.md` — código de conduta
  - `SECURITY.md` — política de segurança

### Stack Técnico

| Tecnologia | Versão |
| ---------- | ------ |
| Java | 21 |
| Spring Boot | 3.5.10 |
| React | 19 |
| React Native | 0.76.5 |
| Expo | ~52.0.0 |
| MySQL | 8.0 |
| Maven | 3.9 |
| TypeScript | ~5.8 (web) / ~5.6 (mobile) |

---

**Última atualização:** 24 de abril de 2026
