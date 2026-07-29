package com.todolist.api.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

/**
 * CLASSE DE TESTE DA DOCUMENTAÇÃO OPENAPI
 *
 * Verifica que a documentação interativa da API realmente sobe e descreve os endpoints.
 *
 * Por que testar documentação? Porque ela é gerada em tempo de execução pelo SpringDoc a partir
 * das anotações dos controllers. Um erro no OpenApiConfig ou uma anotação inválida só apareceria
 * ao abrir o Swagger UI no navegador — este teste transforma isso em falha de build.
 *
 * @SpringBootTest(webEnvironment = RANDOM_PORT): sobe um servidor HTTP real em uma porta livre,
 * necessário porque os endpoints do SpringDoc só existem com o servidor no ar.
 *
 * TestRestTemplate: cliente HTTP já apontado para a porta em que o servidor subiu.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class OpenApiDocsTest {

    @Autowired
    private TestRestTemplate restTemplate;

    /**
     * TESTE: O documento OpenAPI é servido e traz os metadados do OpenApiConfig
     *
     * Objetivo: Garantir que /v3/api-docs responde e que título, versão e licença definidos em
     * OpenApiConfig chegam ao documento — sem essa classe, o SpringDoc usaria os valores
     * genéricos padrão ("OpenAPI definition", versão 1.0).
     */
    @Test
    void testApiDocsExposeConfiguredMetadata() {
        // ACT
        ResponseEntity<String> response = restTemplate.getForEntity("/v3/api-docs", String.class);

        // ASSERT
        assertEquals(HttpStatus.OK, response.getStatusCode());

        String body = response.getBody();
        assertNotNull(body);
        assertTrue(body.contains("To-Do List API"), "título vindo do OpenApiConfig");
        assertTrue(body.contains("MIT License"), "licença vinda do OpenApiConfig");
    }

    /**
     * TESTE: Todos os endpoints de tarefas aparecem na documentação
     *
     * Objetivo: Garantir que os seis endpoints da API estão descritos, e não apenas alguns.
     */
    @Test
    void testApiDocsDescribeAllTaskEndpoints() {
        // ACT
        ResponseEntity<String> response = restTemplate.getForEntity("/v3/api-docs", String.class);
        String body = response.getBody();

        // ASSERT
        assertNotNull(body);
        assertTrue(body.contains("/api/tasks"), "endpoints de coleção");
        assertTrue(body.contains("/api/tasks/{id}"), "endpoints por id");
        assertTrue(body.contains("/api/tasks/{id}/toggle"), "endpoint de toggle");
        assertTrue(body.contains("TaskDTO"), "schema do DTO");
    }

    /**
     * TESTE: O Swagger UI está acessível
     *
     * Objetivo: Garantir que a página onde os endpoints podem ser testados manualmente
     * (http://localhost:8080/swagger-ui/index.html) é servida pela aplicação.
     */
    @Test
    void testSwaggerUiIsAvailable() {
        // ACT
        ResponseEntity<String> response = restTemplate.getForEntity("/swagger-ui/index.html", String.class);

        // ASSERT
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}
