package com.mynoteservice.repository;

import com.mynoteservice.entity.UserPostEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface UserPostRepository extends MongoRepository<UserPostEntity, String> {

    @Query("Select")
    Page<UserPostRepository> getUserPosts(Pageable pageable);
}
