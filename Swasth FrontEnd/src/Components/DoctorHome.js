import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import axios from "axios";
import { motion } from "framer-motion";

const DoctorHome = () => {
  const [doctor, setDoctor] = useState(null);
  const [appointments, setAppointments] = useState([]);
  const navigate = useNavigate();

  useEffect(() => {
    const fetchDoctorDetails = async () => {
      try {
        const doctorId = localStorage.getItem("userId");
        const token = localStorage.getItem("token");

        if (!doctorId || !token) {
          navigate("/login/doctor");
          return;
        }

        // Fetch doctor details
        const response = await axios.get(
          `http://localhost:3500/api/doctor/details/${doctorId}`,
          { headers: { Authorization: `Bearer ${token}` } }
        );
        setDoctor(response.data);

        // Fetch doctor appointments
        const appointmentResponse = await axios.get(
          `http://localhost:3500/api/doctor/${doctorId}/appointments`,
          { headers: { Authorization: `Bearer ${token}` } }
        );
        setAppointments(appointmentResponse.data);
      } catch (error) {
        console.error("Error fetching doctor details or appointments:", error);
      }
    };

    fetchDoctorDetails();
  }, [navigate]);

  if (!doctor) {
    return (
      <div className="min-h-screen flex items-center justify-center bg-gradient-to-br from-gray-100 to-blue-50">
        <p className="text-center text-xl font-semibold text-gray-600">
          Loading doctor details...
        </p>
      </div>
    );
  }

  // Function to apply status colors
  const getStatusClass = (status) => {
    switch (status.toLowerCase()) {
      case "scheduled":
        return "text-blue-600 bg-blue-100";
      case "completed":
        return "text-green-600 bg-green-100";
      case "cancelled":
        return "text-red-600 bg-red-100";
      default:
        return "text-gray-600 bg-gray-100";
    }
  };

  return (
    <div className="min-h-screen bg-gradient-to-br from-gray-100 to-blue-50 p-6">
      <motion.div
        initial={{ opacity: 0, y: 20 }}
        animate={{ opacity: 1, y: 0 }}
        transition={{ duration: 0.6 }}
        className="max-w-4xl mx-auto bg-white p-8 rounded-2xl shadow-2xl"
      >
        <h2 className="text-4xl font-bold text-gray-800 mb-6">
          Welcome, Dr. {doctor.firstName}
        </h2>
        <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
          <p className="text-lg text-gray-700">
            <span className="font-semibold">Specialization:</span>{" "}
            {doctor.expertiseField}
          </p>
          <p className="text-lg text-gray-700">
            <span className="font-semibold">Email:</span> {doctor.email}
          </p>
          <p className="text-lg text-gray-700">
            <span className="font-semibold">Phone:</span> {doctor.mobileNumber}
          </p>
          <p className="text-lg text-gray-700">
            <span className="font-semibold">Experience:</span>{" "}
            {doctor.experience} years
          </p>
        </div>

        {/* Appointments Section */}
        <div className="mt-8">
          <h3 className="text-3xl font-semibold text-gray-800 mb-4">
            Your Appointments
          </h3>
          {appointments.length > 0 ? (
            <div className="overflow-x-auto">
              <table className="w-full border-collapse border border-gray-300 rounded-lg shadow-md">
                <thead>
                  <tr className="bg-blue-500 text-white">
                    <th className="border border-gray-300 px-6 py-3 text-left">Patient Name</th>
                    <th className="border border-gray-300 px-6 py-3 text-left">Date</th>
                    <th className="border border-gray-300 px-6 py-3 text-left">Time</th>
                    <th className="border border-gray-300 px-6 py-3 text-left">Status</th>
                  </tr>
                </thead>
                <tbody>
                  {appointments.map((appointment, index) => (
                    <tr
                      key={appointment.appointmentId}
                      className={`text-gray-700 text-center ${
                        index % 2 === 0 ? "bg-gray-50" : "bg-white"
                      } hover:bg-gray-100 transition duration-200`}
                    >
                      <td className="border border-gray-300 px-6 py-4 text-left">
                        {appointment.patient.firstName} {appointment.patient.lastName}
                      </td>
                      <td className="border border-gray-300 px-6 py-4 text-left">
                        {appointment.appointmentDate}
                      </td>
                      <td className="border border-gray-300 px-6 py-4 text-left">
                        {appointment.appointmentTime}
                      </td>
                      <td
                        className={`border border-gray-300 px-6 py-4 text-left font-semibold rounded-md ${getStatusClass(
                          appointment.status
                        )}`}
                      >
                        {appointment.status}
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          ) : (
            <p className="text-gray-600">No upcoming appointments.</p>
          )}
        </div>
      </motion.div>
    </div>
  );
};

export default DoctorHome;