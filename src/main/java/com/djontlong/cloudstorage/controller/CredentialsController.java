package com.djontlong.cloudstorage.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.djontlong.cloudstorage.model.Credential;
import com.djontlong.cloudstorage.model.User;
import com.djontlong.cloudstorage.services.CredentialService;
import com.djontlong.cloudstorage.services.UserService;

@Controller
public class CredentialsController {
	@Autowired
	CredentialService credentialService;
	@Autowired
	UserService userService;

	@PostMapping("/credentials")
	public String postCredential(Authentication auth, Credential credential, RedirectAttributes redirectAttributes) {

		// получаем имя текущего пользователя и устанавливаем его в объект
		User user = userService.getUser(auth.getName());
		credential.setUserId(user.getUserId());

		// получаем строковое представление ID учетных данных для проверки
		String stringId = credential.getStringId();

		// если ID отсутствует, добавляем новые учетные данные
		if (stringId.isEmpty()) {
			this.credentialService.addCredential(credential);
			redirectAttributes.addFlashAttribute("successEvent", "Данные успешно сохранены!");
		}
		// если ID получен, обновляем существующие
		else {
			credential.setCredentialId(Integer.parseInt(stringId));
			this.credentialService.updateCredential(credential);
			redirectAttributes.addFlashAttribute("successEvent", "Данные успешно обновлены!");
		}

		return "redirect:/home";
	}

	@GetMapping("/credentials/delete/{id}")
	public String deleteCredentials(@PathVariable("id") String id, RedirectAttributes redirectAttributes) {
		Integer credentialId = Integer.parseInt(id);
		credentialService.deleteCredential(credentialId);
		redirectAttributes.addFlashAttribute("successEvent", "Данные успешно удалены!");
		return "redirect:/home";
	}

}
