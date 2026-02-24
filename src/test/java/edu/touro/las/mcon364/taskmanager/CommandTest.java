package edu.touro.las.mcon364.taskmanager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.List;
import java.util.Map;

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
    @Test
    @DisplayName("ChangeTaskPriorityCommand should change the task priority")
    void testChangeTaskPriorityCommand() {
        Task task = new Task("Task to Change", Priority.LOW);
        registry.add(task);

        Command command = new ChangeTaskPriorityCommand(registry, "Task to Change", Priority.HIGH);
        command.execute();

        Task updatedTask = registry.get("Task to Change").orElseThrow();
        assertEquals(Priority.HIGH, updatedTask.getPriority(), "Priority should be updated to HIGH");
    }
    @Test
    @DisplayName("AddTaskCommand should throw InvalidTaskPriorityException for null priority")
    void testAddTaskCommandInvalidPriority() {
        // Expect InvalidTaskPriorityException to be thrown during Task creation (not AddTaskCommand)
        InvalidTaskPriorityException exception = assertThrows(InvalidTaskPriorityException.class, () -> {
            Task task = new Task("Invalid Task", null);  // Task with null priority
            new AddTaskCommand(registry, task);  // Even though command creation comes after, the exception happens earlier
        });

        // Assert the exception message
        assertEquals("Priority cannot be null for task: Invalid Task", exception.getMessage());
    }
    @Test
    @DisplayName("getTasksByPriority should group tasks by their priority")
    void testGetTasksByPriority() {
        Task task1 = new Task("Task 1", Priority.LOW);
        Task task2 = new Task("Task 2", Priority.HIGH);
        Task task3 = new Task("Task 3", Priority.LOW);

        registry.add(task1);
        registry.add(task2);
        registry.add(task3);

        Map<Priority, List<Task>> groupedTasks = registry.getTasksByPriority();

        assertTrue(groupedTasks.containsKey(Priority.LOW));
        assertTrue(groupedTasks.containsKey(Priority.HIGH));
        assertEquals(2, groupedTasks.get(Priority.LOW).size(), "There should be 2 tasks with LOW priority");
        assertEquals(1, groupedTasks.get(Priority.HIGH).size(), "There should be 1 task with HIGH priority");
    }
}