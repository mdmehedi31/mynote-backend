package com.mynoteservice.service;

import com.mynotelibrary.dto.UserRequestDTO;
import com.mynotelibrary.dto.UserResponseDTO;
import com.mynotelibrary.exceptionmessage.ApiResponse;

public interface IUserService {

    public UserResponseDTO createUser(UserRequestDTO userRequestDTO);
    public UserResponseDTO updateUser(UserRequestDTO userRequestDTO);
    public UserResponseDTO getUserByIdOrEmail(String userId, String email);
    public ApiResponse deleteUser(String userId);

}
