package edu.touro.las.mcon364.taskmanager;

public sealed interface Command
        permits UpdateTaskCommand, AddTaskCommand, RemoveTaskCommand, ChangeTaskPriorityCommand {
    void execute();
}