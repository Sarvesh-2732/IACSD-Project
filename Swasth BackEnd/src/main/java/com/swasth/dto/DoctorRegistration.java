package com.swasth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
//@NoArgsConstructor
//@AllArgsConstructor
public class DoctorRegistration {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String mobileNumber;
    private String gender;
    private Integer age;
    private Integer experience;
    private String degree;
    private String expertiseField;
    
	public DoctorRegistration(String firstName, String lastName, String email, String password, String mobileNumber,
			String gender, Integer age, Integer experience, String degree, String expertiseField) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.password = password;
		this.mobileNumber = mobileNumber;
		this.gender = gender;
		this.age = age;
		this.experience = experience;
		this.degree = degree;
		this.expertiseField = expertiseField;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public Integer getExperience() {
		return experience;
	}

	public void setExperience(Integer experience) {
		this.experience = experience;
	}

	public String getDegree() {
		return degree;
	}

	public void setDegree(String degree) {
		this.degree = degree;
	}

	public String getExpertiseField() {
		return expertiseField;
	}

	public void setExpertiseField(String expertiseField) {
		this.expertiseField = expertiseField;
	}
	
}