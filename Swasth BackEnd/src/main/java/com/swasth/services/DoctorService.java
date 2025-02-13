package com.swasth.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.swasth.dto.DoctorDTO;
import com.swasth.pojos.Doctors;

import jakarta.persistence.EntityNotFoundException;

public interface DoctorService {
    boolean addDoctor(Doctors doctor);
    Doctors readDoctorsDetails(int id) throws EntityNotFoundException;
    Doctors updateDoctorDetails(Doctors doctor);
    boolean deleteDoctor(int id);
	Doctors save(Doctors doctor);
	Doctors findByEmail(String email);
	Doctors findByMobileNumber(Long mobileNumber);
	Page<DoctorDTO> getAllDoctors(String search, Pageable pageable);
}