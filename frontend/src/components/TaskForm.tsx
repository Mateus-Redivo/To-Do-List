import type { TaskFormProps } from '../types';

function TaskForm({ form, onSubmit, onChange, submitting = false }: Readonly<TaskFormProps>) {
  return (
    <div className="task-card">
      <h2 className="form-title">Adicionar Nova Tarefa</h2>
      <form onSubmit={onSubmit}>
        <div className="form-group">
          <label className="form-label" htmlFor="title">
            Título
          </label>
          <input
            id="title"
            name="title"
            className="form-input"
            placeholder="Digite o título da tarefa..."
            value={form.title}
            onChange={onChange}
            required
            disabled={submitting}
          />
        </div>
        
        <div className="form-group">
          <label className="form-label" htmlFor="description">
            Descrição
          </label>
          <textarea
            id="description"
            name="description"
            className="form-textarea"
            placeholder="Adicione uma descrição (opcional)..."
            value={form.description}
            onChange={onChange}
            rows={3}
            disabled={submitting}
          />
        </div>
        
        <button 
          type="submit" 
          className="submit-button"
          disabled={submitting || !form.title.trim()}
        >
          {submitting ? "Adicionando..." : "Adicionar Tarefa"}
        </button>
      </form>
    </div>
  );
}

export default TaskForm;