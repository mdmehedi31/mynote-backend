package com.mynoteservice.service;


import com.mynotelibrary.dto.UserAuthInfo;
import com.mynotelibrary.enums.SUCCESS;
import com.mynotelibrary.enums.UserRole;
import com.mynotelibrary.dto.UserRequestDTO;
import com.mynotelibrary.dto.UserResponseDTO;
import com.mynotelibrary.exceptionmessage.ApiResponse;
import com.mynoteservice.config.JwtAuthService;
import com.mynoteservice.entity.UserEntity;
import com.mynoteservice.exceptionhandler.InvalidInputException;
import com.mynoteservice.exceptionhandler.NotFoundException;
import com.mynoteservice.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class UserService implements IUserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private JwtAuthService jwtAuthService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserAuthInfo createUser(UserRequestDTO userRequestDTO) {
        try{

            if (userRequestDTO.getEmail()==null || userRequestDTO.getEmail().isBlank()){
                throw new InvalidInputException("Email can be empty");
            }

            if (userRequestDTO.getPassword()==null || userRequestDTO.getPassword().isBlank()){
                throw new InvalidInputException("Password can be empty");
            }

            Optional<UserEntity> userIsExist = userRepository.findByEmail(userRequestDTO.getEmail());
            if (userIsExist.isPresent()){
                return new UserAuthInfo("User already exist with this email");
            }
            UserEntity user = new UserEntity();
            user.setCreatedAt(LocalDateTime.now());
            user.setEmail(userRequestDTO.getEmail());

            userEntityFromDTO(userRequestDTO, user);
            user=userRepository.save(user);

            UserAuthInfo userAuthInfo = new UserAuthInfo();
            if (user!=null){
                Map<String, String> jwt =jwtAuthService.getJwtToken(userRequestDTO);
                userAuthInfo.setJwtToken(jwt.get("token"));
                UserResponseDTO userResponseDTO = userResponseDTOFromEntity(user);
                userAuthInfo.setUser(userResponseDTO);
                userAuthInfo.setMessage(SUCCESS.SUCCESS.name());

            }else {
                userAuthInfo.setMessage(SUCCESS.FAILED.name());
            }

            return userAuthInfo;

        } catch (InvalidInputException e){
            log.error("An Exception thrown", e);
            return new UserAuthInfo(e.getMessage());
        }
        catch (Exception e){
            log.error("An Exception Thrown : ", e);
            return new UserAuthInfo("Can't create user, Please try again later");
        }
    }

    private UserResponseDTO userResponseDTOFromEntity(UserEntity userEntity){
        UserResponseDTO userResponseDTO = new UserResponseDTO();
        userResponseDTO.setId(userEntity.getId());
        userResponseDTO.setEmail(userEntity.getEmail());
        userResponseDTO.setName(userEntity.getName() != null ? userEntity.getName() : null);
        userResponseDTO.setCreatedAt(userEntity.getCreatedAt());
        userResponseDTO.setRole(userEntity.getRole() != null ? UserRole.valueOf(userEntity.getRole()) : UserRole.USER);

        if (userEntity.getInterests()!=null && userEntity.getInterests().size()>0){
            userResponseDTO.setInterests(userEntity.getInterests());
        }
        return userResponseDTO;
    }

    private UserEntity userEntityFromDTO(UserRequestDTO userRequestDTO, UserEntity userEntity) {

        if (userRequestDTO.getPassword()!=null && !userRequestDTO.getPassword().isBlank()){
            userEntity.setPassword(passwordEncoder.encode(userRequestDTO.getPassword()));
        }

        if (userRequestDTO.getName()!=null && !userRequestDTO.getName().isBlank()){
            userEntity.setName(userRequestDTO.getName());
        }else if (userRequestDTO.getEmail()!=null && !userRequestDTO.getEmail().isBlank()){
            String userName = userRequestDTO.getEmail().split("@")[0];
            userEntity.setName(userName);
        }

        if(userRequestDTO.getRole() != null){
            userEntity.setRole(userRequestDTO.getRole().name());
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
    public UserResponseDTO getUserByIdOrEmail(String userId, String email) {
        try {
            Optional<UserEntity> userEntity =null;

            if(userId != null ){
                userEntity = userRepository.findById(userId);
            } else if (email != null && !email.isBlank()) {
                userEntity= userRepository.findByEmail(email);
            }

            if (userEntity==null || !userEntity.isPresent()){
                return new UserResponseDTO("User not found");
            }
            return userResponseDTOFromEntity(userEntity.get());
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

    @Override
    public Page<UserResponseDTO> getUsersByUserInterests(List<String> selectedInterests, int page, int size) {
        int skip = page * size;
        Pageable pageable = PageRequest.of(page, size);
        List<org.springframework.data.mongodb.core.aggregation.AggregationOperation> pipelines = new ArrayList<>();

        if (selectedInterests != null && !selectedInterests.isEmpty()) {
            List<Pattern> interestsPattern = selectedInterests.stream()
                    .filter(interest -> interest != null && !interest.trim().isEmpty())
                    .map(interest -> Pattern.compile("^" + Pattern.quote(interest.trim()) + "$", Pattern.CASE_INSENSITIVE))
                    .collect(Collectors.toList());

            interestsPattern.forEach(e->{
                log.info(e.toString()+",");
            });
            pipelines.add(Aggregation.match(Criteria.where("interests").in(interestsPattern)));
        }

        pipelines.add(Aggregation.project("name", "email", "interests", "role")
                .andExpression("_id").as("id"));

        pipelines.add(Aggregation.group("id")
                .first("name").as("name")
                .first("email").as("email")
                .first("interests").as("interests")
                .first("role").as("role"));

        pipelines.add(Aggregation.skip((long) skip));
        pipelines.add(Aggregation.limit(size));

        Aggregation aggregation = Aggregation.newAggregation(pipelines);

      /*  Aggregation aggregation = Aggregation.newAggregation(

                Aggregation.match(Criteria.where("interests").in(selectedInterests)),

                Aggregation.project("name", "email", "interests","role")
                        .andExpression("_id").as("id"),

                Aggregation.group("id")
                        .first("name").as("name")
                        .first("email").as("email")
                        .first("interests").as("interests")
                        .first("role").as("role"),

                Aggregation.skip((long) skip),
                Aggregation.limit(size)
        );*/
        AggregationResults<UserResponseDTO> results = mongoTemplate.aggregate(
                aggregation, "users", UserResponseDTO.class);
        List<UserResponseDTO> userList = results.getMappedResults();
        Query countQuery = new Query();
        if (selectedInterests != null && !selectedInterests.isEmpty()) {
            countQuery.addCriteria(Criteria.where("interests").in(selectedInterests));
        }
        long totalElements = mongoTemplate.count(countQuery, "users");
        //return results.getMappedResults();
        return new PageImpl<>(userList, pageable, totalElements);
    }

    @Override
    public Page<UserResponseDTO> getAllUsers(int page, int size,String sortBy, String sortDir) {
        Sort.Direction sortingOrder = sortDir.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortingOrder, sortBy));

        Page<UserEntity> userEntityPage = userRepository.findAll(pageable);

        return userEntityPage.map(this::userResponseDTOFromEntity);
    }

    @Override
    public UserAuthInfo loginUser(UserRequestDTO userLoginDto) {
        UserAuthInfo userAuthInfo= new UserAuthInfo();
        UserResponseDTO userDTO=new UserResponseDTO();

        try{
            if(userLoginDto==null || userLoginDto.getEmail()==null){
                userAuthInfo.setMessage("User registration info invalid");
                return userAuthInfo;
            }
            Optional<UserEntity> userEntityOpt=userRepository.findByEmail(userLoginDto.getEmail());

            if(!userEntityOpt.isPresent()){
                userAuthInfo.setMessage("User not found with this email");
                return userAuthInfo;
            }

            UserEntity userEntity=userEntityOpt.get();
            String endCodedPassword=passwordEncoder.encode(userLoginDto.getPassword());

            log.info("Endcoding password : " + endCodedPassword+", request pass : " +userEntity.getPassword());

            boolean isMatchPassword = passwordEncoder.matches(userLoginDto.getPassword(),userEntity.getPassword());
            if(!isMatchPassword){
                userAuthInfo.setMessage("Password does not match");
                return userAuthInfo;
            }

            userDTO=userResponseDTOFromEntity(userEntity);

            Map<String, String> jwt =jwtAuthService.getJwtToken(userLoginDto);
            userAuthInfo.setJwtToken(jwt.get("token"));
            userAuthInfo.setUser(userDTO);
            return userAuthInfo;
        }catch (Exception e){
            log.error("An exception thrown : ",e);
            throw e;
        }
    }

    private UserResponseDTO convertToUserResponseDTO(UserEntity userEntity){
        UserResponseDTO userResponseDTO = new UserResponseDTO();
        userResponseDTO.setId(userEntity.getId());
        userResponseDTO.setEmail(userEntity.getEmail());
        userResponseDTO.setName(userEntity.getName()!=null?userEntity.getName():null);
        return userResponseDTO;
    }
}
