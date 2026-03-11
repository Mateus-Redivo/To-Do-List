# Guia Completo para Criação da API To-Do List

Este guia detalha o passo a passo para criar uma API RESTful utilizando Spring Boot, seguindo a estrutura do projeto implementado.

## 1. Criação do Projeto

- Acesse o [Spring Initializr](https://start.spring.io/).
- Selecione as opções:
  - Project: Maven Project
  - Language: Java
  - Spring Boot: 3.5.4
  - Group: com.todolist
  - Artifact: todolist-api
  - Name: todolist-api
  - Description: API RESTful para gerenciamento de tarefas (To-Do List)
  - Package name: com.todolist.api
  - Packaging: Jar
  - Java: 21

### Dependências do projeto

- **spring-boot-starter-actuator**  
  Monitoramento e métricas do Spring Boot.

- **spring-boot-starter-data-jpa**  
  Suporte a JPA para persistência de dados.

- **spring-boot-starter-web**  
  Criação de aplicações web (REST, MVC).

- **spring-boot-starter-validation**  
  Validação de dados com Bean Validation.

- **spring-boot-devtools**  
  Ferramentas para desenvolvimento (hot reload, etc).  
  `scope: runtime`, `optional: true`

- **mysql-connector-j**  
  Driver JDBC para conexão com banco de dados MySQL.  
  `scope: runtime`

- **spring-boot-starter-test**  
  Dependências para testes (JUnit, Mockito, etc).  
  `scope: test`

- **jakarta.validation-api**  
  API de validação Jakarta Bean Validation.

- **springdoc-openapi-starter-webmvc-ui** (versão 2.6.0)  
  Geração automática de documentação Swagger/OpenAPI para APIs Spring Boot.

- **springdoc-openapi-starter-common** (versão 2.6.0)  
  Dependência comum para SpringDoc OpenAPI.

## 2. Estrutura de Pastas Implementada

```plaintext
src/main/java/com/rev/revisao/
  controller/
    TaskController.java
  dto/
    TaskDTO.java
  mapper/
    TaskMapper.java
  model/
    Task.java
  repository/
    TaskRepository.java
  service/
    TaskService.java
src/main/resources/
  application.properties
```

## 3. Configuração do Banco de Dados

No arquivo `application.properties`:

```properties
spring.application.name=revisao

# Configuração do MySQL
spring.datasource.url=jdbc:mysql://localhost:3406/revisao_db?allowPublicKeyRetrieval=true&useSSL=false
spring.datasource.username=${MYSQL_USER:revisao_user}
spring.datasource.password=${MYSQL_PASSWORD:revisao_user_password}
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# JPA/Hibernate
spring.jpa.hibernate.ddl-auto=update
#Caso queira criar o banco do zero alterne para auto=create, lembre de voltar ao update para nao zerar o banco a cada execução
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect

# Configurações do SpringDoc OpenAPI
springdoc.api-docs.path=/api-docs
springdoc.swagger-ui.path=/swagger-ui.html
springdoc.swagger-ui.operationsSorter=method
```

## 4. Modelo (Entidade Task)

```java
@Entity
@Table(name = "tasks")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column
    private String description;

    @Column(nullable = false)
    private Boolean completed = false;

    // Constructors, getters e setters
}
```

## 5. DTO (Data Transfer Object)

```java
public class TaskDTO {
    @JsonProperty("id")
    private Long id;

    @NotBlank(message = "Title is required")
    @Size(max = 100, message = "Title must be less than 100 characters")
    @JsonProperty("title")
    private String title;

    @Size(max = 500, message = "Description must be less than 500 characters")
    @JsonProperty("description")
    private String description;

    @JsonProperty("completed")
    private Boolean completed;

    // Constructors, getters e setters
}
```

## 6. Mapper

Implementação de um mapper para conversão entre Entity e DTO:

```java
@Component
public class TaskMapper {
    public TaskDTO toDTO(Task task);
    public Task toEntity(TaskDTO taskDTO);
    public void updateEntityFromDTO(TaskDTO taskDTO, Task task);
}
```

## 7. Repository

```java
@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
}
```

## 8. Service

```java
@Service
public class TaskService {
    // Métodos implementados:
    // - getAllTasks()
    // - getTaskById(Long id)
    // - createTask(TaskDTO taskDTO)
    // - updateTask(Long id, TaskDTO taskDTO)
    // - deleteTask(Long id)
    // - toggleTaskCompletion(Long id)
}
```

## 9. Controller - Endpoints REST

```java
@RestController
@RequestMapping("/api/tasks")
public class TaskController {
    // GET /api/tasks - Listar todas as tarefas
    // GET /api/tasks/{id} - Buscar tarefa por ID
    // POST /api/tasks - Criar nova tarefa
    // PUT /api/tasks/{id} - Atualizar tarefa
    // DELETE /api/tasks/{id} - Deletar tarefa
    // PATCH /api/tasks/{id}/toggle - Alternar status da tarefa
}
```

### Endpoints Disponíveis

- **GET** `/api/tasks/{id}` - Busca tarefa específica
- **POST** `/api/tasks` - Cria nova tarefa
- **PUT** `/api/tasks/{id}` - Atualiza tarefa completa (título e descrição)
- **DELETE** `/api/tasks/{id}` - Remove tarefa
- **PATCH** `/api/tasks/{id}/toggle` - Alterna status completed da tarefa

## 10. Docker e Containerização

O projeto inclui configuração Docker com `docker-compose.yml`:

- **MySQL**: Porta 3406 (container) → 3306 (host)
- **Backend**: Porta 8080
- Variáveis de ambiente configuradas para flexibilidade

## 11. Documentação da API

- **Swagger UI**: Disponível em `http://localhost:8080/swagger-ui.html`
- **OpenAPI Docs**: Disponível em `http://localhost:8080/api-docs`

## 12. Frontend React

Este projeto inclui um frontend desenvolvido em React que consome a API REST do backend.

### 12.1. Estrutura do Frontend

```plaintext
frontend/
  public/
    index.html
    favicon.ico
    manifest.json
  src/
    TaskApp.js          # Componente principal
    TaskApp.css         # Estilos principais
    index.js           # Ponto de entrada
    index.css          # Estilos globais
    components/
      TaskHeader.js     # Cabeçalho da aplicação
      TaskForm.js       # Formulário para criar tarefas
      TaskList.js       # Lista de tarefas
      TaskItem.js       # Item individual da tarefa com edição inline
      TaskFooter.js     # Rodapé com estatísticas
      ErrorMessage.js   # Componente de erro
    hooks/
      useTasks.js       # Hook personalizado para gerenciar estado
    utils/
      constants.js      # Constantes e configurações
```

### 12.2. Criação do Projeto Frontend

#### Passo 1: Criar o projeto React

```bash
npm create vite@latest frontend -- --template react-ts
cd frontend
```

Escolha o framework React, e a variante TypeScript + SWC para melhor tipagem e desenvolvimento.

#### Passo 2: Instalar dependências adicionais

```bash
npm install prop-types
```

#### Passo 3: Configurar as dependências no package.json

```json
{
  "name": "frontend",
  "version": "0.1.0",
  "private": true,
  "dependencies": {
    "@testing-library/dom": "^10.4.1",
    "@testing-library/jest-dom": "^6.6.4",
    "@testing-library/react": "^16.3.0",
    "@testing-library/user-event": "^13.5.0",
    "prop-types": "^15.8.1",
    "react": "^19.1.1",
    "react-dom": "^19.1.1",
    "react-scripts": "5.0.1",
    "web-vitals": "^2.1.4"
  },
  "scripts": {
    "start": "react-scripts start",
    "build": "react-scripts build",
    "test": "react-scripts test",
    "eject": "react-scripts eject"
  }
}
```

### 12.3. Configuração da API

#### constants.js

```javascript
export const API_URL = 'http://localhost:8080/api/tasks';

export const MESSAGES = {
  ERROR_LOAD: 'Erro ao carregar tarefas. Tente novamente.',
  ERROR_CREATE: 'Erro ao criar tarefa. Tente novamente.',
  ERROR_UPDATE: 'Erro ao atualizar tarefa. Tente novamente.',
  ERROR_DELETE: 'Erro ao excluir tarefa. Tente novamente.',
  SUCCESS_CREATE: 'Tarefa criada com sucesso!',
  SUCCESS_UPDATE: 'Tarefa atualizada com sucesso!',
  SUCCESS_DELETE: 'Tarefa excluída com sucesso!'
};
```

### 12.4. Hook Personalizado (useTasks.js)

O hook `useTasks` centraliza toda a lógica de gerenciamento de estado e comunicação com a API:

```javascript
import { useState, useEffect } from 'react';
import { API_URL, MESSAGES } from '../utils/constants';

export function useTasks() {
  const [tasks, setTasks] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const [submitting, setSubmitting] = useState(false);

  // Funções implementadas:
  // - fetchTasks(): Carrega todas as tarefas
  // - createTask(taskData): Cria nova tarefa
  // - updateTask(id, taskData): Atualiza tarefa existente
  // - toggleTask(id): Alterna status da tarefa
  // - deleteTask(id): Remove tarefa
  
  useEffect(() => {
    fetchTasks();
  }, []);

  return {
    tasks,
    loading,
    error,
    submitting,
    createTask,
    updateTask,
    toggleTask,
    deleteTask
  };
}
```

### 12.5. Componentes Principais

#### TaskApp.js (Componente Principal)

```javascript
import React, { useState } from "react";
import TaskHeader from './components/TaskHeader';
import TaskForm from './components/TaskForm';
import TaskList from './components/TaskList';
import TaskFooter from './components/TaskFooter';
import ErrorMessage from './components/ErrorMessage';
import { useTasks } from './hooks/useTasks';

function TaskApp() {
  const [form, setForm] = useState({ title: "", description: "" });
  const { tasks, loading, error, submitting, createTask, updateTask, toggleTask, deleteTask } = useTasks();
  
  // Lógica de manipulação de formulário e eventos
  // handleEdit(id, taskData): Atualiza tarefa existente
}
```

#### TaskForm.js (Formulário de Criação)

```javascript
import React from 'react';
import PropTypes from 'prop-types';

function TaskForm({ form, onSubmit, onChange, submitting }) {
  return (
    <form className="task-form" onSubmit={onSubmit}>
      <input
        type="text"
        name="title"
        placeholder="Título da tarefa"
        value={form.title}
        onChange={onChange}
        required
      />
      <textarea
        name="description"
        placeholder="Descrição (opcional)"
        value={form.description}
        onChange={onChange}
      />
      <button type="submit" disabled={submitting}>
        {submitting ? 'Adicionando...' : 'Adicionar Tarefa'}
      </button>
    </form>
  );
}
```

#### TaskList.js (Lista de Tarefas)

```javascript
import React from 'react';
import TaskItem from './TaskItem';

function TaskList({ tasks, onToggle, onDelete, loading }) {
  if (loading) return <div className="loading">Carregando tarefas...</div>;
  if (tasks.length === 0) return <div className="empty">Nenhuma tarefa encontrada.</div>;
  
  return (
    <div className="task-list">
      {tasks.map(task => (
        <TaskItem
          key={task.id}
          task={task}
          onToggle={onToggle}
          onDelete={onDelete}
        />
      ))}
    </div>
  );
}
```

### 12.6. Funcionalidades Implementadas

- ✅ **Listagem de tarefas**: Exibe todas as tarefas da API
- ✅ **Criação de tarefas**: Formulário para adicionar novas tarefas
- ✅ **Edição de tarefas**: Editar título e descrição de tarefas existentes
- ✅ **Toggle de status**: Marcar/desmarcar tarefas como concluídas
- ✅ **Exclusão de tarefas**: Remover tarefas da lista
- ✅ **Estados de loading**: Indicadores visuais durante operações
- ✅ **Tratamento de erros**: Mensagens de erro amigáveis
- ✅ **Design responsivo**: Interface adaptável a diferentes telas
- ✅ **Estatísticas**: Contador de tarefas totais e concluídas

### 12.7. Estilização (TaskApp.css)

```css
.task-app {
  max-width: 600px;
  margin: 0 auto;
  padding: 20px;
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif;
}

.task-form {
  background: white;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0,0,0,0.1);
  margin-bottom: 20px;
}

.task-item {
  display: flex;
  align-items: center;
  padding: 15px;
  border: 1px solid #ddd;
  border-radius: 8px;
  margin-bottom: 10px;
}

.task-item.completed {
  opacity: 0.6;
  text-decoration: line-through;
}
```

### 12.8. Configuração CORS

Para que o frontend React (porta 3000) se comunique com o backend (porta 8080), é necessário configurar CORS no backend Spring Boot:

```java
@Configuration
@EnableWebMvc
public class CorsConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins("http://localhost:3000")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}
```

### 12.9. Dockerfile para Frontend

```dockerfile
FROM node:18-alpine
WORKDIR /app
COPY package*.json ./
RUN npm install
COPY . .
EXPOSE 3000
CMD ["npm", "start"]
```

### 12.10. Executando o Frontend

#### Desenvolvimento Local

```bash
cd frontend
npm install
npm start
```

O frontend estará disponível em `http://localhost:3000`

#### Com Docker

```bash
docker-compose up -d
```

## 13. Execução da Aplicação

### Opção 1: Docker Compose

```bash
docker-compose up -d
```

### Opção 2: Execução Local

#### Backend

```bash
# Windows
mvnw.cmd spring-boot:run

# Linux/Mac
./mvnw spring-boot:run
```

#### Frontend

```bash
cd frontend
npm install
npm start
```

### Opção 3: IDE

Execute a classe principal da aplicação diretamente pela IDE para o backend.
Para o frontend, use `npm start` no terminal da IDE.

## 14. URLs da Aplicação

- **Frontend**: `http://localhost:3000`
- **Backend API**: `http://localhost:8080/api/tasks`
- **Swagger UI**: `http://localhost:8080/swagger-ui.html`
- **OpenAPI Docs**: `http://localhost:8080/api-docs`

## 15. Validações Implementadas

- **Title**: Obrigatório, máximo 100 caracteres
- **Description**: Opcional, máximo 500 caracteres
- **Completed**: Campo booleano com valor padrão false

## 16. Recursos Adicionais

- ✅ Validação de dados com Bean Validation
- ✅ Tratamento de erros com ResponseEntity
- ✅ Documentação automática com Swagger
- ✅ Mapper pattern para conversão Entity/DTO
- ✅ Configuração com variáveis de ambiente
- ✅ Containerização com Docker

## 17. Testando a API

Use os endpoints através de:

- **Postman** ou **Insomnia**
- **Swagger UI** (interface web)
- **cURL** ou ferramentas similares

Exemplo de requisição POST:

```json
{
  "title": "Estudar Spring Boot",
  "description": "Completar o tutorial de Spring Boot",
  "completed": false
}
```

---

**Frontend**: `http://localhost:3000`  
**Backend API**: `http://localhost:8080/api/tasks`  
**Documentação**: `http://localhost:8080/swagger-ui.html`
