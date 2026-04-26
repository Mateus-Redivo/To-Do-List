import { useState, useRef, useEffect } from 'react';
import type { TaskItemProps, TaskFormData } from '../types';

function TaskItem({ task, onToggle, onDelete, onEdit }: Readonly<TaskItemProps>) {
  const [isEditing, setIsEditing] = useState(false);
  const [editForm, setEditForm] = useState<TaskFormData>({
    title: task.title,
    description: task.description,
  });
  const [editError, setEditError] = useState<string>('');
  const [saving, setSaving] = useState(false);
  const titleInputRef = useRef<HTMLInputElement>(null);

  useEffect(() => {
    if (isEditing) {
      titleInputRef.current?.focus();
    }
  }, [isEditing]);

  function handleEdit() {
    setEditForm({ title: task.title, description: task.description });
    setEditError('');
    setIsEditing(true);
  }

  function handleCancel() {
    setIsEditing(false);
    setEditForm({ title: task.title, description: task.description });
    setEditError('');
  }

  async function handleSave() {
    if (!editForm.title.trim()) {
      setEditError('O título da tarefa é obrigatório.');
      titleInputRef.current?.focus();
      return;
    }

    setSaving(true);
    setEditError('');
    const success = await onEdit(task.id, editForm);
    setSaving(false);
    if (success) {
      setIsEditing(false);
    }
  }

  function handleTitleKeyDown(e: React.KeyboardEvent<HTMLInputElement>) {
    if (e.key === 'Enter') {
      e.preventDefault();
      handleSave();
    } else if (e.key === 'Escape') {
      handleCancel();
    }
  }

  function handleTextareaKeyDown(e: React.KeyboardEvent<HTMLTextAreaElement>) {
    if (e.key === 'Escape') {
      handleCancel();
    }
  }

  if (isEditing) {
    return (
      <div className="task-item editing">
        <div className="edit-form">
          {editError && (
            <div className="edit-error" role="alert">
              {editError}
            </div>
          )}
          <input
            ref={titleInputRef}
            type="text"
            value={editForm.title}
            onChange={(e) => setEditForm({ ...editForm, title: e.target.value })}
            onKeyDown={handleTitleKeyDown}
            className="edit-input"
            placeholder="Título da tarefa"
            disabled={saving}
            aria-label="Título da tarefa"
            aria-invalid={!!editError}
          />
          <textarea
            value={editForm.description}
            onChange={(e) => setEditForm({ ...editForm, description: e.target.value })}
            onKeyDown={handleTextareaKeyDown}
            className="edit-textarea"
            placeholder="Descrição (opcional)"
            disabled={saving}
            aria-label="Descrição da tarefa"
          />
          <div className="edit-actions">
            <button
              onClick={handleSave}
              className="action-button save-button"
              disabled={saving}
              aria-busy={saving}
            >
              {saving ? 'Salvando...' : 'Salvar'}
            </button>
            <button
              onClick={handleCancel}
              className="action-button cancel-button"
              disabled={saving}
            >
              Cancelar
            </button>
          </div>
        </div>
      </div>
    );
  }

  return (
    <div className={`task-item ${task.completed ? 'completed' : 'active'}`}>
      <div className="task-content">
        <div className="task-info">
          <h3 className={`task-item-title ${task.completed ? 'completed' : 'active'}`}>
            {task.title}
          </h3>
          {task.description && (
            <p className={`task-description ${task.completed ? 'completed' : 'active'}`}>
              {task.description}
            </p>
          )}
        </div>

        <div className="task-actions">
          <button
            onClick={handleEdit}
            className="action-button edit-button"
            aria-label={`Editar tarefa "${task.title}"`}
          >
            Editar
          </button>
          <button
            onClick={() => onToggle(task.id)}
            className={`action-button ${task.completed ? 'undo-button' : 'toggle-button'}`}
            aria-label={task.completed ? `Desfazer conclusão de "${task.title}"` : `Concluir tarefa "${task.title}"`}
          >
            {task.completed ? 'Desfazer' : 'Concluir'}
          </button>
          <button
            onClick={() => {
              if (globalThis.confirm('Tem certeza que deseja remover esta tarefa?')) {
                onDelete(task.id);
              }
            }}
            className="action-button delete-button"
            aria-label={`Remover tarefa "${task.title}"`}
          >
            Remover
          </button>
        </div>
      </div>
    </div>
  );
}

export default TaskItem;
