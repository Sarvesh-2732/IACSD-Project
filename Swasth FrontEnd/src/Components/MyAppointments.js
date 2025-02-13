import { useState, useEffect } from "react";
import axios from "axios";
import { motion } from "framer-motion";
import { useNavigate } from "react-router-dom";
import { FaSort, FaTimes, FaFilter } from "react-icons/fa";

const MyAppointments = () => {
  const [appointments, setAppointments] = useState([]);
  const [sortOrder, setSortOrder] = useState("asc");
  const [currentPage, setCurrentPage] = useState(1);
  const [totalPages, setTotalPages] = useState(1);
  const [doctorFilter, setDoctorFilter] = useState("");
  const [hospitalFilter, setHospitalFilter] = useState("");
  const [dateFilter, setDateFilter] = useState("");
  const navigate = useNavigate();

  // Get patient ID from local storage (Assumption: It's stored after login)
  const patientId = localStorage.getItem("patientId");

  useEffect(() => {
    fetchAppointments();
  }, [sortOrder, currentPage, doctorFilter, hospitalFilter, dateFilter]);

  const fetchAppointments = async () => {
    try {
      const response = await axios.get(`/api/appointments/patient/${patientId}`, {
        params: {
          page: currentPage,
          sort: sortOrder,
          doctorName: doctorFilter,
          hospitalName: hospitalFilter,
          appointmentDate: dateFilter,
        },
      });
      setAppointments(response.data.appointments);
      setTotalPages(response.data.totalPages);
    } catch (error) {
      console.error("Error fetching appointments:", error);
    }
  };

  const handleSortToggle = () => {
    setSortOrder(sortOrder === "asc" ? "desc" : "asc");
  };

  const handleCancel = async (appointmentId) => {
    if (!window.confirm("Are you sure you want to cancel this appointment?")) return;

    try {
      await axios.delete(`/api/appointments/${appointmentId}`);
      fetchAppointments(); // Refresh the list
      alert("Appointment canceled successfully.");
    } catch (error) {
      console.error("Error canceling appointment:", error);
    }
  };

  return (
    <div className="min-h-screen bg-gradient-to-br from-gray-50 to-blue-100 py-10 px-4">
      <div className="max-w-4xl mx-auto">
        <h2 className="text-4xl font-bold mb-8 text-center text-gray-800">
          My Appointments
        </h2>

        {/* Filters & Sorting */}
        <div className="flex flex-col md:flex-row items-center justify-between gap-4 mb-8">
          <input
            type="text"
            placeholder="Filter by Doctor"
            className="w-full md:w-auto px-4 py-3 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-400 transition"
            value={doctorFilter}
            onChange={(e) => setDoctorFilter(e.target.value)}
          />
          <input
            type="text"
            placeholder="Filter by Hospital"
            className="w-full md:w-auto px-4 py-3 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-400 transition"
            value={hospitalFilter}
            onChange={(e) => setHospitalFilter(e.target.value)}
          />
          <input
            type="date"
            className="w-full md:w-auto px-4 py-3 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-400 transition"
            value={dateFilter}
            onChange={(e) => setDateFilter(e.target.value)}
          />
          <button
            onClick={handleSortToggle}
            className="flex items-center px-6 py-3 bg-blue-500 text-white rounded-lg font-semibold transition transform hover:scale-105 hover:bg-blue-600"
          >
            Sort by Date <FaSort className="ml-2" />
          </button>
          <button
            onClick={fetchAppointments}
            className="flex items-center px-6 py-3 bg-green-500 text-white rounded-lg font-semibold transition transform hover:scale-105 hover:bg-green-600"
          >
            Apply Filters <FaFilter className="ml-2" />
          </button>
        </div>

        {/* Appointment List */}
        <div className="bg-white p-6 rounded-lg shadow-md">
          {appointments.length > 0 ? (
            appointments.map((appointment) => (
              <motion.div
                key={appointment.appointmentId}
                initial={{ opacity: 0, y: 20 }}
                animate={{ opacity: 1, y: 0 }}
                transition={{ duration: 0.4 }}
                className="p-4 border-b border-gray-200 flex justify-between items-center hover:bg-gray-50 transition-colors"
              >
                <div>
                  <h3 className="text-xl font-semibold text-gray-800">
                    Dr. {appointment.doctor.firstName} {appointment.doctor.lastName}
                  </h3>
                  <p className="text-gray-600">{appointment.doctor.hospital.name}</p>
                  <p className="text-gray-600">
                    {appointment.appointmentDate} at {appointment.appointmentTime}
                  </p>
                </div>
                <button
                  onClick={() => handleCancel(appointment.appointmentId)}
                  className="p-2 text-red-500 hover:text-red-700 transition-colors"
                >
                  <FaTimes size={20} />
                </button>
              </motion.div>
            ))
          ) : (
            <p className="text-gray-600 text-center">No appointments found.</p>
          )}
        </div>

        {/* Pagination Controls */}
        <div className="mt-8 flex justify-center items-center space-x-4">
          <button
            disabled={currentPage === 1}
            onClick={() => setCurrentPage((prev) => Math.max(prev - 1, 1))}
            className="px-6 py-3 bg-gray-300 rounded-lg disabled:opacity-50 transition hover:bg-gray-400"
          >
            Previous
          </button>
          <span className="px-6 py-3 bg-gray-200 rounded-lg">
            {currentPage} / {totalPages}
          </span>
          <button
            disabled={currentPage === totalPages}
            onClick={() => setCurrentPage((prev) => Math.min(prev + 1, totalPages))}
            className="px-6 py-3 bg-gray-300 rounded-lg disabled:opacity-50 transition hover:bg-gray-400"
          >
            Next
          </button>
        </div>
      </div>
    </div>
  );
};

export default MyAppointments;
