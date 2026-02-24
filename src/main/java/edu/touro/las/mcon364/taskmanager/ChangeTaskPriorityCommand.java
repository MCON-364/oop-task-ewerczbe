package edu.touro.las.mcon364.taskmanager;

public final class ChangeTaskPriorityCommand implements Command {
    private final TaskRegistry registry;
    private final String taskName;
    private final Priority newPriority;

    public ChangeTaskPriorityCommand(TaskRegistry registry, String taskName, Priority newPriority) {
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
        registry.get(taskName).ifPresentOrElse(task -> {
            // Create a new task with updated priority and replace the old task
            Task updatedTask = new Task(task.name(), newPriority);
            registry.add(updatedTask);
        }, () -> {
            throw new TaskNotFoundException("Task '" + taskName + "' not found.");
        });
    }
}