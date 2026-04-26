import type { ErrorMessageProps } from '../types';

function ErrorMessage({ error, onDismiss }: Readonly<ErrorMessageProps>) {
  if (!error) return null;

  return (
    <div className="error-message" role="alert" aria-live="assertive">
      <div className="error-content">
        <span className="error-icon" aria-hidden="true">!</span>
        <span className="error-text">{error}</span>
        {onDismiss && (
          <button
            onClick={onDismiss}
            className="error-dismiss"
            aria-label="Fechar mensagem de erro"
          >
            ✕
          </button>
        )}
      </div>
    </div>
  );
}

export default ErrorMessage;
