import type { ErrorMessageProps } from '../types';

function ErrorMessage({ error, onDismiss }: ErrorMessageProps & { onDismiss?: () => void }) {
  if (!error) return null;

  return (
    <div className="error-message">
      <div className="error-content">
        <span className="error-icon">!</span>
        <span className="error-text">{error}</span>
        {onDismiss && (
          <button 
            onClick={onDismiss}
            className="error-dismiss"
            aria-label="Fechar erro"
          >
            X
          </button>
        )}
      </div>
    </div>
  );
}

export default ErrorMessage;