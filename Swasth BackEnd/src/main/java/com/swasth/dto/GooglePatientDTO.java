package com.swasth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
//@AllArgsConstructor
//@NoArgsConstructor
public class GooglePatientDTO {
    private String email;
    private String firstName;
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public GooglePatientDTO(String email, String firstName) {
		super();
		this.email = email;
		this.firstName = firstName;
	}
	public GooglePatientDTO() {
		super();
	}
    
}
