package com.mynoteservice.repository;

import com.mynoteservice.entity.UserPostEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserPostRepository extends MongoRepository<UserPostEntity, String> {
}
