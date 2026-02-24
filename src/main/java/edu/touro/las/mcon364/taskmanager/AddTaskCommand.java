package edu.touro.las.mcon364.taskmanager;

public final class AddTaskCommand implements Command {
    private final TaskRegistry registry;
    private final Task task;

    public AddTaskCommand(TaskRegistry registry, Task task) {
        if (task == null || task.name().isEmpty()) {
            throw new IllegalArgumentException("Task or task name cannot be null or empty");
        }

        // Ensure the priority is not null at the time of command creation
        if (task.priority() == null) {
            throw new InvalidTaskPriorityException("Priority cannot be null for task: " + task.name());
        }

        this.registry = registry;
        this.task = task;
    }

    @Override
    public void execute() {
        registry.get(task.name()).ifPresent(existing -> {
            throw new TaskAlreadyExistsException("Task '" + task.name() + "' already exists.");
        });
        registry.add(task);
    }
}