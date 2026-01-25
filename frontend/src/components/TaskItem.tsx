import { useState } from 'react';
import type { TaskItemProps, TaskFormData } from '../types';

function TaskItem({ task, onToggle, onDelete, onEdit }: Readonly<TaskItemProps>) {
  const [isEditing, setIsEditing] = useState(false);
  const [editForm, setEditForm] = useState<TaskFormData>({
    title: task.title,
    description: task.description
  });

  function handleEdit() {
    setIsEditing(true);
  }

  function handleCancel() {
    setIsEditing(false);
    setEditForm({ title: task.title, description: task.description });
  }

  async function handleSave() {
    if (editForm.title.trim()) {
      onEdit(task.id, editForm);
      setIsEditing(false);
    }
  }

  if (isEditing) {
    return (
      <div className="task-item editing">
        <div className="edit-form">
          <input
            type="text"
            value={editForm.title}
            onChange={(e) => setEditForm({ ...editForm, title: e.target.value })}
            className="edit-input"
            placeholder="Título da tarefa"
          />
          <textarea
            value={editForm.description}
            onChange={(e) => setEditForm({ ...editForm, description: e.target.value })}
            className="edit-textarea"
            placeholder="Descrição (opcional)"
          />
          <div className="edit-actions">
            <button onClick={handleSave} className="action-button save-button">
              Salvar
            </button>
            <button onClick={handleCancel} className="action-button cancel-button">
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
          >
            Editar
          </button>
          <button
            onClick={() => onToggle(task.id)}
            className={`action-button ${task.completed ? 'undo-button' : 'toggle-button'}`}
          >
            {task.completed ? "Desfazer" : "Concluir"}
          </button>
          <button
            onClick={() => onDelete(task.id)}
            className="action-button delete-button"
          >
            Remover
          </button>
        </div>
      </div>
    </div>
  );
}

export default TaskItem;