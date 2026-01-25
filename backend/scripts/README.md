# Scripts do Backend

Esta pasta contém scripts utilitários para o backend da aplicação.

## Scripts Disponíveis

### `wait-for-mysql.sh`
**Descrição:** Aguarda o MySQL estar pronto antes de iniciar a aplicação Spring Boot.
**Uso:** Executado automaticamente pelo Docker na inicialização.
```bash
./wait-for-mysql.sh mysql 3306 java -jar app.jar
```

## Tornando Scripts Executáveis

Caso precise tornar os scripts executáveis:
```bash
chmod +x scripts/*.sh
```

## Estrutura Atual

```
backend/
├── scripts/
│   ├── wait-for-mysql.sh    # Script de inicialização
│   └── README.md           # Esta documentação
└── ...
```

## Health Check

O health check da aplicação é gerenciado automaticamente pelo Docker Compose:
```yaml
healthcheck:
  test: ["CMD", "curl", "-f", "http://localhost:8080/actuator/health"]
```