package com.swasth.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.swasth.pojos.Rooms;

public interface RoomDao extends JpaRepository<Rooms, Integer> {
	List<Rooms> findByHospital_HospitalId(int hospitalId);
	boolean existsByRoomIdAndHospital_HospitalId(int roomId, int hospitalId);	

}
