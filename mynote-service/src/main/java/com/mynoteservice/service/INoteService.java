package com.mynoteservice.service;

import com.mynotelibrary.dto.NoteRequestDTO;
import com.mynotelibrary.dto.NoteResponseDTO;
import com.mynotelibrary.exceptionmessage.ApiResponse;
import org.springframework.data.domain.Page;

public interface INoteService {


    public NoteResponseDTO createNote(NoteRequestDTO note);
    public NoteResponseDTO updateNote(NoteRequestDTO note);
    public NoteResponseDTO getNoteById(String noteId);
    public ApiResponse deleteNoteById(String noteId);

    public Page<NoteResponseDTO> getAllNotes(String createdById,int page, int size,
                                             String sortBy, String sortDir, String search);

}
