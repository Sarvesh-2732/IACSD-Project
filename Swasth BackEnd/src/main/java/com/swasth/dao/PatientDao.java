package com.swasth.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.swasth.pojos.Patient;

public interface PatientDao extends JpaRepository<Patient, Integer> {

	Patient findByEmail(String email);

	Patient findByMobileNumber(String mobileNumber);

}
