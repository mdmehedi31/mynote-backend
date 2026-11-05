package com.mynotelibrary.dto;


import com.mynotelibrary.exceptionmessage.ApiResponse;

import java.time.LocalDateTime;
import java.util.List;

public class UserResponseDTO extends ApiResponse {

    private String id;
    private String name;
    private String email;
    private List<String> interests;
    private String role;
    private LocalDateTime createdAt;

    public UserResponseDTO(String message) {
        super(message);
    }

    public UserResponseDTO() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<String> getInterests() {
        return interests;
    }

    public void setInterests(List<String> interests) {
        this.interests = interests;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
