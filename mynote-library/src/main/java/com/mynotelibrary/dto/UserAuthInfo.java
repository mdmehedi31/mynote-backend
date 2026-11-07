package com.mynotelibrary.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

public class UserAuthInfo  {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String jwtToken;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private UserResponseDTO user;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String message;

    public UserAuthInfo() {
    }

    public UserAuthInfo(String message) {
        this.message = message;
    }

    public UserAuthInfo(String jwtToken, UserResponseDTO user, String message) {
        this.jwtToken = jwtToken;
        this.user = user;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getJwtToken() {
        return jwtToken;
    }

    public void setJwtToken(String jwtToken) {
        this.jwtToken = jwtToken;
    }

    public UserResponseDTO getUser() {
        return user;
    }

    public void setUser(UserResponseDTO user) {
        this.user = user;
    }
}
