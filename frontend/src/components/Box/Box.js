import React from "react";
import PropTypes from "prop-types";

import styles from "./Box.module.css";

const Box = ({ children }) => {
  return <div className={styles.box}>{children}</div>;
};

export default Box;

Box.propTypes = {
  children: PropTypes.node.isRequired,
};
