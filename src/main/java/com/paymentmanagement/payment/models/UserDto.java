package com.paymentmanagement.payment.models;

import java.util.Date;

import org.springframework.web.multipart.MultipartFile;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotEmpty;

public class UserDto {
	@NotEmpty(message="The name is required")
	private String lastName;
	
	@NotEmpty(message="The lastname is required")
	private String firstName;
	
	@NotEmpty(message="The email is required")
	private String email;
	
	@NotEmpty(message="The number is required")
	private String number;
	
	@NotEmpty(message="The password is required")
	private String passwords;
	
	private String about;
	
	private MultipartFile userImageFileName;

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getPasswords() {
		return passwords;
	}

	public void setPasswords(String passwords) {
		this.passwords = passwords;
	}

	public String getAbout() {
		return about;
	}

	public void setAbout(String about) {
		this.about = about;
	}

	public MultipartFile getUserImageFileName() {
		return userImageFileName;
	}

	public void setUserImageFileName(MultipartFile userImageFileName) {
		this.userImageFileName = userImageFileName;
	}
    
	

	
	
}
