import React from 'react';
import { View, Text, TouchableOpacity, StyleSheet } from 'react-native';
import type { ErrorMessageProps } from '../types';

function ErrorMessage({ error, onDismiss }: ErrorMessageProps & { onDismiss?: () => void }) {
  if (!error) return null;

  return (
    <View style={styles.errorMessage}>
      <View style={styles.errorContent}>
        <Text style={styles.errorIcon}>!</Text>
        <Text style={styles.errorText}>{error}</Text>
        {onDismiss && (
          <TouchableOpacity 
            onPress={onDismiss}
            style={styles.errorDismiss}
            accessibilityLabel="Fechar erro"
          >
            <Text style={styles.dismissText}>×</Text>
          </TouchableOpacity>
        )}
      </View>
    </View>
  );
}

const styles = StyleSheet.create({
  errorMessage: {
    backgroundColor: '#fee',
    padding: 12,
    borderRadius: 8,
    marginVertical: 8,
  },
  errorContent: {
    flexDirection: 'row',
    alignItems: 'center',
  },
  errorIcon: {
    fontSize: 20,
    color: '#c00',
    fontWeight: 'bold',
    marginRight: 8,
  },
  errorText: {
    flex: 1,
    color: '#c00',
    fontSize: 14,
  },
  errorDismiss: {
    padding: 4,
    marginLeft: 8,
  },
  dismissText: {
    fontSize: 18,
    color: '#c00',
    fontWeight: 'bold',
  },
});

export default ErrorMessage;