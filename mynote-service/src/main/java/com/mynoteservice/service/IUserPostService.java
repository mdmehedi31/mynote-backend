package com.mynoteservice.service;

import com.mynotelibrary.dto.UserPostRequestDTO;
import com.mynotelibrary.dto.UserPostResponse;
import com.mynotelibrary.exceptionmessage.ApiResponse;

import java.util.List;

public interface IUserPostService {


    public UserPostResponse createUserPost(UserPostRequestDTO userPostRequestDTO);
    public UserPostResponse updateUserPost(UserPostRequestDTO userPostRequestDTO);
    public UserPostResponse getUserPostById(String postId);
    public ApiResponse deleteUserPost(String postId);
    public List<UserPostResponse> getAllUserPostsByUserId(String userId, int page, int pageSize);

}
