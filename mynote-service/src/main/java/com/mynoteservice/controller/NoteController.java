package com.mynoteservice.controller;


import com.mynotelibrary.dto.NoteRequestDTO;
import com.mynotelibrary.dto.NoteResponseDTO;
import com.mynotelibrary.exceptionmessage.ApiResponse;
import com.mynoteservice.service.INoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("note")
public class NoteController {

    @Autowired
    private INoteService noteService;

    @PostMapping("/create")
    public ResponseEntity<NoteResponseDTO> createNote(@RequestBody NoteRequestDTO noteRequestDTO) {
        NoteResponseDTO noteResponseDTO = noteService.createNote(noteRequestDTO);
        return new ResponseEntity<>(noteResponseDTO, HttpStatus.CREATED);
    }

    @PutMapping("/update")
    public ResponseEntity<NoteResponseDTO> updateNote(@RequestBody NoteRequestDTO noteRequestDTO) {
        NoteResponseDTO noteResponseDTO = noteService.updateNote(noteRequestDTO);
        return new ResponseEntity<>(noteResponseDTO, HttpStatus.OK);
    }

    @GetMapping("/get/{noteId}")
    public ResponseEntity<NoteResponseDTO> getNoteById(@PathVariable String noteId) {
        NoteResponseDTO noteResponseDTO = noteService.getNoteById(noteId);
        return new ResponseEntity<>(noteResponseDTO, HttpStatus.OK);
    }

    @DeleteMapping("/deleted/{noteId}")
    public ResponseEntity<ApiResponse> deleteNoteById(@PathVariable String noteId) {
      ApiResponse response=  noteService.deleteNoteById(noteId);
      return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/gets")
    public ResponseEntity<Page<NoteResponseDTO>> getUserNotes(
            @RequestParam String userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir,
            @RequestParam(required = false) String search ) {

        Page<NoteResponseDTO> notesPage = noteService.getAllNotes(userId, page, size, sortBy, sortDir,search);
        return ResponseEntity.ok(notesPage);
    }
}
