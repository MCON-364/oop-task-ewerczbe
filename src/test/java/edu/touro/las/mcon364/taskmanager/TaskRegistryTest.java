package edu.touro.las.mcon364.taskmanager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for TaskRegistry to verify behavior before and after refactoring.
 */
class TaskRegistryTest {
    private TaskRegistry registry;

    @BeforeEach
    void setUp() {
        registry = new TaskRegistry();
    }

    @Test
    @DisplayName("Adding a task should make it retrievable")
    void testAdd() {
        Task task = new Task("Test task", Priority.HIGH);
        registry.add(task);

        // Updated for Optional return type
        Task retrieved = registry.get("Test task").orElseThrow(() -> new IllegalStateException("Task should be retrievable"));
        assertEquals(task, retrieved, "Retrieved task should equal added task");
    }

    @Test
    @DisplayName("Adding multiple tasks with the same name should replace the old one")
    void testAddReplacement() {
        Task task1 = new Task("Test task", Priority.LOW);
        Task task2 = new Task("Test task", Priority.HIGH);

        registry.add(task1);
        registry.add(task2);

        // Updated for Optional return type
        Task retrieved = registry.get("Test task").orElseThrow(() -> new IllegalStateException("Task should be retrievable after replacement"));
        assertEquals(Priority.HIGH, retrieved.getPriority(), "Second task should replace the first one");
    }

    @Test
    @DisplayName("Getting a non-existent task should return an empty Optional")
    void testGetNonExistent() {
        // Check that get() returns an empty Optional for non-existent tasks
        assertTrue(registry.get("Non-existent").isEmpty(), "Non-existent task should return empty Optional");
    }

    @Test
    @DisplayName("Removing a task should make it no longer retrievable")
    void testRemove() {
        Task task = new Task("Test task", Priority.MEDIUM);
        registry.add(task);

        registry.remove("Test task");

        // Updated for Optional return type
        assertTrue(registry.get("Test task").isEmpty(), "Removed task should not be retrievable");
    }


    @Test
    @DisplayName("getAll should return all added tasks")
    void testGetAll() {
        Task task1 = new Task("Task 1", Priority.HIGH);
        Task task2 = new Task("Task 2", Priority.LOW);
        Task task3 = new Task("Task 3", Priority.MEDIUM);

        registry.add(task1);
        registry.add(task2);
        registry.add(task3);

        // Ensure all tasks are in the registry
        assertEquals(3, registry.getAll().size(), "getAll should return all 3 tasks");
        assertTrue(registry.getAll().containsKey("Task 1"), "Should contain Task 1");
        assertTrue(registry.getAll().containsKey("Task 2"), "Should contain Task 2");
        assertTrue(registry.getAll().containsKey("Task 3"), "Should contain Task 3");
    }

    @Test
    @DisplayName("getAll on an empty registry should return an empty map")
    void testGetAllEmpty() {
        assertTrue(registry.getAll().isEmpty(), "Empty registry should return empty map");
    }
}