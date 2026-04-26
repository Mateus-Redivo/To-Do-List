# Política de Segurança

## Versões Suportadas

| Versão | Suportada |
| ------ | --------- |
| 1.0.x  | Sim       |
| < 1.0  | Não       |

## Reportando uma Vulnerabilidade

A segurança do TODO List é levada a sério. Se você descobrir uma vulnerabilidade de segurança, por favor siga estas diretrizes:

### NÃO use Issues Públicas

Por favor, **não** reporte vulnerabilidades de segurança através de issues públicas no GitHub.

### Processo de Reporte

1. **Entre em contato por e-mail privado** com os mantenedores do projeto
2. **Inclua o máximo de informações possível:**
   - Descrição detalhada da vulnerabilidade
   - Passos para reproduzir
   - Impacto potencial
   - Possíveis soluções (se tiver)

### Tempo de Resposta

- **Resposta inicial:** Dentro de 48 horas
- **Atualizações:** A cada 7 dias até resolução
- **Correção:** Objetivo de 30 dias para vulnerabilidades críticas

## Melhores Práticas de Segurança

### Para Usuários

- Use sempre a versão mais recente do TODO List
- Mantenha o Docker e dependências atualizados
- Use conexões HTTPS em produção
- Não exponha portas de serviços diretamente sem firewall
- Não commite arquivos `.env` com credenciais

### Para Contribuidores

- Siga práticas de codificação segura
- Valide todas as entradas de usuário
- Use queries parametrizadas (não concatene SQL)
- Não inclua segredos ou credenciais no código
- Execute testes de segurança antes de submeter PRs

## Recursos de Segurança

O TODO List implementa as seguintes medidas de segurança:

- **Validação de Entrada:** Bean Validation no Java
- **Tratamento de Erros:** Manipuladores de exceção que não expõem detalhes internos
- **JPA/Hibernate:** Proteção contra SQL Injection
- **Docker:** Imagens baseadas em versões oficiais e atualizadas
- **Sem Hardcode:** Todas as credenciais devem ser via variáveis de ambiente
- **CORS:** Configuração apropriada para comunicação frontend-backend

## Problemas de Segurança Conhecidos

Atualmente não há problemas de segurança conhecidos.

## Agradecimentos

Agradecemos a todos que reportam vulnerabilidades de forma responsável e nos ajudam a manter o TODO List seguro.

---

**Última atualização:** 24 de abril de 2026
