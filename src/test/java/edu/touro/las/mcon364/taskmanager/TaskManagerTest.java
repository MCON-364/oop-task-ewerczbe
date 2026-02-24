package edu.touro.las.mcon364.taskmanager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for TaskManager.
 * After refactoring to use pattern-matching switch, these tests should still pass.
 */
class TaskManagerTest {
    private TaskRegistry registry;
    private TaskManager manager;

    @BeforeEach
    void setUp() {
        registry = new TaskRegistry();
        manager = new TaskManager(registry);
    }

    @Test
    @DisplayName("TaskManager.run() should execute AddTaskCommand")
    void testRunAddTaskCommand() {
        Task task = new Task("Test task", Priority.MEDIUM);
        Command command = new AddTaskCommand(registry, task);

        manager.run(command);

        // Handle Optional return from get() method
        assertTrue(registry.get("Test task").isPresent(), "Task should be added");
        assertEquals(task, registry.get("Test task").orElseThrow(), "The task should match the added task");
    }

    @Test
    @DisplayName("TaskManager.run() should execute RemoveTaskCommand")
    void testRunRemoveTaskCommand() {
        registry.add(new Task("Remove me", Priority.HIGH));
        Command command = new RemoveTaskCommand(registry, "Remove me");

        manager.run(command);

        // Handle Optional return from get() method
        assertTrue(registry.get("Remove me").isEmpty(), "Task should be removed");
    }

    @Test
    @DisplayName("TaskManager.run() should execute UpdateTaskCommand")
    void testRunUpdateTaskCommand() {
        registry.add(new Task("Update me", Priority.LOW));
        Command command = new UpdateTaskCommand(registry, "Update me", Priority.HIGH);

        manager.run(command);

        // Handle Optional return from get() method
        Task updatedTask = registry.get("Update me").orElseThrow();
        assertEquals(Priority.HIGH, updatedTask.getPriority(), "Task priority should be updated");
    }

    @Test
    @DisplayName("TaskManager.run() should handle multiple commands in sequence")
    void testRunMultipleCommands() {
        manager.run(new AddTaskCommand(registry, new Task("Task 1", Priority.HIGH)));
        manager.run(new AddTaskCommand(registry, new Task("Task 2", Priority.LOW)));
        manager.run(new UpdateTaskCommand(registry, "Task 2", Priority.MEDIUM));
        manager.run(new RemoveTaskCommand(registry, "Task 1"));

        // Handle Optional return from get() method
        assertTrue(registry.get("Task 1").isEmpty(), "Task 1 should be removed");
        assertTrue(registry.get("Task 2").isPresent(), "Task 2 should still exist");
        assertEquals(Priority.MEDIUM, registry.get("Task 2").orElseThrow().getPriority(),
                "Task 2 priority should be updated");
    }

    @Test
    @DisplayName("TaskManager should work with same registry instance")
    void testSharedRegistry() {
        // Verify that manager uses the same registry instance
        Task task = new Task("Shared task", Priority.HIGH);
        manager.run(new AddTaskCommand(registry, task));

        // Should be retrievable from the registry we passed to manager
        assertTrue(registry.get("Shared task").isPresent(),
                "Task should be in the shared registry instance");
    }
}