# Histórico de Mudanças

Todas as mudanças notáveis no projeto TODO List serão documentadas neste arquivo.

O formato é baseado em [Keep a Changelog](https://keepachangelog.com/pt-BR/1.0.0/),
e este projeto adere ao [Versionamento Semântico](https://semver.org/lang/pt-BR/).

## [Não Lançado]

### Adicionado (Melhorias Recentes)

- **Backend**
  - `GlobalExceptionHandler` para tratamento centralizado de erros
  - Respostas de erro padronizadas com mensagens específicas
  - Retorno JSON estruturado para erros de validação (400 Bad Request)
  
- **Frontend Web & Mobile**
  - Confirmação antes de deletar tarefas (window.confirm no web, Alert.alert no mobile)
  - Tratamento de mensagens de erro recebidas do backend
  - Exibição de mensagens de validação específicas ao usuário
  - Diferenciação entre erro de conexão e erro de validação

### Melhorado

- **UX**: Usuário agora recebe feedback mais claro sobre erros de validação
- **Segurança**: Confirmação obrigatória para ações destrutivas
- **Manutenibilidade**: Mensagens de erro centralizadas no backend

## [1.0.0] - 2026-01-26

### Lançamento Inicial

- **API Java Spring Boot** (Porta 8080)
  - Endpoints CRUD para gerenciamento de tarefas
  - Endpoint para alternar status de conclusão
  - Validação de entrada com Bean Validation
  - Tratamento de exceções com manipulador global
  - Documentação Swagger/OpenAPI
  
- **Frontend Web React** (Porta 80)
  - Interface responsiva com Tailwind CSS
  - Gerenciamento de estado com hooks customizados
  - Formulário de criação e edição de tarefas
  - Lista de tarefas com filtros
  - Integração com API REST
  
- **Frontend Mobile React Native**
  - Aplicativo mobile com Expo
  - Interface nativa para iOS e Android
  - Sincronização com API
  
- **Banco de Dados MySQL**
  - Container Docker para desenvolvimento
  - Container separado para testes
  - Persistência de dados com volumes
  
- **Suporte Docker**
  - Dockerfile para backend
  - Dockerfile para frontend web
  - Orquestração com Docker Compose
  - Health checks para todos os serviços
  - Perfil específico para execução de testes
  
- **Testes Automatizados**
  - Testes unitários para todos os componentes
  - Testes de integração
  - Cobertura de código
  
- **Documentação**
  - README.md completo
  - Guia de arquitetura
  - Documentação do backend
  - Documentação do frontend
  - Guia de testes
  - CONTRIBUTING.md
  - CODE_OF_CONDUCT.md
  - SECURITY.md

### Stack Técnico

- Java 21 + Spring Boot 3.5.10
- React 19 + TypeScript + Vite
- React Native + Expo
- MySQL 8.0
- Docker + Docker Compose
- Maven 3.9
- Spring Data JPA

### Funcionalidades

- Criar, ler, atualizar e deletar tarefas
- Alternar status de conclusão
- Validação de dados no frontend e backend
- Interface responsiva
- Aplicativo mobile nativo
- Persistência de dados
- Containerização completa

---

**Última atualização:** 26 de janeiro de 2026
