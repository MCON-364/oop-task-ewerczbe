package edu.touro.las.mcon364.taskmanager;

import java.util.Objects;

public record Task(String name, Priority priority) {

    public Task(String name, Priority priority) {
        this.name = name;
        this.priority = priority;
    }

    public String getName() {
        return name;
    }

    public Priority getPriority() {
        return priority;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Task)) return false;
        Task task = (Task) o;
        return Objects.equals(name, task.name) &&
                priority == task.priority;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, priority);
    }
}
