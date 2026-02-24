package edu.touro.las.mcon364.taskmanager;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * The TaskRegistry class maintains a collection of tasks, allowing tasks to be added,
 * retrieved, and removed. It uses a HashMap to store tasks by their name.
 */
public class TaskRegistry {
    private final Map<String, Task> tasks = new HashMap<>();

    /**
     * Adds a task to the registry. If a task with the same name already exists, it will
     * be replaced by the new one.
     *
     * @param task The task to be added.
     */
    public void add(Task task) {
        if (task == null || task.getName() == null || task.getName().isEmpty()) {
            throw new IllegalArgumentException("Task and task name must not be null or empty");
        }
        tasks.put(task.getName(), task);
    }

    /**
     * Retrieves a task by its name.
     *
     * @param name The name of the task to retrieve.
     * @return An Optional containing the task if it exists, otherwise an empty Optional.
     */
    public Optional<Task> get(String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Task name must not be null or empty");
        }
        return Optional.ofNullable(tasks.get(name));
    }

    /**
     * Removes a task from the registry by its name.
     *
     * @param name The name of the task to remove.
     * @throws TaskNotFoundException if no task with the specified name is found.
     */
    public void remove(String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Task name must not be null or empty");
        }

        if (!tasks.containsKey(name)) {
            throw new TaskNotFoundException("Task '" + name + "' not found.");
        }

        tasks.remove(name);
    }

    /**
     * Returns all tasks in the registry.
     *
     * @return A map containing all tasks, where the key is the task name and the value is the task.
     */
    public Map<String, Task> getAll() {
        return new HashMap<>(tasks);  // Return a copy to avoid external modifications
    }
}