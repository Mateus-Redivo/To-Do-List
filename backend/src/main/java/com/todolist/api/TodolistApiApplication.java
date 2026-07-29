package com.todolist.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Ponto de entrada da API To-Do List.
 *
 * <p>A varredura de componentes parte deste pacote, por isso a classe fica na raiz de
 * {@code com.todolist.api}, acima de todos os demais pacotes.</p>
 */
@SpringBootApplication
public class TodolistApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(TodolistApiApplication.class, args);
	}

}
