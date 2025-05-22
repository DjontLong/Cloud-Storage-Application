package com.djontlong.cloudstorage.controller;

import com.djontlong.cloudstorage.exceptions.DuplicatedFileException;
import com.djontlong.cloudstorage.exceptions.StorageException;
import com.djontlong.cloudstorage.model.File;
import com.djontlong.cloudstorage.model.User;
import com.djontlong.cloudstorage.services.FileService;
import com.djontlong.cloudstorage.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/files")
public class FilesController {

	private final FileService fileService;
	private final UserService userService;

	@Autowired
	public FilesController(FileService fileService, UserService userService) {
		this.fileService = fileService;
		this.userService = userService;
	}

	@PostMapping
	public String handleFileUpload(@RequestParam("fileUpload") MultipartFile fileUpload,
								   Authentication auth,
								   RedirectAttributes redirectAttributes) {
		try {
			User user = userService.getUser(auth.getName());

			if (fileUpload.isEmpty()) {
				throw new IllegalArgumentException("Please select a file to upload");
			}

			fileService.addFile(fileUpload, user.getUserId());
			redirectAttributes.addFlashAttribute("success", "File uploaded successfully!");
		} catch (DuplicatedFileException e) {
			redirectAttributes.addFlashAttribute("error", "File with this name already exists");
		} catch (IllegalArgumentException e) {
			redirectAttributes.addFlashAttribute("error", e.getMessage());
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("error", "Failed to upload file: " + e.getMessage());
		}

		return "redirect:/home";
	}

	@GetMapping("/delete/{id}")
	public String deleteFile(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
		try {
			fileService.deleteFile(id);
			redirectAttributes.addFlashAttribute("success", "File deleted successfully!");
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("error", "Failed to delete file: " + e.getMessage());
		}
		return "redirect:/home";
	}

	@GetMapping("/download/{id}")
	public ResponseEntity<Resource> downloadFile(@PathVariable Integer id) {
		try {
			File file = fileService.getFile(id);
			byte[] fileData = fileService.downloadFile(id);

			ByteArrayResource resource = new ByteArrayResource(fileData);

			return ResponseEntity.ok()
					.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFileName() + "\"")
					.contentType(MediaType.parseMediaType(file.getContentType()))
					.contentLength(fileData.length)
					.body(resource);
		} catch (Exception e) {
			throw new StorageException("Failed to download file", e);
		}
	}

	@ExceptionHandler(MaxUploadSizeExceededException.class)
	public String handleMaxSizeException(RedirectAttributes redirectAttributes) {
		redirectAttributes.addFlashAttribute("error", "File size exceeds maximum allowed limit");
		return "redirect:/home";
	}

	@ExceptionHandler(StorageException.class)
	public String handleStorageException(StorageException e, RedirectAttributes redirectAttributes) {
		redirectAttributes.addFlashAttribute("error", e.getMessage());
		return "redirect:/home";
	}
}