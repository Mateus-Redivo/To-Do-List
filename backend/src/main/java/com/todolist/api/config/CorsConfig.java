package com.todolist.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Política de CORS da API.
 *
 * <p>As origens liberadas cobrem o frontend em produção via Docker (porta 3000) e o servidor de
 * desenvolvimento do Vite (porta 5173). Sem isso, o navegador bloquearia as chamadas do frontend,
 * que roda em porta diferente da API. O app mobile não é afetado: CORS é restrição de navegador.</p>
 */
@Configuration
public class CorsConfig {

    @Bean
    WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/api/**")
                        .allowedOrigins("http://localhost:3000", "http://localhost:5173", "http://127.0.0.1:5173")
                        .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
                        .allowedHeaders("Content-Type", "Authorization", "Accept")
                        .allowCredentials(true)
                        .maxAge(3600);
            }
        };
    }
}
