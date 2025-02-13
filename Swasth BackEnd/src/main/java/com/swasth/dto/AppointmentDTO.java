package com.swasth.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import com.swasth.pojos.Appointment;

public class AppointmentDTO {
    private LocalDate appointmentDate;
    private LocalTime appointmentTime;
    private String status;
    private String doctorName; // Fetch doctor name instead of full Doctor entity

    public AppointmentDTO(Appointment appointment) {
        this.appointmentDate = appointment.getAppointmentDate();
        this.appointmentTime = appointment.getAppointmentTime();
        this.status = appointment.getStatus();
        this.doctorName = appointment.getDoctor().getFirstName(); 
    }

	public LocalDate getAppointmentDate() {
		return appointmentDate;
	}

	public void setAppointmentDate(LocalDate appointmentDate) {
		this.appointmentDate = appointmentDate;
	}

	public LocalTime getAppointmentTime() {
		return appointmentTime;
	}

	public void setAppointmentTime(LocalTime appointmentTime) {
		this.appointmentTime = appointmentTime;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getDoctorName() {
		return doctorName;
	}

	public void setDoctorName(String doctorName) {
		this.doctorName = doctorName;
	}

    // Getters and Setters
    
}
