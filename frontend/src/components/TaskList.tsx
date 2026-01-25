import TaskItem from './TaskItem';
import { MESSAGES } from '../utils/constants';
import type { TaskListProps } from '../types';

function TaskList({ tasks, loading, onToggle, onDelete, onEdit }: Readonly<TaskListProps>) {
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
              <div className="empty-icon"></div>
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
                  onEdit={onEdit}
                />
              ))}
            </div>
          )}
        </>
      )}
    </div>
  );
}

export default TaskList;