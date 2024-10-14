package com.paymentmanagement.payment.controllers;

import java.io.InputStream;
//import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.*;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.paymentmanagement.payment.models.User;
import com.paymentmanagement.payment.models.UserDto;
import com.paymentmanagement.payment.services.UsersRepository;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/users")
public class UsersController {

	@Autowired
	private UsersRepository repo;
	
	@GetMapping({"", "/"})
	public String showUserList(Model model) {
		List<User> users = repo.findAll(Sort.by(Sort.Direction.DESC, "id"));
		model.addAttribute("users", users);
		return "users/index";
	}
	
	@GetMapping("/create")
	public String showCreatePage(Model model) {
		UserDto userDto = new UserDto();	
		model.addAttribute("userDto", userDto);
		return "users/CreateUser";
	}
	
	@PostMapping("/create")
	public String createuser(@Valid @ModelAttribute UserDto userDto,
			BindingResult result) {
		if(userDto.getUserImageFileName().isEmpty()) {
			result.addError(new FieldError("userDto", "userImageFileName", "The image file is required"));
		}
		
		if(result.hasErrors()) {
			return "users/CreateUser";
		}
		
		//save image file
		MultipartFile image = userDto.getUserImageFileName();
		Date createdAt = new Date();
		String storageFileName = createdAt.getTime() + "_" + image.getOriginalFilename();
		
		try {
			String uploadDir = "public/usersimages/";
			Path uploadPath = Paths.get(uploadDir);
			
			if(!Files.exists(uploadPath)) {
				Files.createDirectories(uploadPath);
			}
			
			try(InputStream inputStream = image.getInputStream()){
				Files.copy(inputStream, Paths.get(uploadDir + storageFileName),
						StandardCopyOption.REPLACE_EXISTING);
			}
		}catch (Exception ex) {
			System.out.println("Exception: "+ ex.getMessage());
		}
		
		User user = new User();
		user.setLastName(userDto.getLastName());
		user.setFirstName(userDto.getFirstName());
		user.setEmail(userDto.getEmail());
		user.setNumber(userDto.getNumber());
		user.setPasswords(userDto.getPasswords());
		user.setAbout(userDto.getAbout());
		user.setUserImageFileName(storageFileName);
		
		repo.save(user);
		
		return "redirect:/users";
	}
	
	@GetMapping("/edit")
	public String showEditpage(Model model, @RequestParam int id) {
		try {
			User user = repo.findById(id).get();
			model.addAttribute("user", user);
			
			UserDto userDto = new UserDto();
			userDto.setLastName(user.getLastName());
			userDto.setFirstName(user.getFirstName());
			userDto.setEmail(user.getEmail());
			userDto.setNumber(user.getNumber());
			userDto.setPasswords(user.getPasswords());
			userDto.setAbout(user.getAbout());
			//user.setUserImageFileName(storageFileName);
			
			model.addAttribute("userDto", userDto);
		}catch(Exception ex) {
			System.out.println("Exception: " + ex.getMessage());
			return "redirect:/users";
		}
		return "users/EditUser";
	}
	
	@PostMapping("/edit")
	public String updateUser(Model model,
			@RequestParam int id,
			@Valid @ModelAttribute UserDto userDto,
			BindingResult result) {
		try {
			User user = repo.findById(id).get();
			model.addAttribute("user", user);
			
			if(result.hasErrors()) {
				return "users/EditUser";
			}
			
			if(!userDto.getUserImageFileName().isEmpty()) {
				//delete old image
				String uploadDir = "public/usersimages/";
				Path oldImagePath = Paths.get(uploadDir + user.getUserImageFileName());
				
				try {
					Files.delete(oldImagePath);
				}catch(Exception ex) {
					System.out.println("Exception: " + ex.getMessage());
				}
				
				//save image file
				MultipartFile image = userDto.getUserImageFileName();
				Date createdAt = new Date();
				String storageFileName = createdAt.getTime() + "_" + image.getOriginalFilename();
				
				try(InputStream inputStream = image.getInputStream()){
					Files.copy(inputStream, Paths.get(uploadDir + storageFileName),
							StandardCopyOption.REPLACE_EXISTING);
				}
				
				user.setUserImageFileName(storageFileName);
			}
			
			user.setLastName(userDto.getLastName());
			user.setFirstName(userDto.getFirstName());
			user.setEmail(userDto.getEmail());
			user.setNumber(userDto.getNumber());
			user.setPasswords(userDto.getPasswords());
			user.setAbout(userDto.getAbout());
			
			repo.save(user);
			
		}catch(Exception ex) {
			System.out.println("Exception: " + ex.getMessage());
		}
		return "redirect:/users";
	}
	
	@GetMapping("/delete")
	public String deleteUser(@RequestParam int id) {
		
		try {
			User user = repo.findById(id).get();
			
			//delete user image
			Path imagePath = Paths.get("public/usersimages/" + user.getUserImageFileName());
			try {
				Files.delete(imagePath);
			}catch(Exception ex) {
				System.out.println("Exception: " + ex.getMessage());
			}
			
			//delete the user in database
			repo.delete(user);
			
		}catch(Exception ex) {
			System.out.println("Exception: " + ex.getMessage());
		}
		return "redirect:/users";
	}
}
