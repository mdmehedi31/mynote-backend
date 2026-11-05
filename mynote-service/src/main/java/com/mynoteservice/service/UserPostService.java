package com.mynoteservice.service;


import com.mynotelibrary.dto.UserPostRequestDTO;
import com.mynotelibrary.dto.UserPostResponse;
import com.mynotelibrary.exceptionmessage.ApiResponse;
import com.mynoteservice.entity.UserPostEntity;
import com.mynoteservice.exceptionhandler.InvalidInputException;
import com.mynoteservice.exceptionhandler.NotFoundException;
import com.mynoteservice.repository.UserPostRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class UserPostService implements IUserPostService {

    @Autowired
    private UserPostRepository userPostRepository;

    private static final Logger log = LoggerFactory.getLogger(UserPostService.class);


    @Override
    public UserPostResponse createUserPost(UserPostRequestDTO userPostRequestDTO) {
        try{

            if(userPostRequestDTO.getCreatedById() ==null || userPostRequestDTO.getCreatedById().isBlank()){
                throw new InvalidInputException("Created by can not be empty");
            }

            if (userPostRequestDTO.getContent() == null || userPostRequestDTO.getContent().isBlank()){
                throw new InvalidInputException("Content can not be empty");
            }
            UserPostEntity userPostEntity = new UserPostEntity();
            userPostEntity.setCreatedById(userPostRequestDTO.getCreatedById());
            userPostEntity.setContent(userPostRequestDTO.getContent());
            userPostEntity.setCreatedAt(LocalDateTime.now());

            UserPostEntity saveResponse= userPostRepository.save(userPostEntity);

            if(saveResponse != null){
                return buildUserPostResponse(saveResponse);
            }

            return new UserPostResponse("Post Not Saved");

        } catch (InvalidInputException e) {
            log.error(" :::> An exception thrown : ",e);
            return new UserPostResponse(e.getMessage());
        }
        catch (Exception e){
            log.error("::> An exception thrown :: ",e);
            return new UserPostResponse("Post not created, Please try again");
        }
    }

    private UserPostResponse buildUserPostResponse(UserPostEntity userPostEntity){

        UserPostResponse userPostResponse = new UserPostResponse();
        userPostResponse.setId(userPostEntity.getId());
        userPostResponse.setContent(userPostEntity.getContent());
        userPostResponse.setCreatedAt(userPostEntity.getCreatedAt());
        userPostResponse.setCreatedBy(userPostEntity.getCreatedById());

        return userPostResponse;
    }

    @Override
    public UserPostResponse updateUserPost(UserPostRequestDTO userPostRequestDTO) {
        try{
            if (userPostRequestDTO.getId() !=null && userPostRequestDTO.getId().isBlank()){
                throw new InvalidInputException("Post can not be empty");
            }

            if(userPostRequestDTO.getContent() == null || userPostRequestDTO.getContent().isBlank()){
                throw new InvalidInputException("Content can not be empty");
            }

            Optional<UserPostEntity> userPostEntity = userPostRepository.findById(userPostRequestDTO.getId());
            if(!userPostEntity.isPresent()){
                throw new NotFoundException("Post Not Found");
            }

            UserPostEntity userPost = userPostEntity.get();
            userPost.setContent(userPostRequestDTO.getContent());
            userPost =  userPostRepository.save(userPost);
            return buildUserPostResponse(userPost);

        }catch (InvalidInputException e){
            log.error(" :::> An exception thrown : ",e);
            return new UserPostResponse(e.getMessage());
        }
        catch (Exception e){
            log.error("::> An exception thrown :: ",e);
            return new UserPostResponse("Post create failed, please try again");
        }
    }

    @Override
    public UserPostResponse getUserPostById(String postId) {
        try{
            Optional<UserPostEntity> postEntity = userPostRepository.findById(postId);

            if(!postEntity.isPresent()){
                throw new NotFoundException("Post Not Found");
            }
            return buildUserPostResponse(postEntity.get());
        }
        catch (NotFoundException e){
            log.error(" :::> An exception thrown : ",e);
            return new UserPostResponse(e.getMessage());
        }catch (Exception e){
            log.error("::> An exception thrown :: ",e);
            return new UserPostResponse("Post fetching failed, please try again");
        }
    }

    @Override
    public ApiResponse deleteUserPost(String postId) {
        try{
            Optional<UserPostEntity> userPostEntity = userPostRepository.findById(postId);
            if(!userPostEntity.isPresent()){
                throw new NotFoundException("Post Not Found");
            }

            userPostRepository.delete(userPostEntity.get());
            return new ApiResponse("User Post Deleted Successfully");

        }catch (NotFoundException e){
            log.error(" :::> An exception thrown : ",e);
            return new ApiResponse(e.getMessage());
        }
    }

    @Override
    public List<UserPostResponse> getAllUserPostsByUserId(String userId, int page, int pageSize) {
        return List.of();
    }
}
