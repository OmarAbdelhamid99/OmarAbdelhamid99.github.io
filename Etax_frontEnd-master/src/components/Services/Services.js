import React, { useEffect, useState } from "react";
import MainHeading from "../MainHeading/MainHeading";
import axios from "axios";
import "./Services.css";
import { Link } from "react-router-dom";
const Services = () => {
  const [services, setServices] = useState([]);

  useEffect(() => {
    async function fetchData() {
      try {
        const response = await axios.get("/data-json/data.json");
        setServices(response.data.services);
      } catch (error) {
        console.error("Error fetching data:", error);
      }
    }

    fetchData();
  }, []);

  const listservices = services.map((ser) => {
    return (
      <Link
        className="col-lg-4 col-md-6 col-sm-12 p-3"
        to={ser.routeService}
        key={ser.id}
      >
        <div className="card">
          <h5 className="tiltle_services_card">{ser.tiltle_services_card}</h5>
          <p className="body_services_card">{ser.bode_services_card}</p>
        </div>
      </Link>
    );
  });
  return (
    <>
      <div className="services text-center m-tb-120" id="service">
        <div className="containerr">
          <MainHeading
            heading={"الخدمات"}
            contentHeading={"كل الخدمات الضريبية في مكان واحد"}
          />
          <div className="cards-services">
            <div className="row">{listservices}</div>
          </div>
        </div>
      </div>
    </>
  );
};

export default Services;
