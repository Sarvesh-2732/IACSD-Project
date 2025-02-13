package com.swasth.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.swasth.pojos.Hospital;

public interface HospitalDao extends JpaRepository<Hospital, Integer> {

	Hospital findByEmail(String email);
	 
}
