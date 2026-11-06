package com.mynoteservice.service;

import com.mynotelibrary.dto.UserPostRequestDTO;
import com.mynotelibrary.dto.UserPostResponse;
import com.mynotelibrary.dto.UserWithPostDTO;
import com.mynotelibrary.exceptionmessage.ApiResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IUserPostService {


    public UserPostResponse createUserPost(UserPostRequestDTO userPostRequestDTO);
    public UserPostResponse updateUserPost(UserPostRequestDTO userPostRequestDTO);
    public UserPostResponse getUserPostById(String postId);
    public ApiResponse deleteUserPost(String postId);
    public Page<UserWithPostDTO> getAllUserPostsByUserId(String userId, int page, int pageSize);

    public Page<UserPostResponse> getAllUsersPostsBy(int page, int pageSize);

}
