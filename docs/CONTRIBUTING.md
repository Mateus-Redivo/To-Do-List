# Contribuindo para TODO List

Obrigado por considerar contribuir com o TODO List! Este documento fornece diretrizes e melhores práticas.

## Código de Conduta

- Seja respeitoso e inclusivo
- Forneça feedback construtivo
- Foque no que é melhor para o projeto

## Como Contribuir

### 1. Fork & Clone

```bash
git clone https://github.com/SEU-USUARIO/To-Do-List.git
cd To-Do-List
```

### 2. Crie uma Branch

```bash
git checkout -b feature/minha-funcionalidade
# ou
git checkout -b fix/issue-123
```

**Convenções de nomenclatura de branches:**

- `feature/` - Novas funcionalidades
- `fix/` - Correções de bugs
- `docs/` - Alterações na documentação
- `test/` - Adições/correções de testes
- `refactor/` - Refatoração de código

### 3. Faça Suas Alterações

#### Antes de fazer commit

```bash
# Execute os testes do backend
cd backend && ./mvnw test

# Execute com Docker
docker-compose up -d
```

### 4. Padrões de Commit

Siga [Conventional Commits](https://www.conventionalcommits.org/):

```text
feat: adiciona filtro de tarefas concluídas
fix: corrige validação de título vazio
docs: atualiza README com exemplos
test: adiciona testes para TaskService
refactor: melhora estrutura dos componentes React
style: ajusta formatação do código
```

### 5. Push e Pull Request

```bash
git push origin feature/minha-funcionalidade
```

Então crie um Pull Request no GitHub preenchendo o template.

## Requisitos de Testes

### Testes Java (Backend)

- **Localização:** `backend/src/test/java/`
- **Executar:** `cd backend && ./mvnw test`
- **Cobertura mínima:** 75%

### Testes Docker

```bash
docker-compose up -d
# Verifique os health checks
docker-compose ps
```

## Estilo de Código

### Java

- Siga as convenções de nomenclatura Java
- Adicione JavaDoc para métodos públicos
- Comprimento máximo de linha: 120 caracteres
- Use Bean Validation para validações

### TypeScript/React

- Siga as convenções do ESLint configurado
- Use TypeScript para tipagem forte
- Componentes funcionais com hooks
- Comprimento máximo de linha: 100 caracteres

## Processo de Revisão de Pull Request

1. Verificações automáticas devem passar (build, testes)
2. Revisão de código pelos mantenedores
3. Endereçar quaisquer alterações solicitadas
4. Aprovação e merge

## Reportando Issues

Ao reportar bugs, inclua:

- Título claro e descritivo
- Passos detalhados para reproduzir
- Comportamento esperado
- Comportamento atual
- Capturas de tela (se aplicável)
- Detalhes do ambiente:
  - Sistema operacional
  - Versão do Docker
  - Versão do Java
  - Versão do Node.js
- Logs ou mensagens de erro relevantes

## Solicitações de Funcionalidades

Ao sugerir funcionalidades, inclua:

- Caso de uso e motivação
- Solução proposta
- Abordagens alternativas consideradas
- Impacto na funcionalidade existente

## Recursos

- [README do Projeto](../README.md)
- [Documentação](docs/)
- [Swagger UI](http://localhost:8080/swagger-ui.html) (ao rodar localmente)

---

Obrigado por contribuir com o TODO List!
