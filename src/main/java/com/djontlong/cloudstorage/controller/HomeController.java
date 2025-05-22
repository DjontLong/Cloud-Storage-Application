package com.djontlong.cloudstorage.controller;

import com.djontlong.cloudstorage.model.Note;
import com.djontlong.cloudstorage.model.User;
import com.djontlong.cloudstorage.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HomeController {
    @Autowired
    private NoteService noteService;
    
    @Autowired
    private CredentialService credentialService;

    @Autowired
    private FileService fileService;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private EncryptionService encryptionService;
	
	 @GetMapping("/home")
	    public ModelAndView getHome(Authentication auth, Note note) throws Exception {
		 	User user = userService.getUser(auth.getName());
		 
		 	if(user == null) {
	           return new ModelAndView(("login"));
	        }
	        
	        ModelAndView modelAndView = new ModelAndView("home");
	        modelAndView.addObject("notes", noteService.getAllNotes(user.getUserId()));
	        modelAndView.addObject("credentials", credentialService.getAllCredentials(user.getUserId()));
	        modelAndView.addObject("files", fileService.getAllFiles(user.getUserId()));
	        modelAndView.addObject("encryptionService", encryptionService);
	        
	        return modelAndView;
	    }
	 

}
