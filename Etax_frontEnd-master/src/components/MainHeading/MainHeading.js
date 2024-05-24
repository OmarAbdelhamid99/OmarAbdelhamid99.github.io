import React from "react";
import './MainHeading.css'
const MainHeading = (props) => {
  return (
    <>
      <div className="main-heading mb-5">
        <h4 className="heading"> {props.heading}</h4>
        <p>{props.contentHeading}</p>
      </div>
    </>
  );
};

export default MainHeading;
