package com.swasth.dto;

import com.swasth.pojos.Hospital;

public class HospitalDetailsDTO {
    private int hospitalId;
    private String name;
    private String location;
    private String speciality;
    private int totalRooms;
    private String testsFacility;
    private String treatmentFacility;
    private String email;
    // Don't include sensitive fields like password
    // Add any additional fields needed for the frontend
    
    // Getters and setters

public static HospitalDetailsDTO convertToDTO(Hospital hospital) {
    HospitalDetailsDTO dto = new HospitalDetailsDTO();
    dto.setHospitalId(hospital.getHospitalId());
    dto.setName(hospital.getName());
    dto.setLocation(hospital.getLocation());
    dto.setSpeciality(hospital.getSpeciality());
    dto.setTotalRooms(hospital.getTotalRooms());
    dto.setTestsFacility(hospital.getTestsFacility());
    dto.setTreatmentFacility(hospital.getTreatmentFacility());
    dto.setEmail(hospital.getEmail());
    return dto;
    
}

public int getHospitalId() {
	return hospitalId;
}

public void setHospitalId(int hospitalId) {
	this.hospitalId = hospitalId;
}

public String getName() {
	return name;
}

public void setName(String name) {
	this.name = name;
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

public int getTotalRooms() {
	return totalRooms;
}

public void setTotalRooms(int totalRooms) {
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

public String getEmail() {
	return email;
}

public void setEmail(String email) {
	this.email = email;
}
}