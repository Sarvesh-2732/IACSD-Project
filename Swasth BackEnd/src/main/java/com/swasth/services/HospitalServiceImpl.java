package com.swasth.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.swasth.dao.DoctorDao;
import com.swasth.dao.HospitalDao;
import com.swasth.dao.RoomDao;
import com.swasth.dto.DoctorDTO;
import com.swasth.dto.HospitalDetailsDTO;
import com.swasth.exception.HospitalException;
import com.swasth.exception.ResourceNotFoundException;
import com.swasth.pojos.Doctors;
import com.swasth.pojos.Hospital;
import com.swasth.pojos.Rooms;

import jakarta.persistence.EntityNotFoundException;

@Service
@Transactional
public class HospitalServiceImpl implements HospitalService {

    private final HospitalDao hospitalDao;
    private final RoomDao roomDao;
    private final DoctorDao doctorDao;
    @Autowired
    public HospitalServiceImpl(HospitalDao hospitalDao, RoomDao roomDao,DoctorDao doctorDao) {
        this.hospitalDao = hospitalDao;
        this.roomDao = roomDao;
        this.doctorDao=doctorDao;
    }

    // Add Hospital
    @Override
    public boolean addHospital(Hospital hospital) {
        validateHospital(hospital);
        try {
            hospitalDao.save(hospital);
            return true;
        } catch (DataIntegrityViolationException e) {
            throw new IllegalStateException("Hospital details violate database constraints", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public HospitalDetailsDTO readHospitalDetails(int id) {
        Hospital hospital = hospitalDao.findById(id)
            .orElseThrow(() -> new HospitalException("Hospital not found with ID: " + id));

        return HospitalDetailsDTO.convertToDTO(hospital);
    }
    
//    public List<DoctorDTO> getDoctorsByHospital(int hospitalId) {
//        Hospital hospital = hospitalDao.findById(hospitalId)
//            .orElseThrow(() -> new ResourceNotFoundException("Hospital not found with ID: " + hospitalId));
//
//        List<Doctors> doctors = doctorDao.findByHospital(hospital);
//
//        return doctors.stream()
//            .map(this::convertToDTO)
//            .collect(Collectors.toList());
//    }
    
    @Override
    public Page<DoctorDTO> getDoctorsByHospital(int hospitalId, Pageable pageable) {
        Hospital hospital = hospitalDao.findById(hospitalId)
            .orElseThrow(() -> new ResourceNotFoundException("Hospital not found with ID: " + hospitalId));

        Page<Doctors> doctorsPage = doctorDao.findByHospital(hospital, pageable);
        return doctorsPage.map(this::convertToDTO);
    }
    
    private DoctorDTO convertToDTO(Doctors doctor) {
        return new DoctorDTO(
            doctor.getDoctorsId(),
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
    
    // Update Hospital Details
    @Override
    public Hospital updateHospitalDetails(Hospital updatedHospital) {
        validateHospital(updatedHospital);
        Hospital existingHospital = hospitalDao.findById(updatedHospital.getHospitalId())
                .orElseThrow(() -> new EntityNotFoundException("Hospital not found with ID: " + updatedHospital.getHospitalId()));

        updateHospitalFields(existingHospital, updatedHospital);
        return hospitalDao.save(existingHospital);
    }

    // Delete Hospital
    @Override
    public boolean deleteHospital(int id) {
        if (!hospitalDao.existsById(id)) {
            throw new EntityNotFoundException("Hospital not found with ID: " + id);
        }
        hospitalDao.deleteById(id);
        return true;
    }

    // Room CRUD operations

    // Add Room to Hospital
    public boolean addRoomToHospital(int hospitalId, Rooms room) {
        validateRoom(room);
        Hospital hospital = hospitalDao.findById(hospitalId)
                .orElseThrow(() -> new EntityNotFoundException("Hospital not found with ID: " + hospitalId));

        room.setHospital(hospital);
        roomDao.save(room);
        return true;
    }

    // Get All Rooms of a Hospital
    @Transactional(readOnly = true)
    public List<Rooms> getRoomsByHospital(int hospitalId) {
        if (!hospitalDao.existsById(hospitalId)) {
            throw new EntityNotFoundException("Hospital not found with ID: " + hospitalId);
        }
        return roomDao.findByHospital_HospitalId(hospitalId);
    }

    // Update Room Details
    public boolean updateRoomDetails(Rooms updatedRoom) {
        validateRoom(updatedRoom);
        Rooms existingRoom = roomDao.findById(updatedRoom.getRoomId())
                .orElseThrow(() -> new EntityNotFoundException("Room not found with ID: " + updatedRoom.getRoomId()));

        updateRoomFields(existingRoom, updatedRoom);
        roomDao.save(existingRoom);
        return true;
    }

    // Delete Room
    public boolean deleteRoom(int roomId) {
        if (!roomDao.existsById(roomId)) {
            throw new EntityNotFoundException("Room not found with ID: " + roomId);
        }
        roomDao.deleteById(roomId);
        return true;
    }

    // Validation methods

    private void validateHospital(Hospital hospital) {
        if (hospital == null) {
            throw new IllegalArgumentException("Hospital cannot be null");
        }
        if (isNullOrEmpty(hospital.getName())) {
            throw new IllegalArgumentException("Hospital name is required");
        }
        if (isNullOrEmpty(hospital.getLocation())) {
            throw new IllegalArgumentException("Hospital location is required");
        }
        if (hospital.getTotalRooms() < 0) {
            throw new IllegalArgumentException("Total rooms cannot be negative");
        }
        if (isNullOrEmpty(hospital.getSpeciality())) {
            throw new IllegalArgumentException("Hospital speciality is required");
        }
    }

    private void validateRoom(Rooms room) {
        if (room == null) {
            throw new IllegalArgumentException("Room cannot be null");
        }
        if (isNullOrEmpty(room.getRoomNumber())) {
            throw new IllegalArgumentException("Room number is required");
        }
    }

    // Field Update Methods

    private void updateHospitalFields(Hospital existingHospital, Hospital updatedHospital) {
        if (!isNullOrEmpty(updatedHospital.getName())) {
            existingHospital.setName(updatedHospital.getName());
        }
        if (!isNullOrEmpty(updatedHospital.getLocation())) {
            existingHospital.setLocation(updatedHospital.getLocation());
        }
        if (!isNullOrEmpty(updatedHospital.getSpeciality())) {
            existingHospital.setSpeciality(updatedHospital.getSpeciality());
        }
        if (updatedHospital.getTotalRooms() > 0) {
            existingHospital.setTotalRooms(updatedHospital.getTotalRooms());
        }
        if (!isNullOrEmpty(updatedHospital.getTestsFacility())) {
            existingHospital.setTestsFacility(updatedHospital.getTestsFacility());
        }
        if (!isNullOrEmpty(updatedHospital.getTreatmentFacility())) {
            existingHospital.setTreatmentFacility(updatedHospital.getTreatmentFacility());
        }
    }

    private void updateRoomFields(Rooms existingRoom, Rooms updatedRoom) {
        if (!isNullOrEmpty(updatedRoom.getRoomNumber())) {
            existingRoom.setRoomNumber(updatedRoom.getRoomNumber());
        }
    }

    private boolean isNullOrEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean isRoomBelongsToHospital(int roomId, int hospitalId) {
        return roomDao.existsByRoomIdAndHospital_HospitalId(roomId, hospitalId);
    }

	@Override
	public Hospital findByEmail(String email) {
        return hospitalDao.findByEmail(email);
    }

	@Override
	public Hospital save(Hospital hospital) {
        return hospitalDao.save(hospital); // Save hospital entity to DB
    }

	@Override
	public Hospital findById(int id) {
		return hospitalDao.findById(id).orElseThrow(null);
	}

}
