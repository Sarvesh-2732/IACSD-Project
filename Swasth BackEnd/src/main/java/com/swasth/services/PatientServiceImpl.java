package com.swasth.services;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.swasth.dao.AppointmentDao;
import com.swasth.dao.PatientDao;
import com.swasth.dto.AppointmentDTO;
import com.swasth.dto.GooglePatientDTO;
import com.swasth.dto.PatientDetailsDTO;
import com.swasth.dto.PatientUpdateDTO;
import com.swasth.exception.PatientException;
import com.swasth.pojos.Appointment;
import com.swasth.pojos.Patient;

@Service
@Transactional
public class PatientServiceImpl implements PatientService {
	 @Autowired
	    private AppointmentDao appointmentRepository;

	   
    private final PatientDao patientDao;

    @Autowired
    public PatientServiceImpl(PatientDao patientDao) {
        this.patientDao = patientDao;
    }

    @Override
    public boolean addPatient(Patient patient) {
        validatePatient(patient);
        patientDao.save(patient);
        return true;
    }

    @Override
    @Transactional(readOnly = true)
    public Patient readPatientDetails(int id) {
        return patientDao.findById(id)
                .orElseThrow(() -> new PatientException("Patient not found with ID: " + id));
    }

    @Override
    public Patient updatePatientDetails(PatientUpdateDTO dto) throws PatientException {
        Patient existingPatient = patientDao.findById(dto.getPatientId())
            .orElseThrow(() -> new PatientException("Patient not found"));

        // Partial updates (only modify provided fields)
        if (dto.getFirstName() != null) existingPatient.setFirstName(dto.getFirstName());
        if (dto.getLastName() != null) existingPatient.setLastName(dto.getLastName());
        if (dto.getAge() != null) existingPatient.setAge(dto.getAge());
        if (dto.getMobileNumber() != null) existingPatient.setMobileNumber(dto.getMobileNumber());
        if (dto.getGender() != null) existingPatient.setGender(dto.getGender());
        if (dto.getWeight() != null) existingPatient.setWeight(dto.getWeight());
        if (dto.getHeight() != null) existingPatient.setHeight(dto.getHeight());
        if (dto.getEmail() != null) existingPatient.setEmail(dto.getEmail());
        if (dto.getPassword() != null) existingPatient.setPassword(dto.getPassword());

        return patientDao.save(existingPatient);
    }
    @Override    
    public void updatePassword(int patientId, String newPasswordEncoded) 
        throws PatientException {
        
        Patient patient = patientDao.findById(patientId)
            .orElseThrow(() -> new PatientException("Patient not found"));

        // Set the pre-encoded new password
        patient.setPassword(newPasswordEncoded);
        patientDao.save(patient);
    }

    @Override
    public boolean deletePatient(int id) {
        if (!patientDao.existsById(id)) {
            throw new PatientException("Patient with ID " + id + " does not exist");
        }
        patientDao.deleteById(id);
        return true;
    }

    // Validation Method
    private void validatePatient(Patient patient) {
        if (patient == null) {
            throw new IllegalArgumentException("Patient cannot be null");
        }
        if (isNullOrEmpty(patient.getFirstName())) {
            throw new PatientException("First name is required");
        }
        if (isNullOrEmpty(patient.getLastName())) {
            throw new PatientException("Last name is required");
        }
        if (isNullOrEmpty(patient.getEmail())) {
            throw new PatientException("Email is required");
        }
        if (patient.getPassword() != null && patient.getPassword().length() < 6) {
            throw new PatientException("Password must be at least 6 characters long");
        }
    }

    private boolean isNullOrEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }
    @Override
    public PatientDetailsDTO getPatientDetailsWithAppointments(int patientId) {
        Patient patient = patientDao.findById(patientId)
            .orElseThrow(() -> new PatientException("Patient not found with ID: " + patientId));

        // Fetch patient's appointments separately
        List<Appointment> appointments = appointmentRepository.findByPatient_PatientId(patientId);

        // Convert appointments to DTO format
        List<AppointmentDTO> appointmentDTOs = appointments.stream()
            .map(AppointmentDTO::new)
            .collect(Collectors.toList());

        return new PatientDetailsDTO(patient, appointmentDTOs);
    }

	@Override
	public Patient findByEmail(String email) {
        return patientDao.findByEmail(email);
    }

	@Override
	public Patient findByMobileNumber(String mobileNumber) {
        return patientDao.findByMobileNumber(mobileNumber);
    }	
	@Override
	public Patient save(Patient patient) {
        return patientDao.save(patient);
    }
	
	public Patient addGooglePatient(GooglePatientDTO googlePatientDTO) {
        Patient patient = new Patient();
        patient.setEmail(googlePatientDTO.getEmail());
        patient.setFirstName(googlePatientDTO.getFirstName());

        // Set default values for required fields
        patient.setLastName("Not Provided");
        patient.setAge(25); // Default age
        patient.setMobileNumber("0000000000"); // Dummy number
        patient.setGender("Other");
        patient.setWeight(new BigDecimal("60.0")); // Default weight
        patient.setHeight(new BigDecimal("1.7")); // Default height

        patientDao.save(patient);
        return patient;
    }
}
