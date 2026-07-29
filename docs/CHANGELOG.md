# Histórico de Mudanças

Todas as mudanças notáveis no projeto TODO List serão documentadas neste arquivo.

O formato é baseado em [Keep a Changelog](https://keepachangelog.com/pt-BR/1.0.0/),
e este projeto adere ao [Versionamento Semântico](https://semver.org/lang/pt-BR/).

## [Não Lançado]

### Corrigido

- **Backend**
  - Descrições entre 256 e 500 caracteres passavam na validação da API mas eram rejeitadas pelo
    banco, resultando em `500 Internal Server Error`. As colunas `title` e `description` agora
    declaram `length` alinhado aos limites de `@Size` do `TaskDTO` (100 e 500)
  - Um `PUT` que omitisse `completed` marcava a tarefa como não concluída, porque o campo era um
    `boolean` primitivo e o Jackson preenchia a ausência com `false`. O campo virou `Boolean` e o
    service só sobrescreve o estado quando o cliente informa o valor
  - `GET /api/tasks/abc` (id não numérico) e corpos com JSON malformado retornavam `500`; agora
    retornam `400`, com handlers dedicados para `MethodArgumentTypeMismatchException` e
    `HttpMessageNotReadableException`
  - Método HTTP não suportado agora retorna `405 Method Not Allowed` com o cabeçalho `Allow`,
    em vez de `500`
  - `updateTask` e `toggleTaskCompletion` passaram a ser `@Transactional`: liam e gravavam em
    transações separadas, abrindo janela para *lost update* entre requisições concorrentes
  - `/v3/api-docs` retornava `500` (`NoSuchMethodError: ControllerAdviceBean`) porque o SpringDoc
    2.5.0 é incompatível com o Spring Framework 6.2 usado pelo Spring Boot 3.5. A dependência foi
    atualizada para 2.8.13 e o Swagger UI voltou a funcionar

- **Testes**
  - A suíte não rodava sem Docker no ar: `TaskRepositoryTest` e `TodolistApiApplicationTests`
    usavam o datasource de desenvolvimento (MySQL em `localhost:3406`), falhando com
    `Failed to load ApplicationContext`. Ambos passaram a usar `@ActiveProfiles("test")` com H2
    em memória

### Adicionado

- **Backend**
  - `OpenApiConfig` com os metadados globais da documentação (título, descrição, versão lida do
    `pom.xml`, contato e licença)
  - Schemas de erro documentados no Swagger via `@ApiResponse` + `@Content`, e descrições e
    exemplos por campo no `TaskDTO` via `@Schema`
  - Javadoc nas classes de `src/main`

- **Testes**
  - `GlobalExceptionHandlerTest` — 9 testes cobrindo os caminhos de erro da API
  - Testes de regressão para os dois bugs corrigidos: descrição de 500 caracteres persistida sem
    truncamento, e `PUT` sem `completed` preservando o estado atual
  - `src/test/resources/application-test.properties` — profile `test` com H2 em memória
  - JaCoCo configurado no `pom.xml`: `mvn test` gera o relatório em `target/site/jacoco/` e
    `mvn verify` reprova o build abaixo de 80% de cobertura de linhas
  - `OpenApiDocsTest` — 3 testes que sobem um servidor HTTP real e verificam que `/v3/api-docs` e
    o Swagger UI respondem e descrevem todos os endpoints. Foi este teste que revelou a
    incompatibilidade do SpringDoc
  - A suíte passou de 37 para 54 testes, todos verdes e sem dependência de banco externo

- **Relatórios**
  - `maven-javadoc-plugin`: `./mvnw javadoc:javadoc` gera o Javadoc navegável em
    `target/reports/apidocs/index.html`
  - `maven-surefire-report-plugin`: `./mvnw surefire-report:report` gera o relatório HTML dos
    testes em `target/reports/surefire.html`
  - O CI publica Javadoc, relatório de testes e cobertura como artefatos do workflow

- **Frontend Mobile**
  - Camada de serviço `src/services/taskApi.ts` separando chamadas HTTP do hook de estado
  - Suporte a `AbortController` no `fetchTasks` para cancelar requisições ao desmontar
  - Atualização otimista do estado local (sem re-fetch completo após mutações)

### Alterado

- **Backend**
  - `GlobalExceptionHandler` deixou de tratar `NoSuchElementException` — o handler era código
    morto, já que o service sinaliza "não encontrado" com `Optional.empty()`
  - `TaskDTO` passou a usar `@Schema` no lugar de `@JsonProperty`, que era redundante por os
    nomes dos campos JSON já coincidirem com os da classe

- **CI**
  - Workflow dividido em dois jobs: `backend-tests` roda a suíte com H2 (sem Docker) e verifica a
    cobertura; `docker-stack` sobe o compose completo e faz um smoke test do CRUD contra o MySQL
    real. Antes, um único job subia toda a stack só para então rodar `mvnw test` no host, contra
    o banco de desenvolvimento

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
  - Documentação Swagger/OpenAPI via SpringDoc
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
  - `TaskRepositoryTest` — integração com banco de dados
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

**Última atualização:** 29 de julho de 2026
