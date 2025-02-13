package com.swasth.pojos;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name="doctors")
@Getter
@Setter
@ToString
public class Doctors extends Users{
	
	@Column(name="doctors_id")
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int doctorsId;
	@Column(name="first_name",length=50)
	private String firstName;

	@Column(name="last_name",length=50)
	private String lastName;
	
	@Column(name="mobile_number",unique=true)
	private long mobileNumber;
	
	@Column(name="gender",length=10)
	private String gender;
	
	@Column
	private int age;
	
	@Column
	private int experience;

	@Column(name="degree",length=200,nullable=false)
	private String degree;
	
	@Column(name="expertise_field",length=200,nullable=false)
	private String expertiseField;

	@ManyToOne
	@JoinColumn(name="hospital_id")
	private Hospital hospital; 

	@ManyToOne
	@JoinColumn(name="patient_id")
	private Patient patient;

	public int getDoctorsId() {
		return doctorsId;
	}

	public void setDoctorsId(int doctorsId) {
		this.doctorsId = doctorsId;
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

	public long getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(long mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public int getExperience() {
		return experience;
	}

	public void setExperience(int experience) {
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

	public Hospital getHospital() {
		return hospital;
	}

	public void setHospital(Hospital hospital) {
		this.hospital = hospital;
	}

	public Patient getPatient() {
		return patient;
	}

	public void setPatient(Patient patient) {
		this.patient = patient;
	}
	
	
	
}
