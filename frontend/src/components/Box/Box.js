import React from "react";
import PropTypes from "prop-types";

import "./Box.css";

const Box = ({ children }) => {
  return <div className="box">{children}</div>;
};

export default Box;

Box.propTypes = {
  children: PropTypes.node.isRequired,
};
