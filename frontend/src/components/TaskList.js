import React from 'react';
import PropTypes from 'prop-types';
import TaskItem from './TaskItem';
import { MESSAGES } from '../utils/constants';

function TaskList({ tasks, loading, onToggle, onDelete }) {
  const completedCount = tasks.filter(task => task.completed).length;

  return (
    <div className="task-card">
      <div className="tasks-header">
        <h2 className="form-title">Suas Tarefas ({tasks.length})</h2>
        {tasks.length > 0 && (
          <span className="tasks-count">
            {completedCount} concluídas
          </span>
        )}
      </div>

      {loading ? (
        <div className="loading-container">
          <div className="loading-spinner"></div>
          <span>{MESSAGES.LOADING}</span>
        </div>
      ) : (
        <>
          {tasks.length === 0 ? (
            <div className="empty-state">
              <div className="empty-icon">📋</div>
              <h3 className="empty-title">{MESSAGES.EMPTY_TITLE}</h3>
              <p className="empty-description">
                {MESSAGES.EMPTY_DESCRIPTION}
              </p>
            </div>
          ) : (
            <div className="tasks-list">
              {tasks.map((task) => (
                <TaskItem
                  key={task.id}
                  task={task}
                  onToggle={onToggle}
                  onDelete={onDelete}
                />
              ))}
            </div>
          )}
        </>
      )}
    </div>
  );
}

TaskList.propTypes = {
  tasks: PropTypes.arrayOf(
    PropTypes.shape({
      id: PropTypes.oneOfType([PropTypes.string, PropTypes.number]).isRequired,
      title: PropTypes.string.isRequired,
      description: PropTypes.string,
      completed: PropTypes.bool.isRequired,
    })
  ).isRequired,
  loading: PropTypes.bool.isRequired,
  onToggle: PropTypes.func.isRequired,
  onDelete: PropTypes.func.isRequired,
};

export default TaskList;
