package com.swasth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
//@AllArgsConstructor
public class HospitalRegistration {
    private String name;
    private String email;
    private String password;
    private String location;
    private String speciality;
    private Integer totalRooms;
    private String testsFacility;
    private String treatmentFacility;
  
    public HospitalRegistration(String name, String email, String password, String location, String speciality,
			Integer totalRooms, String testsFacility, String treatmentFacility) {
		super();
		this.name = name;
		this.email = email;
		this.password = password;
		this.location = location;
		this.speciality = speciality;
		this.totalRooms = totalRooms;
		this.testsFacility = testsFacility;
		this.treatmentFacility = treatmentFacility;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getSpeciality() {
		return speciality;
	}

	public void setSpeciality(String speciality) {
		this.speciality = speciality;
	}

	public Integer getTotalRooms() {
		return totalRooms;
	}

	public void setTotalRooms(Integer totalRooms) {
		this.totalRooms = totalRooms;
	}

	public String getTestsFacility() {
		return testsFacility;
	}

	public void setTestsFacility(String testsFacility) {
		this.testsFacility = testsFacility;
	}

	public String getTreatmentFacility() {
		return treatmentFacility;
	}

	public void setTreatmentFacility(String treatmentFacility) {
		this.treatmentFacility = treatmentFacility;
	}
    
}