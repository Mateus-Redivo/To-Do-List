package com.todolist.api;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/**
 * TESTE DE FUMAÇA (SMOKE TEST)
 *
 * Sobe o contexto completo do Spring e falha se qualquer bean não puder ser criado —
 * dependência faltando, configuração inválida, conflito de nomes.
 *
 * @ActiveProfiles("test"): usa o H2 em memória de application-test.properties. Sem isso o
 * teste tentaria se conectar ao MySQL de desenvolvimento e falharia sem o Docker no ar.
 */
@SpringBootTest
@ActiveProfiles("test")
class TodolistApiApplicationTests {

	@Test
	void contextLoads() {
	}

}
