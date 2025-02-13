package com.swasth.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.swasth.pojos.Doctors;
import com.swasth.pojos.Hospital;

public interface DoctorDao extends JpaRepository<Doctors, Integer> {

	Doctors findByEmail(String email);	
	Doctors findByMobileNumber(Long mobileNumber);
	List<Doctors> findByHospital(Hospital hospital);
	  Page<Doctors> findByHospital(Hospital hospital, Pageable pageable);
	  Page<Doctors> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseOrExpertiseFieldContainingIgnoreCase(
		        String firstName, String lastName, String expertiseField, Pageable pageable);
}
