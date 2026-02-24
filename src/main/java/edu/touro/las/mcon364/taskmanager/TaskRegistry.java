package edu.touro.las.mcon364.taskmanager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class TaskRegistry {
    private final Map<String, Task> tasks = new HashMap<>();

    public void add(Task task) {
        tasks.put(task.name(), task);
    }

    public Optional<Task> get(String name) {
        return Optional.ofNullable(tasks.get(name));
    }

    public void remove(String name) {
        tasks.remove(name);
    }

    public Map<String, Task> getAll() {
        return tasks;
    }
    // New method to get tasks by priority
    public Map<Priority, List<Task>> getTasksByPriority() {
        return tasks.values().stream()
                .collect(Collectors.groupingBy(Task::getPriority));
    }
}