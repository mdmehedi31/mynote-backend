package com.mynoteservice.controller;


import com.mynotelibrary.dto.UserPostRequestDTO;
import com.mynotelibrary.dto.UserPostResponse;
import com.mynotelibrary.exceptionmessage.ApiResponse;
import com.mynoteservice.service.UserPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user-post")
public class UserPostController {

    @Autowired
    private UserPostService userPostService;

    @PostMapping("/create")
    public ResponseEntity<UserPostResponse> createUserPost(@RequestBody UserPostRequestDTO userPostRequestDTO) {

        UserPostResponse userPostResponse = userPostService.createUserPost(userPostRequestDTO);
        return new ResponseEntity<>(userPostResponse, HttpStatus.CREATED);
    }

    @PutMapping("/update")
    public ResponseEntity<UserPostResponse> updateUserPost(@RequestBody UserPostRequestDTO userPostRequestDTO) {
        UserPostResponse userPostResponse = userPostService.updateUserPost(userPostRequestDTO);
        return new ResponseEntity<>(userPostResponse, HttpStatus.OK);
    }

    @GetMapping("/get/{postId}")
    public ResponseEntity<UserPostResponse> getUserPostById(@PathVariable("postId") String postId) {
        UserPostResponse userResponseDTO= userPostService.getUserPostById(postId);
        return new ResponseEntity<>(userResponseDTO, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{postId}")
    public ResponseEntity<ApiResponse> deleteUserPost(@PathVariable("postId") String postId) {
        ApiResponse apiResponse = userPostService.deleteUserPost(postId);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }
}
