import React from "react";
import logo from "../../assets/logo/eTaxNewLogo.svg";
import ButtonHome from "../ButtonHome/ButtonHome";
const NavBarHome = () => {
  return (
    <nav className="navbar navbar-expand-lg">
      <div className="row justify-content-center">
        <div className="col-2">
          <img className="logo" src={logo} alt="" />
        </div>
        <div className="col-lg-10">
          <ul className="navbar-nav ms-auto mb-lg-0">
            <li className="nav-item ">
              <a className="nav-link active" aria-current="page" href="/">
                الرئيسية
              </a>
            </li>
            <li className="nav-item">
              <a className="nav-link" href="/">
                خدماتنا
              </a>
            </li>
            <li className="nav-item">
              <a className="nav-link" href="/">
                من نحن
              </a>
            </li>
            <li className="nav-item">
              <a className="nav-link" href="/">
                مرحبا , يوسف حسين
              </a>
            </li>
            <form>
              <ButtonHome title={"تسجيل الخروج"} HomeOrNot={"/"} />
            </form>
          </ul>
        </div>
      </div>
    </nav>
  );
};

export default NavBarHome;
