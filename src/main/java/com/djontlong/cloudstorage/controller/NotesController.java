package com.djontlong.cloudstorage.controller;


import com.djontlong.cloudstorage.model.Note;
import com.djontlong.cloudstorage.model.User;
import com.djontlong.cloudstorage.services.NoteService;
import com.djontlong.cloudstorage.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class NotesController {
    @Autowired
    NoteService noteService;
    @Autowired
    UserService userService;
   

	@PostMapping("/notes")
	public String postNote(Authentication auth, Note note, RedirectAttributes redirectAttributes) {
		// получить имя текущего пользователя и установить его в объект
		User user = userService.getUser(auth.getName());
		note.setUserId(user.getUserId());
		// получить строковое представление NoteId для проверки на null
		String stringId = note.getStringId();

		// если id отсутствует, добавить заметку
		if(stringId.isEmpty()) {
			this.noteService.addNote(note);
			// успех
	     	redirectAttributes.addFlashAttribute("successEvent", "Заметка успешна создана!");
		}
		// если id был передан, обновить заметку
		else{
			note.setNoteId(Integer.parseInt(stringId));
			this.noteService.updateNote(note);
			// успех
	     	redirectAttributes.addFlashAttribute("successEvent", "Заметка успешна обновлена!");
		}
		
		return "redirect:/home";
	}
	
	@GetMapping("/notes/delete/{id}")
    public String deleteNotes(@PathVariable("id") String id, RedirectAttributes redirectAttributes) {
        Integer noteId = Integer.parseInt(id);
        noteService.deleteNote(noteId);

		// успех
     	redirectAttributes.addFlashAttribute("successEvent", "Заметка успешна удалена!");
        return "redirect:/home";
    }

}
