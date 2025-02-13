import React from 'react';
import { motion } from 'framer-motion';

const DoctorsList = ({ doctors, onRemoveDoctor = () => {} }) => {
  if (!Array.isArray(doctors)) {
    // console.error("DoctorsList received an invalid doctors array:", doctors);
    return <p className="text-gray-500 text-center py-4">No doctors available</p>;
  }

  return (
    <div className="bg-white p-8 rounded-2xl shadow-2xl">
      <h3 className="text-2xl font-bold mb-6 text-gray-800 text-center">
        Our Esteemed Doctors
      </h3>
      <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 gap-6">
        {doctors.length === 0 ? (
          <p className="text-gray-500 col-span-full text-center py-4">No doctors found</p>
        ) : (
          doctors.map((doctor) => {
            const doctorId = doctor.doctorsId; // Use the correct property from your API
            // console.log("Rendering doctor with id:", doctorId, doctor);
            return (
              <div
                key={doctorId}
                className="bg-gradient-to-br from-blue-50 to-blue-100 p-6 rounded-xl shadow-lg flex flex-col justify-between"
              >
                <div>
                  <h4 className="text-xl font-semibold text-gray-800">
                    Dr. {doctor.firstName} {doctor.lastName}
                  </h4>
                  <p className="text-md text-gray-600 mb-2">{doctor.expertiseField}</p>
                </div>
                <div className="mt-4 flex justify-end">
                  <button
                    onClick={() => onRemoveDoctor(doctorId)}
                    className="px-4 py-2 bg-red-500 hover:bg-red-600 text-white font-semibold rounded-lg transition duration-300"
                  >
                    Expel
                  </button>
                </div>
              </div>
            );
          })
        )}
      </div>
    </div>
  );
};

export default DoctorsList;
