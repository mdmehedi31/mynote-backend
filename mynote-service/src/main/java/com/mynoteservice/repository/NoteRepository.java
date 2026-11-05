package com.mynoteservice.repository;

import com.mynotelibrary.dto.NoteResponseDTO;
import com.mynoteservice.entity.NoteEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface NoteRepository extends MongoRepository<NoteEntity, String> {

    Page<NoteEntity> findByUserId(String userId, Pageable pageable);

    Page<NoteEntity> findAllByUserIdAndTitleContainingIgnoreCaseOrUserIdAndContentContaining(String userId, String search, String userId1, String search1, Pageable pageable);
}
