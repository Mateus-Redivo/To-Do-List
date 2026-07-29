package com.todolist.api.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Metadados globais exibidos no topo do Swagger UI.
 *
 * <p>O SpringDoc descobre os endpoints sozinho a partir das anotações dos controllers; sem esta
 * configuração o cabeçalho da documentação ficaria com os valores genéricos padrão
 * ("OpenAPI definition", versão 1.0).</p>
 */
@Configuration
public class OpenApiConfig {

    private final String applicationVersion;

    /** A versão vem do {@code pom.xml} para não precisar ser mantida em dois lugares. */
    public OpenApiConfig(@Value("${build.version:1.0.0}") String applicationVersion) {
        this.applicationVersion = applicationVersion;
    }

    @Bean
    OpenAPI todolistOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("To-Do List API")
                        .description("""
                                API REST para gerenciamento de tarefas.

                                Cada tarefa possui título, descrição opcional e um estado de conclusão. \
                                Além do CRUD completo, há um endpoint dedicado para alternar a conclusão \
                                sem precisar reenviar a tarefa inteira.""")
                        .version(applicationVersion)
                        .contact(new Contact()
                                .name("Mateus Redivo")
                                .url("https://github.com/Mateus-Redivo"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")));
    }
}
