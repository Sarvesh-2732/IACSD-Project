package com.swasth.pojos;

import java.time.LocalDate;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name="hospital")
@Getter
@Setter
@ToString
public class Hospital extends Users{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="hospital_id")
	private int hospitalId;
	
	@Column(name="name", length=100, unique=true)
	private String name;
	
	@Column(name="location")
	private String location;
	
	@Column(name="speciality", length=200)
	private String speciality;
	
	@Column(name="total_rooms")
	private int totalRooms;

	@Column(name="tests_facility", length=300)
	private String testsFacility;
	
	@Column(name="treatment_facility", length=200)
	private String treatmentFacility;
	
	@Column(name="registration_date")
	@CreationTimestamp	
	private LocalDate registrationDate;

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

	public LocalDate getRegistrationDate() {
		return registrationDate;
	}

	public void setRegistrationDate(LocalDate registrationDate) {
		this.registrationDate = registrationDate;
	}
	
	
	
}