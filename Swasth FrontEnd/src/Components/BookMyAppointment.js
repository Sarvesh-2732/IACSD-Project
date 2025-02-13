import { useState, useEffect } from "react"; 
import { useNavigate } from "react-router-dom";
import axios from "axios";
import { FaSort } from "react-icons/fa";

const BookMyAppointment = () => {
  const [doctors, setDoctors] = useState([]);
  const [searchTerm, setSearchTerm] = useState("");
  const [sortOrder, setSortOrder] = useState("asc"); // Sorting Order
  const [currentPage, setCurrentPage] = useState(1);
  const [totalPages, setTotalPages] = useState(1);
  const navigate = useNavigate();

  // Fetch doctors from API when page, sortOrder, or searchTerm changes
  useEffect(() => {
    fetchDoctors();
  }, [currentPage, sortOrder, searchTerm]);

  const fetchDoctors = async () => {
    try {
      const response = await axios.get("http://localhost:3500/api/doctors", {//this end point is specially created to get the doctors details as in 
        //the back end only doctors can access the doctors controller and we were not able to get the data here so had to 
        //create a new controller which is permitted by all
        params: { page: currentPage, sort: sortOrder, search: searchTerm },
      });
      // With a Spring Data Page response, the list is in response.data.content
      setDoctors(response.data.content);
      setTotalPages(response.data.totalPages);
    } catch (error) {
      console.error("Error fetching doctors:", error);
    }
  };
  

  // Toggle sorting order
  const toggleSortOrder = () => {
    setSortOrder(sortOrder === "asc" ? "desc" : "asc");
  };

  // Handle search input
  const handleSearch = (e) => {
    setSearchTerm(e.target.value);
    setCurrentPage(1); // Reset to page 1 on new search
  };

  // Navigate to Doctor Details Page
  const handleDoctorClick = (doctorId) => {
    navigate(`/doctor/${doctorId}`);
  };

  return (
    <div className="min-h-screen bg-gradient-to-br from-gray-100 to-blue-50 py-10 px-4">
      <div className="max-w-7xl mx-auto">
        <h1 className="text-4xl font-bold text-center mb-8 text-gray-800">
          Book My Appointment
        </h1>

        {/* Search & Sort */}
        <div className="flex flex-col md:flex-row md:justify-between md:items-center mb-8 space-y-4 md:space-y-0">
          <input
            type="text"
            placeholder="Search by name or expertise..."
            className="w-full md:w-2/3 p-3 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 transition"
            value={searchTerm}
            onChange={handleSearch}
          />
          <button
            onClick={toggleSortOrder}
            className="w-full md:w-auto bg-blue-500 text-white py-3 px-6 rounded-md flex items-center justify-center transition-colors duration-200 hover:bg-blue-600"
          >
            Sort <FaSort className="ml-2" />
          </button>
        </div>

        {/* Doctor List */}
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-8">
          {doctors.length > 0 ? (
            doctors.map((doctor) => (
              <div
                key={doctor.id}
                className="bg-white p-6 rounded-lg shadow-md hover:shadow-xl transition-all duration-200 cursor-pointer"
                onClick={() => handleDoctorClick(doctor.doctorsId)}
              >
                <h2 className="text-2xl font-bold text-gray-800">{doctor.firstName}</h2>
                <p className="text-gray-600 mt-2">{doctor.hospital}</p>
                <p className="text-indigo-500 mt-1">{doctor.expertiseField}</p>
              </div>
            ))
          ) : (
            <p className="text-center col-span-full text-gray-500">
              No doctors found.
            </p>
          )}
        </div>

        {/* Pagination */}
        <div className="flex justify-center items-center mt-10 space-x-4">
          <button
            className="px-4 py-2 bg-gray-300 rounded disabled:opacity-50 transition hover:bg-gray-400"
            onClick={() => setCurrentPage((prev) => Math.max(prev - 1, 1))}
            disabled={currentPage === 1}
          >
            Previous
          </button>
          <span className="px-4 py-2 bg-gray-200 rounded">
            {`Page ${currentPage} of ${totalPages}`}
          </span>
          <button
            className="px-4 py-2 bg-gray-300 rounded disabled:opacity-50 transition hover:bg-gray-400"
            onClick={() => setCurrentPage((prev) => Math.min(prev + 1, totalPages))}
            disabled={currentPage === totalPages}
          >
            Next
          </button>
        </div>
      </div>
    </div>
  );
};

export default BookMyAppointment;
