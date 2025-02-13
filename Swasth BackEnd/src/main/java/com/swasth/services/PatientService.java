package com.swasth.services;

import com.swasth.dto.GooglePatientDTO;
import com.swasth.dto.PatientDetailsDTO;
import com.swasth.dto.PatientUpdateDTO;
import com.swasth.exception.PatientException;
import com.swasth.pojos.Patient;


public interface PatientService {
	
	boolean addPatient(Patient patient);
	
	Patient readPatientDetails(int id);
	
	public boolean deletePatient(int id) ;

	Patient findByEmail(String email);

	Patient findByMobileNumber(String mobileNumber);

	Patient save(Patient patient);

	PatientDetailsDTO getPatientDetailsWithAppointments(int patientId);
	Patient updatePatientDetails(PatientUpdateDTO dto) throws PatientException;
	void updatePassword(int patientId, String newPasswordEncoded) 
	        throws PatientException  ;
	
	Patient addGooglePatient(GooglePatientDTO googlePatientDTO) ;
}
