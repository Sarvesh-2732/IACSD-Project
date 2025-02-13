import { useState, useEffect } from "react";
import { useParams, useNavigate } from "react-router-dom";
import axios from "axios";
import { motion } from "framer-motion";

const DoctorDetails = () => {
  const { doctorId } = useParams(); // Get doctorId from URL
  const [doctor, setDoctor] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const navigate = useNavigate();

  useEffect(() => {
    fetchDoctorDetails();
  }, [doctorId]);

  const fetchDoctorDetails = async () => {
    try {
      const response = await axios.get(`/api/doctors/${doctorId}`);
      setDoctor(response.data);
      setLoading(false);
    } catch (err) {
      setError("Failed to load doctor details.");
      setLoading(false);
    }
  };

  if (loading)
    return (
      <div className="min-h-screen flex items-center justify-center">
        <p className="text-center mt-10 text-xl font-semibold text-gray-600">
          Loading doctor details...
        </p>
      </div>
    );

  if (error)
    return (
      <div className="min-h-screen flex items-center justify-center">
        <p className="text-center mt-10 text-xl font-semibold text-red-500">
          {error}
        </p>
      </div>
    );

  return (
    <div className="min-h-screen bg-gradient-to-br from-blue-50 to-purple-50 py-10 px-4">
      <motion.div
        initial={{ opacity: 0, scale: 0.8, y: 50 }}
        animate={{ opacity: 1, scale: 1, y: 0 }}
        transition={{ duration: 0.5 }}
        className="max-w-3xl mx-auto p-8 bg-white rounded-xl shadow-2xl"
      >
        <div className="text-center mb-8">
          <h1 className="text-4xl font-bold text-gray-800">
            {doctor.firstName} {doctor.lastName}
          </h1>
          <p className="text-lg text-indigo-600 mt-2">{doctor.expertiseField}</p>
        </div>

        <div className="grid grid-cols-1 md:grid-cols-2 gap-6 mb-8">
          <div>
            <p className="text-gray-700 text-lg">
              <span className="font-semibold">Age:</span> {doctor.age}
            </p>
            <p className="text-gray-700 text-lg">
              <span className="font-semibold">Gender:</span> {doctor.gender}
            </p>
            <p className="text-gray-700 text-lg">
              <span className="font-semibold">Experience:</span> {doctor.experience} years
            </p>
          </div>
          <div>
            <p className="text-gray-700 text-lg">
              <span className="font-semibold">Degree:</span> {doctor.degree}
            </p>
            <p className="text-gray-700 text-lg">
              <span className="font-semibold">Mobile:</span> {doctor.mobileNumber}
            </p>
            <p className="text-gray-700 text-lg">
              <span className="font-semibold">Expertise:</span> {doctor.expertiseField}
            </p>
          </div>
        </div>

        <div className="border-t border-gray-200 pt-6 mb-8">
          <h2 className="text-2xl font-bold text-gray-800 mb-4">Hospital Information</h2>
          <p className="text-gray-700 text-lg">
            <span className="font-semibold">Name:</span> {doctor.hospital.name}
          </p>
          <p className="text-gray-700 text-lg">
            <span className="font-semibold">Location:</span> {doctor.hospital.location}
          </p>
          <p className="text-gray-700 text-lg">
            <span className="font-semibold">Specialty:</span> {doctor.hospital.speciality}
          </p>
          <p className="text-gray-700 text-lg">
            <span className="font-semibold">Total Rooms:</span> {doctor.hospital.totalRooms}
          </p>
        </div>

        <button
          className="w-full bg-blue-500 text-white py-3 rounded-xl font-semibold transition transform hover:scale-105 hover:bg-blue-600"
          onClick={() => navigate(`/book-appointment/${doctorId}`)}
        >
          Book Appointment
        </button>
      </motion.div>
    </div>
  );
};

export default DoctorDetails;
