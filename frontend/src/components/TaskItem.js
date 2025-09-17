import React from 'react';
import PropTypes from 'prop-types';

function TaskItem({ task, onToggle, onDelete }) {
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

TaskItem.propTypes = {
  task: PropTypes.shape({
    id: PropTypes.oneOfType([PropTypes.string, PropTypes.number]).isRequired,
    title: PropTypes.string.isRequired,
    description: PropTypes.string,
    completed: PropTypes.bool.isRequired,
  }).isRequired,
  onToggle: PropTypes.func.isRequired,
  onDelete: PropTypes.func.isRequired,
};

export default TaskItem;
