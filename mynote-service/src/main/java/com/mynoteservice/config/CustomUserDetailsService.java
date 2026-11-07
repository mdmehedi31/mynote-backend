package com.mynoteservice.config;


import com.mynoteservice.entity.UserEntity;
import com.mynoteservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserEntity loadUserByUsername(String username)throws UsernameNotFoundException {
        return userRepository.findUserEntityByEmail(username);
    }
}
