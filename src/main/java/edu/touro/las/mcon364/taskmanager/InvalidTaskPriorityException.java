package edu.touro.las.mcon364.taskmanager;

public class InvalidTaskPriorityException extends RuntimeException {
    public InvalidTaskPriorityException(String message) {
        super(message);
    }

    public InvalidTaskPriorityException(String message, Throwable cause) {
        super(message, cause);
    }
}