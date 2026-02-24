package edu.touro.las.mcon364.taskmanager;

import java.util.Optional;

public final class UpdateTaskCommand implements Command {
    private final TaskRegistry registry;
    private final String taskName;
    private final Priority newPriority;

    public UpdateTaskCommand(TaskRegistry registry, String taskName, Priority newPriority) {
        if (taskName == null || taskName.isEmpty()) {
            throw new IllegalArgumentException("Task name must not be null or empty");
        }
        if (newPriority == null) {
            throw new IllegalArgumentException("Priority cannot be null");
        }
        this.registry = registry;
        this.taskName = taskName;
        this.newPriority = newPriority;
    }

    @Override
    public void execute() {
        // Try to retrieve the task, no exception thrown
        Optional<Task> existing = registry.get(taskName);

        if (existing.isEmpty()) {
            // Gracefully handle if the task doesn't exist
            throw new TaskNotFoundException("Task '" + taskName + "' not found.");
        }

        // Proceed with update if task exists
        Task updated = new Task(existing.get().getName(), newPriority);  // Create a new task with the updated priority
        registry.add(updated);  // Replace the existing task with the updated one
    }
}