import React from 'react';
import PropTypes from 'prop-types';

function ErrorMessage({ error, onDismiss }) {
  if (!error) return null;

  return (
    <div className="error-message">
      <div className="error-content">
        <span className="error-icon">⚠️</span>
        <span className="error-text">{error}</span>
        {onDismiss && (
          <button 
            onClick={onDismiss}
            className="error-dismiss"
            aria-label="Fechar erro"
          >
            ✕
          </button>
        )}
      </div>
    </div>
  );
}

ErrorMessage.propTypes = {
  error: PropTypes.string,
  onDismiss: PropTypes.func,
};

export default ErrorMessage;
