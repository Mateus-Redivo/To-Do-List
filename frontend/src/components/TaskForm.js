import React from 'react';
import PropTypes from 'prop-types';

function TaskForm({ form, onSubmit, onChange, submitting = false }) {
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
            rows="3"
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

TaskForm.propTypes = {
  form: PropTypes.shape({
    title: PropTypes.string.isRequired,
    description: PropTypes.string.isRequired,
  }).isRequired,
  onSubmit: PropTypes.func.isRequired,
  onChange: PropTypes.func.isRequired,
  submitting: PropTypes.bool,
};

export default TaskForm;
