package edu.touro.las.mcon364.taskmanager;

import java.util.Objects;

public record Task(String name, Priority priority) {

    // Custom constructor for validation
    public Task {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Task name cannot be null or empty");
        }
        if (priority == null) {
            throw new InvalidTaskPriorityException("Priority cannot be null for task: " + name);
        }
    }

    // Getters are automatically provided by the record, but can be manually added if needed
    public String getName() {
        return name;
    }

    public Priority getPriority() {
        return priority;
    }

    // Override equals and hashCode for comparison in tests and collections
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Objects.equals(name, task.name) && priority == task.priority;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, priority);
    }
}