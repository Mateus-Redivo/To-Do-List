package com.todolist.api;

import org.junit.jupiter.api.Test;

/**
 * Teste de integração principal da aplicação.
 * 
 * Verifica se o contexto Spring Boot carrega corretamente com todas as dependências.
 * Usa Testcontainers com MySQL real através da classe AbstractIntegrationTest.
 */
class TodolistApiApplicationTests extends AbstractIntegrationTest {

	@Test
	void contextLoads() {
		// Verifica se a aplicação inicia sem erros com MySQL em container
	}

}
