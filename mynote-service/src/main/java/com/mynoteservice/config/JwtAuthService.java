package com.mynoteservice.config;

import com.mynotelibrary.dto.UserRequestDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
public class JwtAuthService {

    @Autowired
    private JWTUtils jwtUtils;

    @Autowired
    private AuthenticationManager authenticationManager;

    public Map<String, String> getJwtToken(UserRequestDTO userLoginDTO) {
        try{
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userLoginDTO.getEmail(), userLoginDTO.getPassword())
            );

         return  jwtUtils.generateJwtToken(userLoginDTO);
        }catch (Exception e) {
            throw e;
        }

    }
}
