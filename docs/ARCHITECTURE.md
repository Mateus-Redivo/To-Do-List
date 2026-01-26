# TODO List - Arquitetura

## Visão Geral

Aplicação full-stack para gerenciamento de tarefas com:

- **Frontend Web** (React + TypeScript)
- **Frontend Mobile** (React Native + Expo)
- **API REST** (Spring Boot + Java 21)
- **Banco de Dados** (MySQL 8.0)

## Arquitetura

```plaintext
Frontend Web (React)          Frontend Mobile (React Native)
     :80                              Expo
      |                                |
      +----------------+---------------+
                       |
                 /api/tasks (HTTP)
                       |
                       v
              API Spring Boot :8080
                (Controller)
                (Service)
                (Repository)
                       |
                  JDBC MySQL
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
Backend: TaskController.createTask()
↓
Backend: TaskService.createTask()
↓
Backend: TaskRepository.save()
↓
MySQL: INSERT INTO tasks
↓
Response: Task criada com ID
```

### 2. Listar Tarefas

```plaintext
Frontend → GET /api/tasks
↓
Backend: TaskController.getAllTasks()
↓
Backend: TaskService.getAllTasks()
↓
Backend: TaskRepository.findAll()
↓
MySQL: SELECT * FROM tasks
↓
Response: Array de Tasks
```

### 3. Atualizar Tarefa

```plaintext
Frontend → PUT /api/tasks/{id}
Body: { title: "...", description: "...", completed: true }
↓
Backend: TaskController.updateTask()
↓
Backend: TaskService.updateTask()
↓
Backend: TaskRepository.save()
↓
MySQL: UPDATE tasks SET ...
↓
Response: Task atualizada
```

### 4. Alternar Status (Toggle)

```plaintext
Frontend → PATCH /api/tasks/{id}/toggle
↓
Backend: TaskController.toggleTaskCompletion()
↓
Backend: TaskService.toggleTaskCompletion()
↓
MySQL: UPDATE tasks SET completed = NOT completed
↓
Response: Task com status alterado
```

### 5. Deletar Tarefa

```plaintext
Frontend → DELETE /api/tasks/{id}
↓
Backend: TaskController.deleteTask()
↓
Backend: TaskService.deleteTask()
↓
Backend: TaskRepository.deleteById()
↓
MySQL: DELETE FROM tasks WHERE id = ?
↓
Response: 204 No Content
```

## Containers Docker

| Serviço | Container | Porta | Descrição |
| ------- | --------- | ----- | --------- |
| Frontend Web | todolist_frontend_web | 80 | React + Vite |
| Backend API | todolist_backend_api | 8080 | Spring Boot |
| Database | todolist_backend_db | 3406 | MySQL 8.0 |
| Test DB | todolist_backend_test_db | 3307 | MySQL (testes) |

### Rede Docker

Todos os containers na rede: `api_network`

## Inicialização Rápida

```powershell
# Subir todos os serviços
docker-compose up -d

# Ver logs
docker logs -f todolist_backend_api

# Testar API
Invoke-RestMethod -Uri "http://localhost:8080/api/tasks" -Method GET

# Parar serviços
docker-compose down
```

## Stack Técnico

- **Backend:** Java 21 + Spring Boot 3.5.10
- **Frontend Web:** React 19 + TypeScript + Vite
- **Frontend Mobile:** React Native + Expo
- **Database:** MySQL 8.0
- **ORM:** Spring Data JPA
- **Build:** Maven 3.9
- **Container:** Docker + Docker Compose

---

**Última atualização:** 26 de janeiro de 2026
