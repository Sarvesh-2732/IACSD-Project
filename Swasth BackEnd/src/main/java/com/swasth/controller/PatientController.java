package com.swasth.controller;

import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.swasth.dao.AppointmentDao;
import com.swasth.dto.ChangePasswordDTO;
import com.swasth.dto.PatientDetailsDTO;
import com.swasth.dto.PatientUpdateDTO;
import com.swasth.exception.PatientException;
import com.swasth.pojos.Patient;
import com.swasth.services.PatientService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/patient")  // Updated to match SecurityConfig path pattern
@CrossOrigin(origins = "http://localhost:3000")
public class PatientController {

    private final PatientService patientService;
    private final PasswordEncoder passwordEncoder;
    @Autowired
    private AppointmentDao appointmentRepository;

    @Autowired
    public PatientController(PatientService patientService, PasswordEncoder passwordEncoder) {
        this.patientService = patientService;
        this.passwordEncoder = passwordEncoder;
    }

    // Helper method to get current authenticated patient's ID
    private int getCurrentPatientId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("Authenticated user ID: " + authentication.getName());  // Debugging
        return Integer.parseInt(authentication.getName());
    }


    @PostMapping("/add")
    public ResponseEntity<String> insertPatient(@Valid @RequestBody Patient patient) {
        try {
            validatePatient(patient);
            
            // Encrypt password before saving
            patient.setPassword(passwordEncoder.encode(patient.getPassword()));
            
            boolean insertOperation = patientService.addPatient(patient);
            return insertOperation ? 
                ResponseEntity.ok("Patient added successfully") :
                ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to add patient");
        } catch (PatientException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("An unexpected error occurred: " + e.getMessage());
        }
    }

    @GetMapping("/details")
    public ResponseEntity<Patient> getCurrentPatientDetails() {
        try {
            int patientId = getCurrentPatientId();
            Patient patient = patientService.readPatientDetails(patientId);
            
            if (patient == null) {
                throw new PatientException("Patient details not found");
            }
            
            return ResponseEntity.ok(patient);
        } catch (PatientException e) {
            throw e;
        } catch (Exception e) {
            throw new PatientException("Error retrieving patient details");
        }
    }

    @GetMapping("/details/{id}")
    public ResponseEntity<PatientDetailsDTO> getPatientDetails(@PathVariable int id) {
        try {
            // Verify if the requesting patient is accessing their own details
            int currentPatientId = getCurrentPatientId();
            if (currentPatientId != id) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }

            // Fetch patient details along with their appointments
            PatientDetailsDTO patientDetails = patientService.getPatientDetailsWithAppointments(id);

            return ResponseEntity.ok(patientDetails);
        } catch (PatientException e) {
            throw e;
        } catch (Exception e) {
            throw new PatientException("Error retrieving patient details");
        }
    }

    @PreAuthorize("hasRole('PATIENT')")
    @PutMapping("/update")
    public ResponseEntity<String> updatePatientDetails(
        @Valid @RequestBody PatientUpdateDTO patientUpdateDTO
    ) {
        try {
            int currentPatientId = getCurrentPatientId();
            if (patientUpdateDTO.getPatientId() != currentPatientId) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("You can only update your own details");
            }

            // Encode password in the controller if provided
            if (patientUpdateDTO.getPassword() != null && !patientUpdateDTO.getPassword().isEmpty()) {
                String encodedPassword = passwordEncoder.encode(patientUpdateDTO.getPassword());
                patientUpdateDTO.setPassword(encodedPassword);
            }

            // Delegate to service (service will handle partial updates)
            Patient updatedPatient = patientService.updatePatientDetails(patientUpdateDTO);
            return ResponseEntity.ok("Profile updated successfully");

        } catch (PatientException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
    
    // Helper method to validate patient details
    private void validatePatient(Patient patient) {
        if (patient == null) {
            throw new PatientException("Patient details cannot be null");
        }
        if (patient.getFirstName() == null || patient.getFirstName().trim().isEmpty()) {
            throw new PatientException("First name is required");
        }
        if (patient.getLastName() == null || patient.getLastName().trim().isEmpty()) {
            throw new PatientException("Last name is required");
        }
        if (patient.getEmail() == null || patient.getEmail().trim().isEmpty()) {
            throw new PatientException("Email is required");
        }
        if (!isValidEmail(patient.getEmail())) {
            throw new PatientException("Invalid email format");
        }
        if (patient.getPassword() != null && patient.getPassword().length() < 6) {
            throw new PatientException("Password must be at least 6 characters long");
        }
    }

    @PreAuthorize("hasRole('PATIENT')")
    @PutMapping("/change-password")
    public ResponseEntity<String> changePassword(
        @Valid @RequestBody ChangePasswordDTO changePasswordDTO
    ) {
        try {
            int currentPatientId = getCurrentPatientId();
            
            // Fetch the patient from the service
            Patient patient = patientService.readPatientDetails(currentPatientId);
            
            // Validate old password in the controller
            if (passwordEncoder.matches(changePasswordDTO.getOldPassword(), patient.getPassword())) {
                throw new PatientException("Old password is incorrect");
            }
            
            // Encode new password in the controller
            String encodedNewPassword = passwordEncoder.encode(changePasswordDTO.getNewPassword());
            
            // Pass only the encoded new password to the service
            patientService.updatePassword(currentPatientId, encodedNewPassword);
            
            return ResponseEntity.ok("Password changed successfully");
        } catch (PatientException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }


    
    // Helper method to validate email format
    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        return pattern.matcher(email).matches();
    }
}