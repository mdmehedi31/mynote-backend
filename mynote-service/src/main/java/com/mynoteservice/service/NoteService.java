package com.mynoteservice.service;


import com.mynotelibrary.dto.NoteRequestDTO;
import com.mynotelibrary.dto.NoteResponseDTO;
import com.mynoteservice.entity.NoteEntity;
import com.mynoteservice.exceptionhandler.InvalidInputException;
import com.mynotelibrary.exceptionmessage.ApiResponse;
import com.mynoteservice.repository.NoteRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class NoteService implements INoteService {

    @Autowired
    private NoteRepository noteRepository;
    private static final Logger log = LoggerFactory.getLogger(NoteService.class);

    @Override
    public NoteResponseDTO createNote(NoteRequestDTO note) {
        try {

            if (note.getContent() == null || note.getContent().isBlank()) {
                throw new InvalidInputException("Note content is empty");
            }

            if (note.getUserId() == null || note.getUserId().isBlank()) {
                throw new InvalidInputException("User id is empty");
            }

            if (note.getTitle() == null || note.getTitle().isBlank()) {
                createNoteTitle(note);
            }

            NoteEntity noteEntity = new NoteEntity();
            noteEntity.setCreatedAt(LocalDateTime.now());
            noteEntity.setUserId(note.getUserId());
            createNoteEntity(note, noteEntity);

            noteEntity = noteRepository.save(noteEntity);

            if (noteEntity != null) {
                return createNoteResponse(noteEntity);
            }
            return new NoteResponseDTO("Note creation failed, Please try again");
        } catch (InvalidInputException e) {
            log.error("Note Create :: An Exception thrown : ",e);
            return new NoteResponseDTO(e.getMessage());
        }
        catch (Exception e) {
            log.error("Note Create :: An Exception thrown : ",e);
            return new NoteResponseDTO("Not create failed, Please try again");
        }
    }

    private void createNoteTitle(NoteRequestDTO note) {
        String content = note.getContent();
        String title = "";
        int maxLength = Math.min(content.length(), 30);
        title = content.substring(0, maxLength).trim() + "...";

        if(title.isBlank()){
            title="Untitled Note";
        }
        note.setTitle(title);
    }

    private NoteResponseDTO createNoteResponse(NoteEntity noteEntity) {
        NoteResponseDTO noteResponseDTO = new NoteResponseDTO();
        noteResponseDTO.setId(noteEntity.getId());
        noteResponseDTO.setContent(noteEntity.getContent());
        noteResponseDTO.setUserId(noteEntity.getUserId());

        if (noteEntity.getTitle() != null) {
            noteResponseDTO.setTitle(noteEntity.getTitle());
        }

        if (noteEntity.getCreatedAt() != null) {
            noteResponseDTO.setCreatedAt(noteEntity.getCreatedAt());
        }

        return noteResponseDTO;
    }

    private NoteEntity createNoteEntity(NoteRequestDTO note, NoteEntity noteEntity) {
        noteEntity.setTitle(note.getTitle());
        noteEntity.setContent(note.getContent());

        return noteEntity;
    }

    @Override
    public NoteResponseDTO updateNote(NoteRequestDTO note) {
        try{
            if (note.getId() == null || note.getId().isBlank()) {
                throw new InvalidInputException("Note can't be empty");
            }

            if (note.getContent() == null || note.getContent().isBlank()) {
                throw new InvalidInputException("Note content is empty");
            }

            Optional<NoteEntity> noteEntity = noteRepository.findById(note.getId());

            if (!noteEntity.isPresent()) {
                return new NoteResponseDTO("Note not found");
            }
            NoteEntity noteEntityToUpdate = noteEntity.get();
            createNoteEntity(note, noteEntityToUpdate);
            noteRepository.save(noteEntityToUpdate);

            return createNoteResponse(noteEntityToUpdate);

        }catch (InvalidInputException e) {
            log.error("Note Update <:::> An Exception thrown : ",e);
            return new NoteResponseDTO(e.getMessage());
        }
        catch (Exception e){
            log.error("Note Update :: An Exception thrown : ",e);
            return new NoteResponseDTO("Update failed, Please try again");
        }
    }

    @Override
    public NoteResponseDTO getNoteById(String noteId) {
        try{
            Optional<NoteEntity> noteEntity = noteRepository.findById(noteId);
            if (!noteEntity.isPresent()) {
                return new NoteResponseDTO("Note not found");
            }
            return createNoteResponse(noteEntity.get());
        } catch (Exception e) {
            log.error("Note Get <::> An Exception thrown : ",e);
            return new NoteResponseDTO("Get failed, Please try again");
        }
    }

    @Override
    public ApiResponse deleteNoteById(String noteId) {
        try{
            Optional<NoteEntity> noteEntity = noteRepository.findById(noteId);

            if (!noteEntity.isPresent()) {
                return new ApiResponse("Note not found");
            }
            noteRepository.delete(noteEntity.get());

            return new ApiResponse("Note deleted successfully");

        }catch (Exception e){
            log.error("Note Delete <::> An Exception thrown : ",e);
            return new ApiResponse("Delete failed, Please try again");
        }
    }

    @Override
    public Page<NoteResponseDTO> getAllNotes(String userId, int page, int size, String sortBy, String sortDir,String search) {

        try{

            Sort.Direction sortingOrder = sortDir.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;

            Pageable pageable = PageRequest.of(page, size, Sort.by( sortingOrder,sortBy));
            Page<NoteEntity> pageResponse;

            log.info("Page able information is : " + pageable);
            if (search != null && !search.isBlank()) {
                pageResponse= noteRepository.findAllByUserIdAndTitleContainingIgnoreCaseOrUserIdAndContentContaining(userId,search, userId,search,pageable);
            }else {
                pageResponse = noteRepository.findByUserId(userId, pageable);
            }
            return pageResponse.map(this::convertToNoteResponseDTO);

        }catch (Exception e){
            log.error("Note Get <::> An Exception thrown : ",e);
            return null;
        }
    }

    private NoteResponseDTO convertToNoteResponseDTO(NoteEntity noteEntity) {
        NoteResponseDTO noteResponseDTO = new NoteResponseDTO();
        noteResponseDTO.setId(noteEntity.getId());
        noteResponseDTO.setContent(noteEntity.getContent()!=null ? noteEntity.getContent() : null);
        noteResponseDTO.setUserId(noteEntity.getUserId());
        noteResponseDTO.setTitle(noteEntity.getTitle()!=null ? noteEntity.getTitle() : null);
        return noteResponseDTO;
    }
}
