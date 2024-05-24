import React from 'react';
import './ButtonHome.css';
const ButtonHome = (props) => {
  return (
    <>
      <button className="btn btn-primary bg-button btn-log" type="submit">
        <a className="text-white" aria-current="page" href={props.HomeOrNot}>
          {props.title}
        </a>
      </button>
    </>
  );
}

export default ButtonHome