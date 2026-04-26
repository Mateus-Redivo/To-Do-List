export interface Task {
  id: number;
  title: string;
  description: string;
  completed: boolean;
  createdAt?: string;
  updatedAt?: string;
}

export interface TaskFormData {
  title: string;
  description: string;
}

export interface TaskFormProps {
  form: TaskFormData;
  onSubmit: (e: React.FormEvent<HTMLFormElement>) => void;
  onChange: (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => void;
  submitting?: boolean;
}

export interface TaskListProps {
  tasks: Task[];
  loading: boolean;
  onToggle: (id: number) => void;
  onDelete: (id: number) => void;
  onEdit: (id: number, taskData: TaskFormData) => Promise<boolean>;
}

export interface TaskItemProps {
  task: Task;
  onToggle: (id: number) => void;
  onDelete: (id: number) => void;
  onEdit: (id: number, taskData: TaskFormData) => Promise<boolean>;
}

export interface ErrorMessageProps {
  error: string;
  onDismiss?: () => void;
}

export interface UseTasksReturn {
  tasks: Task[];
  loading: boolean;
  error: string | null;
  submitting: boolean;
  clearError: () => void;
  createTask: (taskData: TaskFormData) => Promise<boolean>;
  updateTask: (id: number, taskData: TaskFormData) => Promise<boolean>;
  toggleTask: (id: number) => Promise<void>;
  deleteTask: (id: number) => Promise<void>;
  fetchTasks: () => Promise<void>;
}

export interface Messages {
  LOADING: string;
  EMPTY_TITLE: string;
  EMPTY_DESCRIPTION: string;
  ERROR_LOAD: string;
  ERROR_CREATE: string;
  ERROR_UPDATE: string;
  ERROR_DELETE: string;
  ERROR_EMPTY_TITLE: string;
  ERROR_CONNECTION: string;
}
