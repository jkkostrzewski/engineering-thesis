package com.example.thesis.backend;

import org.springframework.http.HttpStatus;

public class ServiceResponse<T> {
    private HttpStatus status;
    private final T content;

    public ServiceResponse(HttpStatus status, T content) {
        this.status = status;
        this.content = content;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public T getContent() {
        return content;
    }

    public String toString() {
        return "Response{" +
                "content=" + content +
                ", status=" + getStatus() +
                '}';
    }
}
