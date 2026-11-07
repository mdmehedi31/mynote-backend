package com.mynoteservice.repository;

import com.mynoteservice.entity.NoteEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NoteRepository extends MongoRepository<NoteEntity, String> {

    Page<NoteEntity> findByCreatedById(String createdById, Pageable pageable);
    Page<NoteEntity> findAllByCreatedByIdAndTitleContainingIgnoreCaseOrCreatedByIdAndContentContaining(String createdById, String search, String createdById1, String search1, Pageable pageable);
}
