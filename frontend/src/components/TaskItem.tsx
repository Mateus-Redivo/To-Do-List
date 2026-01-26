import { useState } from 'react';
import type { TaskItemProps, TaskFormData } from '../types';

function TaskItem({ task, onToggle, onDelete, onEdit }: Readonly<TaskItemProps>) {
  const [isEditing, setIsEditing] = useState(false);
  const [editForm, setEditForm] = useState<TaskFormData>({
    title: task.title,
    description: task.description
  });
  const [editError, setEditError] = useState<string>('');

  function handleEdit() {
    setIsEditing(true);
    setEditError('');
  }

  function handleCancel() {
    setIsEditing(false);
    setEditForm({ title: task.title, description: task.description });
    setEditError('');
  }

  async function handleSave() {
    if (!editForm.title.trim()) {
      setEditError('O título da tarefa é obrigatório.');
      return;
    }
    
    setEditError('');
    onEdit(task.id, editForm);
    setIsEditing(false);
  }

  if (isEditing) {
    return (
      <div className="task-item editing">
        <div className="edit-form">
          {editError && (
            <div style={{ 
              color: '#ef4444', 
              fontSize: '14px', 
              marginBottom: '8px',
              padding: '8px',
              backgroundColor: '#fee2e2',
              borderRadius: '4px',
              border: '1px solid #fecaca'
            }}>
              {editError}
            </div>
          )}
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