package edu.touro.las.mcon364.taskmanager;

public final class RemoveTaskCommand implements Command {
    private final TaskRegistry registry;
    private final String name;

    public RemoveTaskCommand(TaskRegistry registry, String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Task name must not be null or empty");
        }
        this.registry = registry;
        this.name = name;
    }

    @Override
    public void execute() {
        // Check if the task exists before removing it
        Task existing = registry.get(name)
                .orElseThrow(() -> new TaskNotFoundException("Task '" + name + "' not found."));

        // Proceed with removing the task from the registry
        registry.remove(name);
    }
}