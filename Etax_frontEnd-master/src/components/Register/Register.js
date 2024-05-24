import React, { useState } from "react";
import "./Register.css";
import logo from "../../assets/logo/eTaxNewLogo.svg";
import { Link } from "react-router-dom";
import MediaQuery from "react-responsive";

const Register = () => {
  const [formData, setFormData] = useState({
    name: "",
    email: "",
    password: "",
    password_confirmation: "",
    phonenumber: "",
  });

  const [validationErrors, setValidationErrors] = useState({
    name: "",
    email: "",
    password: "",
    phonenumber: "",
  });

  const handleInputChange = (event) => {
    const { name, value } = event.target;
    setFormData((prevData) => ({ ...prevData, [name]: value }));
  };

  const handleRegister = async () => {
    // Reset validation errors
    setValidationErrors({
      name: "",
      email: "",
      password: "",
      phonenumber: "",
    });

    // Validate input fields
    let isValid = true;
    const errors = { ...validationErrors };

    if (formData.name.trim() === "") {
      errors.name = "اسم المستخدم مطلوب";
      isValid = false;
    }

    if (
      !/^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/.test(formData.email)
    ) {
      errors.email = "البريد الإلكتروني غير صالح";
      isValid = false;
    }

    if (formData.password.length < 6) {
      errors.password = "كلمة المرور يجب أن تكون على الأقل 6 أحرف";
      isValid = false;
    }

    if (!/^\d{10}$/.test(formData.phonenumber)) {
      errors.phonenumber = "رقم الهاتف غير صالح";
      isValid = false;
    }

    if (!isValid) {
      setValidationErrors(errors);
      return;
    }

    try {
      const response = await fetch("http://127.0.0.1:8000/api/register", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(formData),
      });

      if (response.ok) {
        // User registered successfully
        console.log("User registered successfully");
        window.location.href = "/login"; // Redirect to login page
      } else {
        // Handle registration error
        console.error("Error registering user");
        alert("An error occurred during registration.........."); // Show error alert
        console.log(response.ok);
      }
    } catch (error) {
      console.error("An error occurred", error);
      alert("An error occurred during registration."); // Show error alert
    }
  };

  return (
    <div className="register">
      <div className="d-flex">
        <div className="col-lg-4 col-md-8 col-sm-12">
          <div className="content-section">
            <div className="logo text-center">
              <img src={logo} alt="Logo" />
            </div>
            <h2>إنشاء حساب جديد</h2>
            <p>لديك حساب على المنظومة؟</p>
            <Link to="/login" className="create-account">
              تسجيل الدخول
            </Link>

            <input
              type="text"
              name="name"
              placeholder="اسم المستخدم"
              value={formData.name}
              onChange={handleInputChange}
            />
            {validationErrors.name && (
              <p className="error-message">{validationErrors.name}</p>
            )}

            <input
              type="email"
              name="email"
              placeholder="البريد الإلكتروني"
              value={formData.email}
              onChange={handleInputChange}
            />
            {validationErrors.email && (
              <p className="error-message">{validationErrors.email}</p>
            )}

            <input
              type="password"
              name="password"
              placeholder="كلمة المرور"
              value={formData.password}
              onChange={handleInputChange}
            />
            {validationErrors.password && (
              <p className="error-message">{validationErrors.password}</p>
            )}

            <input
              type="password"
              name="password_confirmation"
              placeholder=" تأكيد كلمة المرور "
              value={formData.password_confirmation}
              onChange={handleInputChange}
            />

            <input
              type="tel"
              name="phonenumber"
              placeholder="رقم الهاتف"
              value={formData.phonenumber}
              onChange={handleInputChange}
            />
            {validationErrors.phonenumber && (
              <p className="error-message">{validationErrors.phonenumber}</p>
            )}

            <button className="submit-button" onClick={handleRegister}>
              إنشاء حساب جديد
            </button>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Register;
