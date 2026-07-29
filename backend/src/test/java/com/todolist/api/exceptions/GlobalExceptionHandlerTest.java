package com.todolist.api.exceptions;

import com.todolist.api.controller.TaskController;
import com.todolist.api.dto.TaskDTO;
import com.todolist.api.service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * CLASSE DE TESTE DO TRATAMENTO GLOBAL DE ERROS
 *
 * O GlobalExceptionHandler decide qual status HTTP e qual corpo o cliente recebe quando algo
 * dá errado. Estes testes verificam esse contrato: cada tipo de falha deve produzir o status
 * correto e nunca vazar detalhes internos.
 *
 * Diferença para TaskControllerTest:
 * - TaskControllerTest: testa os caminhos em que tudo dá certo (e o 404 de "não encontrado")
 * - GlobalExceptionHandlerTest: testa os caminhos de erro
 *
 * setControllerAdvice(): registra o handler no MockMvc. Sem essa chamada o MockMvc standalone
 * ignoraria o @ControllerAdvice e usaria o tratamento padrão do Spring, e os testes estariam
 * verificando um comportamento que não é o da aplicação real.
 */
@ExtendWith(MockitoExtension.class)
class GlobalExceptionHandlerTest {

    private MockMvc mockMvc;

    @Mock
    private TaskService taskService;

    @InjectMocks
    private TaskController taskController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(taskController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    /**
     * TESTE: Título em branco
     *
     * Objetivo: Verificar que a validação @NotBlank do TaskDTO devolve 400 com a mensagem
     * associada ao campo, e não um erro genérico.
     *
     * O corpo da resposta é um mapa campo → mensagem, formato que permite ao frontend
     * destacar exatamente qual campo do formulário está errado.
     */
    @Test
    void testCreateTaskWithBlankTitleReturnsBadRequest() throws Exception {
        mockMvc.perform(post("/api/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"title\": \"\", \"description\": \"Sem título\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("Title is required"));
    }

    /**
     * TESTE: Título acima do tamanho máximo
     *
     * Objetivo: Verificar que a validação @Size(max = 100) é aplicada.
     */
    @Test
    void testCreateTaskWithTooLongTitleReturnsBadRequest() throws Exception {
        String longTitle = "a".repeat(101);

        mockMvc.perform(post("/api/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"title\": \"" + longTitle + "\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("Title must be less than 100 characters"));
    }

    /**
     * TESTE: ID que não é um número
     *
     * Objetivo: Verificar que GET /api/tasks/abc devolve 400, e não 500.
     *
     * Por que este teste existe: antes o caso caía no handler genérico de Exception e
     * retornava 500 Internal Server Error, culpando o servidor por um erro do cliente.
     */
    @Test
    void testGetTaskWithNonNumericIdReturnsBadRequest() throws Exception {
        mockMvc.perform(get("/api/tasks/abc"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Invalid value for parameter 'id'"));
    }

    /**
     * TESTE: JSON malformado
     *
     * Objetivo: Verificar que um corpo que o Jackson não consegue ler devolve 400.
     *
     * Assim como o teste acima, o caso antes resultava em 500.
     */
    @Test
    void testCreateTaskWithMalformedJsonReturnsBadRequest() throws Exception {
        mockMvc.perform(post("/api/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"title\": \"Sem fechar as chaves\""))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Malformed or missing request body"));
    }

    /**
     * TESTE: Corpo ausente em um POST
     *
     * Objetivo: Verificar que a falta do corpo obrigatório também devolve 400.
     */
    @Test
    void testCreateTaskWithoutBodyReturnsBadRequest() throws Exception {
        mockMvc.perform(post("/api/tasks")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Malformed or missing request body"));
    }

    /**
     * TESTE: Método HTTP não suportado
     *
     * Objetivo: Verificar que PATCH em /api/tasks (que só aceita GET e POST) devolve 405,
     * junto com o cabeçalho Allow informando quais métodos são aceitos.
     */
    @Test
    void testUnsupportedHttpMethodReturnsMethodNotAllowed() throws Exception {
        mockMvc.perform(patch("/api/tasks"))
                .andExpect(status().isMethodNotAllowed())
                .andExpect(header().exists("Allow"))
                .andExpect(jsonPath("$.error").value("Method PATCH is not supported for this endpoint"));
    }

    /**
     * TESTE: Falha inesperada na camada de serviço
     *
     * Objetivo: Verificar que uma exceção não prevista vira 500 com uma mensagem genérica.
     *
     * O ponto central é que a mensagem original da exceção NÃO aparece na resposta: detalhes
     * internos ficam no log do servidor, nunca na resposta HTTP.
     */
    @Test
    void testUnexpectedExceptionReturnsGenericServerError() throws Exception {
        when(taskService.getAllTasks())
                .thenThrow(new RuntimeException("Falha na conexão com o banco em 10.0.0.5"));

        mockMvc.perform(get("/api/tasks"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.error").value("Internal server error. Please try again."));
    }

    /**
     * TESTE: IllegalArgumentException vira 400
     *
     * Objetivo: Verificar que argumentos rejeitados pela lógica de negócio são tratados como
     * erro do cliente, e que a mensagem da exceção é repassada para ajudar na correção.
     */
    @Test
    void testIllegalArgumentReturnsBadRequestWithMessage() throws Exception {
        when(taskService.getAllTasks())
                .thenThrow(new IllegalArgumentException("Filtro inválido"));

        mockMvc.perform(get("/api/tasks"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Filtro inválido"));
    }

    /**
     * TESTE: Requisição válida não é afetada pelo handler
     *
     * Objetivo: Garantir que o tratamento de erros não interfere no caminho feliz.
     */
    @Test
    void testValidRequestIsNotAffectedByHandler() throws Exception {
        when(taskService.getAllTasks())
                .thenReturn(List.of(new TaskDTO(1L, "Test Task", "Test Description", false)));

        mockMvc.perform(get("/api/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Test Task"));
    }
}
