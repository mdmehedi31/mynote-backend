package com.mynoteservice.controller;


import com.mynotelibrary.dto.UserAuthInfo;
import com.mynotelibrary.dto.UserRequestDTO;
import com.mynoteservice.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {


    @Autowired
    private IUserService userService;

    @PostMapping("/create")
    public ResponseEntity<UserAuthInfo> createNewUser(@RequestBody UserRequestDTO userRegistrationDTO) {
        UserAuthInfo response = userService.createUser(userRegistrationDTO);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<UserAuthInfo> login(@RequestBody UserRequestDTO userLoginDTO) {
        UserAuthInfo userAuthInfo = userService.loginUser(userLoginDTO);
        return new ResponseEntity<>(userAuthInfo, HttpStatus.OK);
    }
}
