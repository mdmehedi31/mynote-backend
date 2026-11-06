package com.mynoteservice.service;


import com.mynotelibrary.enums.UserRole;
import com.mynotelibrary.dto.UserRequestDTO;
import com.mynotelibrary.dto.UserResponseDTO;
import com.mynotelibrary.exceptionmessage.ApiResponse;
import com.mynoteservice.entity.UserEntity;
import com.mynoteservice.exceptionhandler.InvalidInputException;
import com.mynoteservice.exceptionhandler.NotFoundException;
import com.mynoteservice.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class UserService implements IUserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public UserResponseDTO createUser(UserRequestDTO userRequestDTO) {
        try{

            if (userRequestDTO.getEmail()==null || userRequestDTO.getEmail().isBlank()){
                throw new InvalidInputException("Email can be empty");
            }

            UserEntity userIsExist = userRepository.findByEmail(userRequestDTO.getEmail());
            if (userIsExist!=null){
                return new UserResponseDTO("Email already exists");
            }
            UserEntity user = new UserEntity();
            user.setCreatedAt(LocalDateTime.now());
            user.setEmail(userRequestDTO.getEmail());

            userEntityFromDTO(userRequestDTO, user);
            user=userRepository.save(user);

            if (user!=null){
                return userResponseDTOFromEntity(user);
            }

            return new UserResponseDTO("User creation failed");

        } catch (InvalidInputException e){
            log.error("An Exception thrown", e);
            return new UserResponseDTO(e.getMessage());
        }
        catch (Exception e){
            log.error("An Exception Thrown : ", e);
            return new UserResponseDTO("Can't create user, Please try again later");
        }
    }

    private UserResponseDTO userResponseDTOFromEntity(UserEntity userEntity){
        UserResponseDTO userResponseDTO = new UserResponseDTO();
        userResponseDTO.setId(userEntity.getId());
        userResponseDTO.setEmail(userEntity.getEmail());
        userResponseDTO.setName(userEntity.getName() != null ? userEntity.getName() : null);
        userResponseDTO.setCreatedAt(userEntity.getCreatedAt());
        userResponseDTO.setRole(userEntity.getRole() != null ? userEntity.getRole() : "USER");
        userResponseDTO.setInterests(userEntity.getInterests().isEmpty() ? null : userEntity.getInterests());
        return userResponseDTO;
    }

    private UserEntity userEntityFromDTO(UserRequestDTO userRequestDTO, UserEntity userEntity) {

        if (userRequestDTO.getPassword()!=null && !userRequestDTO.getPassword().isBlank()){
            userEntity.setPassword(userEntity.getPassword());
        }

        if (userRequestDTO.getName()!=null && !userRequestDTO.getName().isBlank()){
            userEntity.setName(userRequestDTO.getName());
        }

        if(userRequestDTO.getRole() != null && !userRequestDTO.getRole().isBlank()){
            userEntity.setRole(userRequestDTO.getRole());
        }else{
            userEntity.setRole(UserRole.USER.name());
        }

        if (userRequestDTO.getInterests() != null && !userRequestDTO.getInterests().isEmpty()){
            userEntity.setInterests(userRequestDTO.getInterests());
        }

        return userEntity;
    }

    @Override
    public UserResponseDTO updateUser(UserRequestDTO userRequestDTO) {
        try{
            if (userRequestDTO.getId() ==null || userRequestDTO.getId().isBlank()){
                throw new InvalidInputException("User invalid");
            }

            Optional<UserEntity> userEntity = userRepository.findById(userRequestDTO.getId());

            if (!userEntity.isPresent()){
                return new UserResponseDTO("User not found");
            }
            UserEntity user = userEntity.get();
            userEntityFromDTO(userRequestDTO, user);
            user=userRepository.save(user);
            return userResponseDTOFromEntity(user);

        }
        catch (InvalidInputException e){
            log.error("An Exception thrown :: ", e);
            return new UserResponseDTO(e.getMessage());
        }
        catch (Exception e){
            log.error("An Exception thrown ::: ", e);
            return new UserResponseDTO("User update failed");
        }
    }

    @Override
    public UserResponseDTO getUser(String userId) {
        try {
            Optional<UserEntity> userEntity = userRepository.findById(userId);

            if (!userEntity.isPresent()){
                throw new NotFoundException("User not found");
            }
            return userResponseDTOFromEntity(userEntity.get());
        }catch (NotFoundException e){
            log.error("An Exception thrown : ", e);
            return new UserResponseDTO(e.getMessage());

        }
        catch (Exception e){
            log.error("An Exception thrown :: ", e);
            return new UserResponseDTO("User Fetching failed");
        }
    }

    @Override
    public ApiResponse deleteUser(String userId) {
        try {
            Optional<UserEntity> userEntity = userRepository.findById(userId);
            if (!userEntity.isPresent()){
                throw new NotFoundException("User not found");
            }
            userRepository.delete(userEntity.get());
            return new  ApiResponse("User deleted successfully");
        }catch (NotFoundException e){
            log.error("An Exception thrown : ", e);
            return new ApiResponse(e.getMessage());
        }
        catch (Exception e){
            log.error("An Exception thrown :: ", e);
            return new ApiResponse("User deletion failed");
        }
    }
}
