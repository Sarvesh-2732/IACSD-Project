import React from "react";
import { useNavigate } from "react-router-dom";
import { motion } from "framer-motion";

// Common motion variants to avoid repetition
const fadeInFromLeft = {
  initial: { opacity: 0, x: -50 },
  animate: { opacity: 1, x: 0 },
  transition: { duration: 0.5 },
};

const fadeInFromRight = {
  initial: { opacity: 0, x: 50 },
  animate: { opacity: 1, x: 0 },
  transition: { duration: 0.5 },
};

const fadeInUp = {
  initial: { opacity: 0, scale: 0.8, y: 50 },
  animate: { opacity: 1, scale: 1, y: 0 },
  transition: { duration: 0.5 },
};

const CommonHome = () => {
  const navigate = useNavigate();

  return (
    <div className="min-h-screen flex flex-col items-center justify-center bg-gradient-to-r from-blue-100 to-green-100 p-6">
      {/* Section 1: About Swasth */}
      <div className="mt-16 w-full max-w-5xl">
        <div className="grid md:grid-cols-2 gap-8 items-center">
          <motion.img
            src="https://americanonlinebenefits.com/wp-content/uploads/2019/11/healthcare-768x512.png"
            alt="Healthcare"
            className="rounded-2xl shadow-xl"
            {...fadeInFromLeft}
          />
          <motion.div {...fadeInFromRight}>
            <h2 className="text-3xl font-bold text-gray-800 mb-4">About Swasth</h2>
            <p className="text-gray-700 text-lg">
              Swasth is a comprehensive healthcare management platform designed to simplify and streamline the interaction between patients, doctors, and hospitals. With our innovative system, you can easily book appointments, manage your medical records digitally, and experience a hassle-free healthcare journey. Our goal is to empower you with the tools to take control of your health.
            </p>
          </motion.div>
        </div>
      </div>

      {/* Section 2: Our Vision */}
      <div className="mt-16 w-full max-w-5xl">
        <div className="grid md:grid-cols-2 gap-8 items-center">
          <motion.div {...fadeInFromRight}>
            <h2 className="text-3xl font-bold text-gray-800 mb-4">Our Vision</h2>
            <p className="text-gray-700 text-lg">
              At Swasth, we envision a future where healthcare is accessible, efficient, and stress-free for everyone. By leveraging cutting-edge technology, we strive to bridge the gap between patients and healthcare providers, ensuring that quality care is just a click away. Join us in revolutionizing the healthcare experience and making wellness a priority.
            </p>
          </motion.div>
          <motion.img
            src="https://www.sme.org/globalassets/sme.org/technologies/articles/2022/04---april/transforming-healthcare_768x432.jpg"
            alt="Healthcare Vision"
            className="rounded-2xl shadow-xl"
            {...fadeInFromLeft}
          />
        </div>
      </div>

      {/* New Section: Our Features (Different Style) */}
      <div className="mt-16 w-full bg-gradient-to-r from-indigo-700 to-purple-700 p-10 rounded-2xl shadow-2xl max-w-5xl">
        <div className="grid md:grid-cols-3 gap-8 text-white">
          <motion.div
            initial={{ opacity: 0, y: 50 }}
            animate={{ opacity: 1, y: 0 }}
            transition={{ duration: 0.5, delay: 0.2 }}
            className="flex flex-col items-center text-center"
          >
            <i className="fas fa-calendar-check text-5xl mb-4"></i>
            <h3 className="text-2xl font-bold mb-2">Easy Appointments</h3>
            <p className="text-lg">
              Schedule appointments with just a few clicks and get timely reminders.
            </p>
          </motion.div>
          <motion.div
            initial={{ opacity: 0, y: 50 }}
            animate={{ opacity: 1, y: 0 }}
            transition={{ duration: 0.5, delay: 0.4 }}
            className="flex flex-col items-center text-center"
          >
            <i className="fas fa-file-medical-alt text-5xl mb-4"></i>
            <h3 className="text-2xl font-bold mb-2">Digital Records</h3>
            <p className="text-lg">
              Securely store and access your medical records anytime, anywhere.
            </p>
          </motion.div>
          <motion.div
            initial={{ opacity: 0, y: 50 }}
            animate={{ opacity: 1, y: 0 }}
            transition={{ duration: 0.5, delay: 0.6 }}
            className="flex flex-col items-center text-center"
          >
            <i className="fas fa-user-md text-5xl mb-4"></i>
            <h3 className="text-2xl font-bold mb-2">Expert Care</h3>
            <p className="text-lg">
              Connect with top-notch doctors and receive personalized care.
            </p>
          </motion.div>
        </div>
      </div>

      {/* Main Landing Card */}
      <motion.div {...fadeInUp} className="text-center bg-white p-10 rounded-2xl shadow-2xl max-w-2xl mt-16">
        <h1 className="text-4xl font-bold text-gray-800 mb-4">Welcome to Swasth</h1>
        <p className="text-gray-600 text-lg mb-6">
          Your one-stop solution for managing healthcare efficiently. Book appointments, manage medical records, and more!
        </p>
        <div className="space-y-4">
          <button
            onClick={() => navigate("/login/patient")}
            className="w-full py-3 bg-blue-500 text-white rounded-lg font-semibold transition hover:scale-105 hover:bg-blue-600"
          >
            Patient Login
          </button>
          <button
            onClick={() => navigate("/login/doctor")}
            className="w-full py-3 bg-green-500 text-white rounded-lg font-semibold transition hover:scale-105 hover:bg-green-600"
          >
            Doctor Login
          </button>
          <button
            onClick={() => navigate("/login/hospital")}
            className="w-full py-3 bg-purple-500 text-white rounded-lg font-semibold transition hover:scale-105 hover:bg-purple-600"
          >
            Hospital Login
          </button>
        </div>
        <p className="mt-6 text-gray-700">New here? Sign up to get started!</p>
        <div className="flex justify-center gap-4 mt-4">
          <button
            onClick={() => navigate("/signup/patient")}
            className="text-blue-500 hover:underline font-semibold"
          >
            Patient Signup
          </button>
          <button
            onClick={() => navigate("/hospital/signup")}
            className="text-purple-500 hover:underline font-semibold"
          >
            Hospital Signup
          </button>
        </div>
      </motion.div>
    </div>
  );
};

export default CommonHome;
