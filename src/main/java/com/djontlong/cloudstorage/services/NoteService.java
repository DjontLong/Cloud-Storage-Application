package com.djontlong.cloudstorage.services;


import org.springframework.stereotype.Service;

import com.djontlong.cloudstorage.mapper.NoteMapper;
import com.djontlong.cloudstorage.model.Note;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Service
public class NoteService {
    private List<Note> listNotes;
    private final NoteMapper noteMapper;


    public NoteService(NoteMapper noteMapper) {
        this.noteMapper = noteMapper;
    }

    @PostConstruct
    public void postConstruct() {
        System.out.println("Creating NoteService bean");
    }

    public void addNote(Note note) {
        // добавление заметки в базу данных (объект Note)
        noteMapper.insert(note);

    }

    public List<Note> getNotes() {
        return noteMapper.getNotes();
    }

    public List<Note> getAllNotes(Integer userId){
        List<Note> notesList = noteMapper.getNotesByUser(userId);
        return notesList;
    }

    public Note getNote(int noteId) {
        Note updateNote = noteMapper.findNote(noteId);
        return updateNote;
    }

    public void updateNote(Note note) {
        // обновление в базе данных
        noteMapper.updateNote(note);

    }

    public void deleteNote(Integer noteId) {
        // удаление из базы данных
        noteMapper.delete(noteId);

    }
}
