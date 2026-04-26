# TODO List - Arquitetura

## Visão Geral

Aplicação full-stack para gerenciamento de tarefas com:

- **Frontend Web** (React 19 + TypeScript + Vite)
- **Frontend Mobile** (React Native + Expo)
- **API REST** (Spring Boot 3.5.10 + Java 21)
- **Banco de Dados** (MySQL 8.0)

## Diagrama de Arquitetura

```plaintext
Frontend Web (React)          Frontend Mobile (React Native)
  :5173 (dev) / :80 (prod)            Expo / Metro
        |                                   |
        +------------------+----------------+
                           |
                    /api/tasks (HTTP)
                           |
                           v
                  API Spring Boot :8080
                    (TaskController)
                    (TaskService)
                    (TaskMapper)
                    (TaskRepository)
                           |
                      JDBC / JPA
                           |
                           v
                  MySQL Database :3406
                    (todolist_db)
```

## Fluxo de Dados

### 1. Criar Tarefa

```plaintext
Frontend → POST /api/tasks
Body: { title: "...", description: "...", completed: false }
↓
TaskController.createTask()
↓
TaskService.createTask()  →  TaskMapper.convertToEntity()
↓
TaskRepository.save()
↓
MySQL: INSERT INTO tasks
↓
Response 201: TaskDTO criado
```

### 2. Listar Tarefas

```plaintext
Frontend → GET /api/tasks
↓
TaskController.getAllTasks()
↓
TaskService.getAllTasks()  →  TaskMapper.convertToDTO()
↓
TaskRepository.findAll()
↓
MySQL: SELECT * FROM tasks
↓
Response 200: Array de TaskDTO
```

### 3. Atualizar Tarefa

```plaintext
Frontend → PUT /api/tasks/{id}
Body: { title: "...", description: "...", completed: true }
↓
TaskController.updateTask()
↓
TaskService.updateTask()
↓
TaskRepository.save()
↓
MySQL: UPDATE tasks SET ...
↓
Response 200: TaskDTO atualizado
```

### 4. Alternar Status (Toggle)

```plaintext
Frontend → PATCH /api/tasks/{id}/toggle
↓
TaskController.toggleTaskCompletion()
↓
TaskService.toggleTaskCompletion()
↓
MySQL: UPDATE tasks SET completed = !completed
↓
Response 200: TaskDTO com status alterado
```

### 5. Deletar Tarefa

```plaintext
Frontend → DELETE /api/tasks/{id}
↓
TaskController.deleteTask()
↓
TaskService.deleteTask()
↓
TaskRepository.deleteById()
↓
MySQL: DELETE FROM tasks WHERE id = ?
↓
Response 204: No Content
```

## Containers Docker

| Serviço | Container | Porta | Descrição |
| ------- | --------- | ----- | --------- |
| Frontend Web | todolist_frontend_web | 80 | React + Vite (build) |
| Backend API | todolist_backend_api | 8080 | Spring Boot |
| Database | todolist_backend_db | 3406 | MySQL 8.0 |
| Test DB | todolist_backend_test_db | 3307 | MySQL (testes) |

Todos os containers na rede: `api_network`

## Inicialização Rápida

```bash
# Subir todos os serviços
docker-compose up -d

# Ver logs da API
docker logs -f todolist_backend_api

# Testar API
curl http://localhost:8080/api/tasks

# Parar serviços
docker-compose down
```

## Stack Técnico

| Camada | Tecnologia |
| ------ | ---------- |
| Backend | Java 21 + Spring Boot 3.5.10 |
| Frontend Web | React 19 + TypeScript + Vite |
| Frontend Mobile | React Native 0.76.5 + Expo ~52.0 |
| Database | MySQL 8.0 |
| ORM | Spring Data JPA + Hibernate |
| Build (backend) | Maven 3.9 |
| Container | Docker + Docker Compose |

---

**Última atualização:** 24 de abril de 2026
