package com.swasth.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.swasth.dao.DoctorDao;
import com.swasth.dto.DoctorDTO;
import com.swasth.pojos.Doctors;

import jakarta.persistence.EntityNotFoundException;

@Service
@Transactional
public class DoctorServiceImpl implements DoctorService {
    
    private final DoctorDao doctorDao;

    // Constructor injection instead of field injection
    @Autowired
    public DoctorServiceImpl(DoctorDao doctorDao) {
        this.doctorDao = doctorDao;
    }

    @Override
    public boolean addDoctor(Doctors doctor) {
        try {
            validateDoctor(doctor);
            doctorDao.save(doctor);
            return true;
        } catch (DataIntegrityViolationException e) {
            throw new IllegalStateException("Doctor details violate database constraints", e);
        } catch (Exception e) {
            throw new RuntimeException("Failed to add doctor", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Doctors readDoctorsDetails(int id) {
        return doctorDao.findById(id)
            .orElseThrow(() -> new EntityNotFoundException(
                "Doctor not found with ID: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DoctorDTO> getAllDoctors(String search, Pageable pageable) {
        if (search != null && !search.trim().isEmpty()) {
            return doctorDao.findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseOrExpertiseFieldContainingIgnoreCase(
                search, search, search, pageable
            ).map(this::convertToDTO);
        } else {
            return doctorDao.findAll(pageable).map(this::convertToDTO);
        }
    }
    private DoctorDTO convertToDTO(Doctors doctor) {
        return new DoctorDTO(
            doctor.getDoctorsId(),           // Unique doctor ID
            doctor.getFirstName(),
            doctor.getLastName(),
            doctor.getEmail(),
            doctor.getMobileNumber(),
            doctor.getGender(),
            doctor.getAge(),
            doctor.getExperience(),
            doctor.getDegree(),
            doctor.getExpertiseField()
        );
    }
    @Override
    public Doctors updateDoctorDetails(Doctors doctor) {
        try {
            validateDoctor(doctor);
            
            // Check if doctor exists
            if (!doctorDao.existsById(doctor.getDoctorsId())) {
                throw new EntityNotFoundException(
                    "Doctor not found with ID: " + doctor.getDoctorsId());
            }

            // Get existing doctor to preserve unchanged fields
            Doctors existingDoctor = doctorDao.findById(doctor.getDoctorsId()).get();
            updateDoctorFields(existingDoctor, doctor);
            
            return doctorDao.save(existingDoctor);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalStateException("Doctor details violate database constraints", e);
        }
    }

    @Override
    public boolean deleteDoctor(int id) {
        try {
            if (!doctorDao.existsById(id)) {
                return false;
            }
            doctorDao.deleteById(id);
            return true;
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete doctor with ID: " + id, e);
        }
    }

    private void validateDoctor(Doctors doctor) {
        if (doctor == null) {
            throw new IllegalArgumentException("Doctor cannot be null");
        }
        
        // Validate required fields
        if (isNullOrEmpty(doctor.getFirstName())) {
            throw new IllegalArgumentException("First name is required");
        }
        if (isNullOrEmpty(doctor.getLastName())) {
            throw new IllegalArgumentException("Last name is required");
        }
        if (isNullOrEmpty(doctor.getEmail())) {
            throw new IllegalArgumentException("Email is required");
        }
        if (isNullOrEmpty(doctor.getPassword())) {
            throw new IllegalArgumentException("Password is required");
        }
        if (isNullOrEmpty(doctor.getDegree())) {
            throw new IllegalArgumentException("Degree is required");
        }
        if (isNullOrEmpty(doctor.getExpertiseField())) {
            throw new IllegalArgumentException("Expertise field is required");
        }
        
        // Validate data constraints
        if (doctor.getMobileNumber() != 0 && String.valueOf(doctor.getMobileNumber()).length() != 10) {
            throw new IllegalArgumentException("Mobile number must be 10 digits");
        }
        if (doctor.getAge() < 18 || doctor.getAge() > 100) {
            throw new IllegalArgumentException("Age must be between 18 and 100");
        }
        if (doctor.getExperience() < 0) {
            throw new IllegalArgumentException("Experience cannot be negative");
        }
        if (!isNullOrEmpty(doctor.getGender()) && 
            !doctor.getGender().equalsIgnoreCase("male") && 
            !doctor.getGender().equalsIgnoreCase("female") && 
            !doctor.getGender().equalsIgnoreCase("other")) {
            throw new IllegalArgumentException("Invalid gender value");
        }
    }

    private void updateDoctorFields(Doctors existingDoctor, Doctors updatedDoctor) {
        // Basic information
        if (!isNullOrEmpty(updatedDoctor.getFirstName())) {
            existingDoctor.setFirstName(updatedDoctor.getFirstName());
        }
        if (!isNullOrEmpty(updatedDoctor.getLastName())) {
            existingDoctor.setLastName(updatedDoctor.getLastName());
        }
        if (!isNullOrEmpty(updatedDoctor.getEmail())) {
            existingDoctor.setEmail(updatedDoctor.getEmail());
        }
        if (!isNullOrEmpty(updatedDoctor.getPassword())) {
            existingDoctor.setPassword(updatedDoctor.getPassword());
        }
        
        // Professional details
        if (!isNullOrEmpty(updatedDoctor.getExpertiseField())) {
            existingDoctor.setExpertiseField(updatedDoctor.getExpertiseField());
        }
        if (!isNullOrEmpty(updatedDoctor.getDegree())) {
            existingDoctor.setDegree(updatedDoctor.getDegree());
        }
        if (updatedDoctor.getExperience() > 0) {
            existingDoctor.setExperience(updatedDoctor.getExperience());
        }
        
        // Personal details
        if (updatedDoctor.getMobileNumber() != 0) {
            existingDoctor.setMobileNumber(updatedDoctor.getMobileNumber());
        }
        if (!isNullOrEmpty(updatedDoctor.getGender())) {
            existingDoctor.setGender(updatedDoctor.getGender());
        }
        if (updatedDoctor.getAge() > 0) {
            existingDoctor.setAge(updatedDoctor.getAge());
        }
        
        // Relations
        if (updatedDoctor.getHospital() != null) {
            existingDoctor.setHospital(updatedDoctor.getHospital());
        }
        if (updatedDoctor.getPatient() != null) {
            existingDoctor.setPatient(updatedDoctor.getPatient());
        }
    }

    // Helper method to check for null or empty strings
    private boolean isNullOrEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }

	@Override
	public Doctors save(Doctors doctor) {
        return doctorDao.save(doctor); // Save doctor entity to DB
    }

	@Override
	public Doctors findByEmail(String email) {
        return doctorDao.findByEmail(email);
    }

	@Override
	public Doctors findByMobileNumber(Long mobileNumber) {
        return doctorDao.findByMobileNumber(mobileNumber);
    }
}