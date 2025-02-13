import React, { useState, useEffect } from "react";
import { Link } from "react-router-dom";
import { FaBars, FaTimes, FaUserCircle } from "react-icons/fa";

const Header = () => {
  const [isLoggedIn, setIsLoggedIn] = useState(false);
  const [userName, setUserName] = useState(null); // Initially null
  const [dropdownOpen, setDropdownOpen] = useState(false);
  const [mobileMenuOpen, setMobileMenuOpen] = useState(false);

  useEffect(() => {
    // Retrieve user info from localStorage on mount
    const storedUserId = localStorage.getItem("userId");
    const storedUserType = localStorage.getItem("userType");

    if (storedUserId && storedUserType === "patient") {
      setIsLoggedIn(true);
      setUserName("Loading..."); // Placeholder while fetching

      // Fetch user details from backend (optional)
      fetch(`http://localhost:3500/api/patient/details/${storedUserId}`, {
        headers: { Authorization: `Bearer ${localStorage.getItem("token")}` },
      })
        .then((res) => res.json())
        .then((data) => setUserName(data.firstName))
        .catch((err) => {
          console.error("Error fetching user details:", err);
          setUserName("User"); // Fallback if error occurs
        });
    }
    // For Hospital
else if (storedUserId && storedUserType === "hospital") {
  setIsLoggedIn(true);
  setUserName("Loading..."); // Placeholder while fetching

  // Fetch hospital details from backend
  fetch(`http://localhost:3500/api/hospital/details/${storedUserId}`, {
    headers: { Authorization: `Bearer ${localStorage.getItem("token")}` },
  })
    .then((res) => res.json())
    .then((data) => setUserName(data.name)) // Assuming hospital has 'name' field
    .catch((err) => {
      console.error("Error fetching hospital details:", err);
      setUserName("Hospital"); // Fallback if error occurs
    });
}

// For Doctor
else if (storedUserId && storedUserType === "doctor") {
  setIsLoggedIn(true);
  setUserName("Loading..."); // Placeholder while fetching

  // Fetch doctor details from backend
  fetch(`http://localhost:3500/api/doctor/details/${storedUserId}`, {
    headers: { Authorization: `Bearer ${localStorage.getItem("token")}` },
  })
    .then((res) => res.json())
    .then((data) => setUserName(data.firstName)) // Assuming doctor has 'firstName' field
    .catch((err) => {
      console.error("Error fetching doctor details:", err);
      setUserName("Doctor"); // Fallback if error occurs
    });
}
  }, []);

  const toggleDropdown = () => {
    setDropdownOpen(!dropdownOpen);
  };

  const handleLogout = () => {
    setIsLoggedIn(false);
    setDropdownOpen(false);
    localStorage.removeItem("userId");
    localStorage.removeItem("token");
    localStorage.removeItem("userType");
    window.location.reload(); // Refresh page to reflect logout
  };

  const toggleMobileMenu = () => {
    setMobileMenuOpen(!mobileMenuOpen);
  };

  return (
    <nav className="bg-gradient-to-r from-indigo-600 to-blue-500 text-white shadow-lg">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div className="flex justify-between h-16 items-center">
          {/* Logo */}
          <div className="flex-shrink-0">
            <Link to="/" className="text-2xl font-bold hover:text-gray-300">
              Swasth
            </Link>
          </div>

          {/* Desktop Navigation */}
          <div className="hidden md:flex md:space-x-8 items-center">
            <Link to="/" className="hover:text-gray-300">
              Home
            </Link>
            <Link to="/book-appointment" className="hover:text-gray-300">
              Book Appointment
            </Link>
          </div>

          {/* User Dropdown (Desktop) */}
          <div className="hidden md:flex items-center">
            {isLoggedIn ? (
              <div className="relative">
                <button
                  onClick={toggleDropdown}
                  className="flex items-center space-x-2 hover:text-gray-300 focus:outline-none"
                >
                  <FaUserCircle size={24} />
                  <span>{userName || "User"}</span>
                </button>
                {dropdownOpen && (
                  <div className="absolute right-0 mt-2 w-48 bg-white text-black rounded-md shadow-lg z-50 transition transform origin-top-right">
                    <Link
                      to="/profile"
                      className="block px-4 py-2 hover:bg-gray-100"
                    >
                      View Profile
                    </Link>
                    <Link
                      to="/change-password"
                      className="block px-4 py-2 hover:bg-gray-100"
                    >
                      Change Password
                    </Link>
                    <button
                      onClick={handleLogout}
                      className="w-full text-left px-4 py-2 hover:bg-gray-100"
                    >
                      Logout
                    </button>
                  </div>
                )}
              </div>
            ) : (
              <Link to="/login" className="hover:text-gray-300">
                Login
              </Link>
            )}
          </div>

          {/* Mobile Menu Button */}
          <div className="flex md:hidden">
            <button
              onClick={toggleMobileMenu}
              className="text-white hover:text-gray-300 focus:outline-none"
            >
              {mobileMenuOpen ? <FaTimes size={24} /> : <FaBars size={24} />}
            </button>
          </div>
        </div>
      </div>

      {/* Mobile Navigation Menu */}
      {mobileMenuOpen && (
        <div className="md:hidden bg-gradient-to-r from-indigo-600 to-blue-500 px-2 pt-2 pb-3 space-y-1">
          <Link
            to="/"
            onClick={() => setMobileMenuOpen(false)}
            className="block px-3 py-2 rounded-md text-base font-medium hover:bg-indigo-700"
          >
            Home
          </Link>
          <Link
            to="/book-appointment"
            onClick={() => setMobileMenuOpen(false)}
            className="block px-3 py-2 rounded-md text-base font-medium hover:bg-indigo-700"
          >
            Book Appointment
          </Link>
          {isLoggedIn ? (
            <>
              <Link
                to="/profile"
                onClick={() => setMobileMenuOpen(false)}
                className="block px-3 py-2 rounded-md text-base font-medium hover:bg-indigo-700"
              >
                View Profile
              </Link>
              <Link
                to="/change-password"
                onClick={() => setMobileMenuOpen(false)}
                className="block px-3 py-2 rounded-md text-base font-medium hover:bg-indigo-700"
              >
                Change Password
              </Link>
              <button
                onClick={() => {
                  handleLogout();
                  setMobileMenuOpen(false);
                }}
                className="block w-full text-left px-3 py-2 rounded-md text-base font-medium hover:bg-indigo-700"
              >
                Logout
              </button>
            </>
          ) : (
            <Link
              to="/login"
              onClick={() => setMobileMenuOpen(false)}
              className="block px-3 py-2 rounded-md text-base font-medium hover:bg-indigo-700"
            >
              Login
            </Link>
          )}
        </div>
      )}
    </nav>
  );
};

export default Header;
