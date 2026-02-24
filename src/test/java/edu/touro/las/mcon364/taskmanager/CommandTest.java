package edu.touro.las.mcon364.taskmanager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

class CommandTest {
    private TaskRegistry registry;

    @BeforeEach
    void setUp() {
        registry = new TaskRegistry();
    }

    @Test
    @DisplayName("AddTaskCommand should add task to registry")
    void testAddTaskCommand() {
        Task task = new Task("New task", Priority.MEDIUM);
        Command command = new AddTaskCommand(registry, task);

        command.execute();

        assertNotNull(registry.get("New task").orElse(null), "Task should be in registry after AddTaskCommand");
        assertEquals(task, registry.get("New task").orElse(null), "Added task should match");
    }


    @Test
    @DisplayName("AddTaskCommand should throw TaskAlreadyExistsException when task with same name exists")
    void testAddTaskCommandTaskAlreadyExists() {
        Task task = new Task("Existing task", Priority.MEDIUM);
        new AddTaskCommand(registry, task).execute();

        Task duplicateTask = new Task("Existing task", Priority.HIGH);
        Command addCommand = new AddTaskCommand(registry, duplicateTask);

        // Expect TaskAlreadyExistsException to be thrown when trying to add a duplicate task
        assertThrows(TaskAlreadyExistsException.class, addCommand::execute,
                "Adding a task with the same name should throw TaskAlreadyExistsException");
    }

    @Test
    @DisplayName("RemoveTaskCommand should remove task from registry")
    void testRemoveTaskCommand() {
        registry.add(new Task("To be removed", Priority.HIGH));

        Command command = new RemoveTaskCommand(registry, "To be removed");
        command.execute();

        assertNull(registry.get("To be removed").orElse(null), "Task should be removed from registry");
    }

    @Test
    @DisplayName("RemoveTaskCommand on non-existent task should throw TaskNotFoundException")
    void testRemoveTaskCommandNonExistent() {
        Command command = new RemoveTaskCommand(registry, "Non-existent");

        assertThrows(TaskNotFoundException.class, command::execute,
                "Removing non-existent task should throw TaskNotFoundException");
    }

    @Test
    @DisplayName("UpdateTaskCommand should update existing task priority")
    void testUpdateTaskCommand() {
        registry.add(new Task("Update me", Priority.LOW));

        Command command = new UpdateTaskCommand(registry, "Update me", Priority.HIGH);
        command.execute();

        Task updated = registry.get("Update me").orElseThrow();
        assertEquals(Priority.HIGH, updated.getPriority(), "Priority should be updated to HIGH");
    }

    @Test
    @DisplayName("UpdateTaskCommand should preserve task name")
    void testUpdateTaskCommandPreservesName() {
        registry.add(new Task("Important task", Priority.MEDIUM));

        Command command = new UpdateTaskCommand(registry, "Important task", Priority.LOW);
        command.execute();

        Task updated = registry.get("Important task").orElseThrow();
        assertEquals("Important task", updated.name(), "Task name should be preserved");
    }

    @Test
    @DisplayName("UpdateTaskCommand on non-existent task should throw TaskNotFoundException")
    void testUpdateTaskCommandNonExistent() {
        Command command = new UpdateTaskCommand(registry, "Non-existent", Priority.HIGH);

        assertThrows(TaskNotFoundException.class, command::execute,
                "Updating non-existent task should throw TaskNotFoundException");
    }

    @Test
    @DisplayName("UpdateTaskCommand should allow changing priority from HIGH to LOW")
    void testUpdateTaskCommandPriorityDecrease() {
        registry.add(new Task("Flexible", Priority.HIGH));

        new UpdateTaskCommand(registry, "Flexible", Priority.LOW).execute();

        assertEquals(Priority.LOW, registry.get("Flexible").orElseThrow().getPriority(),
                "Should allow decreasing priority");
    }

    @Test
    @DisplayName("UpdateTaskCommand should allow changing priority from LOW to HIGH")
    void testUpdateTaskCommandPriorityIncrease() {
        registry.add(new Task("Urgent", Priority.LOW));

        new UpdateTaskCommand(registry, "Urgent", Priority.HIGH).execute();

        assertEquals(Priority.HIGH, registry.get("Urgent").orElseThrow().getPriority(),
                "Should allow increasing priority");
    }
}