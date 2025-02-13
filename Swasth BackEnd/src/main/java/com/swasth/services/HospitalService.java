package com.swasth.services;

import java.util.List;

import com.swasth.dto.DoctorDTO;
import com.swasth.dto.DoctorHospitalDTO;
import com.swasth.dto.HospitalDetailsDTO;
import com.swasth.pojos.Hospital;
import com.swasth.pojos.Rooms;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface HospitalService {
		boolean addHospital(Hospital hospital);
	
		HospitalDetailsDTO readHospitalDetails(int id);
		
		Hospital updateHospitalDetails(Hospital hospital);
		
		public boolean deleteHospital(int id) ;
		
		Page<DoctorDTO> getDoctorsByHospital(int hospitalId, Pageable pageable);
		
		 Hospital findById(int id);
		
		public boolean addRoomToHospital(int hospitalId, Rooms room);
		
		public List<Rooms> getRoomsByHospital(int hospitalId);
		
		public boolean updateRoomDetails(Rooms room);
		
		public boolean deleteRoom(int roomId) ;
		
		boolean isRoomBelongsToHospital(int roomId, int hospitalId);

		Hospital findByEmail(String email);

		Hospital save(Hospital hospital);
//		List<DoctorDTO> getDoctorsByHospital(int hospitalId) ;
		
}
