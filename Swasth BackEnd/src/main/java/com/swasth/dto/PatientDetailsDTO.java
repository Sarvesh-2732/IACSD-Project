package com.swasth.dto;

import java.util.List;

import com.swasth.pojos.Patient;

public class PatientDetailsDTO {
    private int patientId;
    private String firstName;
    private String email;
    private String mobileNumber;
    private int age;
    private List<AppointmentDTO> appointments; // List of appointment details

    public PatientDetailsDTO(Patient patient, List<AppointmentDTO> appointments) {
        this.patientId = patient.getPatientId();
        this.firstName= patient.getFirstName();
        this.email = patient.getEmail();
        this.mobileNumber = patient.getMobileNumber();
        this.age = patient.getAge();
        this.appointments = appointments;
    }

	public int getPatientId() {
		return patientId;
	}

	public void setPatientId(int patientId) {
		this.patientId = patientId;
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

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public List<AppointmentDTO> getAppointments() {
		return appointments;
	}

	public void setAppointments(List<AppointmentDTO> appointments) {
		this.appointments = appointments;
	}

	    
}
