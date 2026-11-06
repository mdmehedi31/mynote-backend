package com.mynoteservice.controller;


import com.mynotelibrary.dto.UserPostRequestDTO;
import com.mynotelibrary.dto.UserPostResponse;
import com.mynotelibrary.dto.UserWithPostDTO;
import com.mynotelibrary.exceptionmessage.ApiResponse;
import com.mynoteservice.service.IUserPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user-post")
public class UserPostController {

    @Autowired
    private IUserPostService userPostService;

    @PostMapping("/create")
    public ResponseEntity<UserPostResponse> createUserPost(@RequestBody UserPostRequestDTO userPostRequestDTO) {

        UserPostResponse userPostResponse = userPostService.createUserPost(userPostRequestDTO);
        return new ResponseEntity<>(userPostResponse, HttpStatus.CREATED);
    }

    @PatchMapping("/update")
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

    @GetMapping("/posts-by-creator")
    public ResponseEntity<Page<UserWithPostDTO>> getUserPosts(
            @RequestParam String userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<UserWithPostDTO> response = userPostService.getAllUserPostsByUserId(userId, page, size);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/get-posts")
    public ResponseEntity<Page<UserPostResponse>> getUserPosts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size){
        Page<UserPostResponse> userPosts = userPostService.getAllUsersPosts(page, size);
        return new ResponseEntity<>(userPosts, HttpStatus.OK);
    }
}
