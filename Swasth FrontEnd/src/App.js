import './App.css';
import Header from './Components/Header';
import { BrowserRouter as Router, Routes, Route, useLocation } from "react-router-dom";
import LoginSelection from './Components/LoginSelection';
import PatientLogin from './Components/PatientLogin';
import PatientSignup from './Components/PatientSignup';
import DoctorLogin from './Components/DoctorLogin';
import HospitalLogin from './Components/HospitalLogin';
import HospitalSignup from './Components/HospitalSignup';
import BookMyAppointment from './Components/BookMyAppointment';
import PatientHome from './Components/PatientHome';
import DoctorHome from './Components/DoctorHome';
import HospitalHome from './Components/HospitalHome';
import CommonHome from './Components/CommonHome';
import BookAppointment from './Components/BookAppointment';
import ProfilePage from './Components/ProfilePage';
import ChangePassword from './Components/ChangePassword';


const AppContent = () => {
  const location = useLocation();
  
  // Only show the CommonHome component if the current path is the landing page
  const showCommonHome = location.pathname === "/" || location.pathname === "/home";

  return (
    <>
      <Header/>
        <Routes>
          <Route path="/login" element={<LoginSelection />} />          
          {/* This above will open the login selection page where a user can select from whatever  role he/she wants to login with*/}

          <Route path="/login/patient" element={<PatientLogin />} /> 
           {/* This route will open login form for Patient */}

          <Route path="/signup/patient" element={<PatientSignup />}/>
          {/* This route will open the patient sign up page the flow is login-->loginSelection page-->login page-->click on signup button-->PatientSignup page */}

          <Route path="/login/doctor" element={<DoctorLogin />}/>
          {/* The above route is to handle doctor login and there is no signup button as hospital will be able to add doctors */}

          <Route path="/login/hospital" element={<HospitalLogin />}/>
          {/* This route will handle hospital login */}

          <Route path="/hospital/signup" element={<HospitalSignup />}/>
          {/* This will handle hospital signup  */}
          
          <Route path="/book-appointment" element={<BookMyAppointment />}/>
          {/* This route will show the list of doctors and each block is clickable  BookMyAppointment page */}

          {/* <Route path="/doctor/:doctorId" element={<DoctorDetails />} /> */}
          {/* This end point is in BookMyAppointment page and will take the doctors id and render specific doctor details page (It is causing a problem not able to understand)*/}

          <Route path="/doctor/:doctorId" element={<BookAppointment />} />          
          {/* This end point is in DoctorDetails page and will render the BookAppointment page which contains the form to add time and date to book appointment will be used with appointments pojo and store the data */}

          <Route path="/patient/home" element={<PatientHome />}/>
          {/* This route is triggered when user logs in as a patient */}

          <Route path="/doctor-home" element={<DoctorHome />}/>
          {/* This route is when user logs in as a doctor  */}

          <Route path="/hospital/home" element={<HospitalHome/>}/>
          {/* This route is when user logs in as a hospital  */}
          
          <Route path="/profile" element={<ProfilePage/>}/>
          {/* This route is when patient logs in and is trying to look at his profile*/}

          
          <Route path="/change-password" element={<ChangePassword/>}/>
          {/* This route is when patient Wants to change his password*/}          
        </Routes>     
        {showCommonHome && <CommonHome />}

    </>
  );
};

function App() {
  return (
      <AppContent />    
  );
}

export default App;
