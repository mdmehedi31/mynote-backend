package com.mynoteservice.controller;


import com.mynotelibrary.dto.UserRequestDTO;
import com.mynotelibrary.dto.UserResponseDTO;
import com.mynotelibrary.exceptionmessage.ApiResponse;
import com.mynoteservice.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private IUserService userService;

    @PostMapping("/create")
    public ResponseEntity<UserResponseDTO> createUser(@RequestBody UserRequestDTO userRequestDTO) {
        UserResponseDTO createdUser = userService.createUser(userRequestDTO);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    @PutMapping("/update")
    public ResponseEntity<UserResponseDTO> updateUser(@RequestBody UserRequestDTO userRequestDTO) {
        UserResponseDTO updatedUser = userService.updateUser(userRequestDTO);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

    @GetMapping("get/{userId}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable String userId) {
        UserResponseDTO userResponseDTO = userService.getUserByIdOrEmail(userId, null);
        return new ResponseEntity<>(userResponseDTO, HttpStatus.OK);
    }

    @GetMapping("{userEmail}/get")
    public ResponseEntity<UserResponseDTO> getUserByEmail(@PathVariable String userEmail) {
        UserResponseDTO userResponseDTO = userService.getUserByIdOrEmail(null, userEmail);
        return new ResponseEntity<>(userResponseDTO, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<ApiResponse> deleteUser(@PathVariable String userId) {
        ApiResponse apiResponse = userService.deleteUser(userId);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @GetMapping("/by-interests")
    public ResponseEntity<Page<UserResponseDTO>> getUsersByUserInterests( @RequestParam(required = false) List<String> interests,
                                                                        @RequestParam(defaultValue = "0") int page,
                                                                        @RequestParam(defaultValue = "10") int size) {
        Page<UserResponseDTO> users = userService.getUsersByUserInterests(interests, page, size);
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/gets")
    public ResponseEntity<Page<UserResponseDTO>> getAllUsers(@RequestParam(defaultValue = "0") int page,
                                                             @RequestParam(defaultValue = "10") int size,
                                                             @RequestParam(defaultValue = "createdAt") String sort,
                                                             @RequestParam(defaultValue = "DESC") String sortDir) {
        Page<UserResponseDTO> users = userService.getAllUsers(page, size, sort, sortDir);
        return new ResponseEntity<>(users, HttpStatus.OK);
    }


}
