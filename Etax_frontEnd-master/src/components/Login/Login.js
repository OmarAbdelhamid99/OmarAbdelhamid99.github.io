import React from "react";
import "./Login.css";
import logo from "../../assets/logo/eTaxNewLogo.svg";
import { Link } from "react-router-dom";
import InputLogin from "../InputLogin/InputLogin";

const Login = () => {
  return (
    <>
      <div className="login">
        <div className="d-flex">
          <div className="col-lg-4">
            <div className="content-section">
              <div className="logo text-center">
                <img src={logo} alt="Logo" />
              </div>
              <h2>تسجيل الدخول</h2>
              <p>لا املك بريد إلكترونى على هذه المنظومة</p>
              <Link to="/register" class="create-account">
                إنشاء حساب
              </Link>
              <InputLogin type="text" placeholder="البريد الإلكتروني" />
              <InputLogin type="password" placeholder="كلمة المرور" />
              <button className="submit-button">تسجيل الدخول</button>
            </div>
          </div>
        </div>
      </div>
    </>
  );
};

export default Login;
