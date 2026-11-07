package com.mynoteservice.service;

import com.mynotelibrary.dto.UserAuthInfo;
import com.mynotelibrary.dto.UserRequestDTO;
import com.mynotelibrary.dto.UserResponseDTO;
import com.mynotelibrary.exceptionmessage.ApiResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IUserService {

    public UserAuthInfo createUser(UserRequestDTO userRequestDTO);
    public UserResponseDTO updateUser(UserRequestDTO userRequestDTO);
    public UserResponseDTO getUserByIdOrEmail(String userId, String email);
    public ApiResponse deleteUser(String userId);
    public Page<UserResponseDTO> getUsersByUserInterests(List<String> selectedInterests, int page, int size);
    public Page<UserResponseDTO> getAllUsers(int page, int size, String sortBy, String sortDir);
    public UserAuthInfo loginUser(UserRequestDTO userLoginDto);
}
