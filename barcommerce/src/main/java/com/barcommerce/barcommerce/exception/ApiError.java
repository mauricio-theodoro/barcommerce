package com.barcommerce.barcommerce.exception;

import java.time.Instant;
import java.util.List;

public class ApiError {
    private Instant timestamp;
    private int status;
    private List<String> errors;

    public ApiError(int status, List<String> errors) {
        this.timestamp = Instant.now();
        this.status = status;
        this.errors = errors;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }
// getters e setters
}
