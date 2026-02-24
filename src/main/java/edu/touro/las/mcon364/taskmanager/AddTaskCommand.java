package edu.touro.las.mcon364.taskmanager;

public final class AddTaskCommand implements Command {
    private final TaskRegistry registry;
    private final Task task;

    public AddTaskCommand(TaskRegistry registry, Task task) {
        if (task == null) {
            throw new IllegalArgumentException("Task cannot be null");
        }
        this.registry = registry;
        this.task = task;
    }

    @Override
    public void execute() {
        // Check if a task with the same name already exists
        registry.get(task.getName())
                .ifPresent(existing -> {
                    // Throw TaskAlreadyExistsException if task exists
                    throw new TaskAlreadyExistsException("Task '" + task.getName() + "' already exists.");
                });

        // Add the new task to the registry
        registry.add(task);
    }
}