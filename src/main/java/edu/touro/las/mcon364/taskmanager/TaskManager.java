package edu.touro.las.mcon364.taskmanager;

public class TaskManager {
    private final TaskRegistry registry;

    public TaskManager(TaskRegistry registry) {
        this.registry = registry;
    }

    public void run(Command command) {
        try {
            command.execute();
        } catch (TaskNotFoundException e) {
            // Gracefully handle the exception
            System.err.println("Error: " + e.getMessage());
        }
    }
}