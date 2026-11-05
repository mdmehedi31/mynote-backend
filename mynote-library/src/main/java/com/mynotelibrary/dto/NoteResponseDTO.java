package com.mynotelibrary.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.mynotelibrary.exceptionmessage.ApiResponse;

import java.time.LocalDateTime;

public class NoteResponseDTO extends ApiResponse {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String id;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String userId;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String title;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String content;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private LocalDateTime createdAt;

    public NoteResponseDTO() {
    }

    public NoteResponseDTO(String message) {
        super(message);
    }



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
