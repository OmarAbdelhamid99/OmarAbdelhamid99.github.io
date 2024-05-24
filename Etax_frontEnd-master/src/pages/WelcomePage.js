import React from "react";
import Home from "../components/Home/Home";
import Services from "../components/Services/Services";
import About from "../components/About/About";
import Footer from "../components/Footer/Footer";
import NavBarHome from "../components/NavBarHome/NavBarHome";

const WelcomePage = () => {
  return (
    <>
      <NavBarHome />
      <Home />
      <Services />
      <About />
      <Footer />
    </>
  );
};

export default WelcomePage;
