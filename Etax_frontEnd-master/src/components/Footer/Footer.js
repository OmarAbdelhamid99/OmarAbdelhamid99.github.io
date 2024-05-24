import React from "react";
import logoFooter from "../../assets/footer/eTaxNewLogo.svg";
import arrow from "../../assets/footer/arrow-2.svg";
import './Footer.css'
const Footer = () => {
  return (
    <div className="footer p-5">
      <div className="containerr">
        <div className="row justify-content-between">
          <div className="col-lg-8">
            <div className="content-footer">
              <p>
                من خلال هذه المنظومة يمكنك الاستعلام و التحقق من الشئون الضريبية
                لدى الجهات الأخرى و التحقق من مستند الرواتب الخاص بالمواطن
                للتأكد من صحتة
              </p>
            </div>
            <div className="content-footer">
              <ul className="list-footer d-flex">
                <li>
                  <a href="/">الرئيسية</a>
                </li>
                <li>
                  <a href="/">خدماتنا</a>
                </li>
                <li>
                  <a href="/">من نحن</a>
                </li>
              </ul>
            </div>
          </div>
          <div className="col-lg-4">
            <div className="footer-logo-img">
              <img src={logoFooter} className="col-lg-4" alt="" />
            </div>
          </div>
        </div>
      </div>
      {/* <button className="footer-arrow-img">
        <img src={arrow} className="" alt="" />
      </button> */}
    </div>
  );
};

export default Footer;
