package com.mynotelibrary.exceptionmessage;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ExceptionResponseDTO {
    private String error;
    private int code;
    private String details;
    private LocalDateTime timestamp;
    private String path;

    @Builder
    public ExceptionResponseDTO(String error, int code, String details, LocalDateTime timestamp, String path) {
        this.error = error;
        this.code = code;
        this.details = details;
        this.timestamp = timestamp;
        this.path = path;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
