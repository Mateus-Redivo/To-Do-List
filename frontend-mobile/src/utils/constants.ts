/**
 * Constantes da aplicação
 * Centraliza URLs, mensagens e configurações
 */

import type { Messages } from '../types';

// URL base da API
export const API_URL = "{API_URL}:8080/api/tasks";

// Mensagens do sistema
export const MESSAGES: Messages = {
  LOADING: "Carregando tarefas...",
  EMPTY_TITLE: "Nenhuma tarefa encontrada",
  EMPTY_DESCRIPTION: "Adicione sua primeira tarefa acima para começar!",
  ERROR_LOAD: "Erro ao carregar tarefas. Verifique se o servidor está rodando.",
  ERROR_CREATE: "Erro ao adicionar tarefa. Tente novamente.",
  ERROR_UPDATE: "Erro ao atualizar tarefa. Tente novamente.",
  ERROR_DELETE: "Erro ao remover tarefa. Tente novamente."
};
