package com.todolist.api;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

/**
 * Classe base para testes de integração usando Testcontainers.
 * 
 * Esta classe configura um container MySQL que é compartilhado entre todos os testes
 * que estendem esta classe, garantindo que os testes rodem em um ambiente idêntico
 * ao de produção.
 * 
 * Uso:
 * - Faça sua classe de teste estender AbstractIntegrationTest
 * - O container será iniciado automaticamente antes dos testes
 * - O container será encerrado automaticamente após os testes
 * 
 * Exemplo:
 * {@code
 * class MyServiceTest extends AbstractIntegrationTest {
 *     @Test
 *     void myTest() {
 *         // seu teste aqui
 *     }
 * }
 * }
 */
@SpringBootTest
@ActiveProfiles("test")
@Testcontainers
public abstract class AbstractIntegrationTest {

    /**
     * Container MySQL compartilhado entre todos os testes.
     * 
     * - @Container: Gerencia o ciclo de vida do container
     * - @ServiceConnection: Configura automaticamente o DataSource do Spring
     * - static: O container é iniciado uma vez e reutilizado
     * - mysql:8.0: Mesma versão usada em produção
     */
    @Container
    @ServiceConnection
    static MySQLContainer<?> mysqlContainer = new MySQLContainer<>("mysql:8.0")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test")
            .withReuse(true); // Reutiliza o container entre execuções para maior velocidade

    // O Spring Boot Testcontainers configura automaticamente:
    // - spring.datasource.url
    // - spring.datasource.username
    // - spring.datasource.password
    // - spring.datasource.driver-class-name
}
