import React from "react";
import PropTypes from "prop-types";
import classNames from "classnames";

import styles from "./Box.module.css";

const Box = ({ children, className }) => {
  return <div className={classNames(styles.box, className)}>{children}</div>;
};

export default Box;

Box.propTypes = {
  children: PropTypes.node.isRequired,
};
