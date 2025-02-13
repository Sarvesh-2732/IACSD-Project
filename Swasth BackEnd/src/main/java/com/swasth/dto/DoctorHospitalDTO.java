package com.swasth.dto;

public class DoctorHospitalDTO {
	  private int doctorsId;
	    private String firstName;
	    private String lastName;
	    private long mobileNumber;
	    private String gender;
	    private int age;
	    private int experience;
	    private String degree;
	    private String expertiseField;
	    private String email;
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
		public String getEmail() {
			return email;
		}
		public void setEmail(String email) {
			this.email = email;
		}
		public DoctorHospitalDTO(int doctorsId, String firstName, String lastName, long mobileNumber, String gender,
				int age, int experience, String degree, String expertiseField, String email) {
			super();
			this.doctorsId = doctorsId;
			this.firstName = firstName;
			this.lastName = lastName;
			this.mobileNumber = mobileNumber;
			this.gender = gender;
			this.age = age;
			this.experience = experience;
			this.degree = degree;
			this.expertiseField = expertiseField;
			this.email = email;
		}
		public DoctorHospitalDTO() {
			super();
		}
	    
}
