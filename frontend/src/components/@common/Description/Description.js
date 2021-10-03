import React from "react";
import PropTypes from "prop-types";
import classNames from "classnames";
import styles from "./Description.module.css";

const Description = ({ children, className }) => {
  return (
    <div className={classNames(styles.description, className)}>{children}</div>
  );
};

Description.propTypes = {
  children: PropTypes.node.isRequired,
};

export default Description;
