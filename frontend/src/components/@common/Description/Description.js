import React from "react";
import PropTypes from "prop-types";
import styles from "./Description.module.css";

const Description = ({ children }) => {
  return <div className={styles.description}>{children}</div>;
};

Description.propTypes = {
  children: PropTypes.node.isRequired,
};

export default Description;
