package com.mynotelibrary.dto;

import java.util.List;

public class UserWithPostDTO {

    private String id;
    private String name;
    private String email;
    private List<UserPostResponse> userPosts;


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

    public List<UserPostResponse> getUserPosts() {
        return userPosts;
    }

    public void setUserPosts(List<UserPostResponse> userPosts) {
        this.userPosts = userPosts;
    }
}
