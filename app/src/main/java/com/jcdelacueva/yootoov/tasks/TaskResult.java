package com.jcdelacueva.yootoov.tasks;

/**
 * Created by robin on 17/07/2017.
 */

public class TaskResult {
    private String response;
    private Exception exception;

    public TaskResult(String response, Exception exception) {
        this.response = response;
        this.exception = exception;
    }

    public String getResponse() {
        return response;
    }

    public Exception getException() {
        return exception;
    }
}
