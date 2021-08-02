import React from "react";
import PropTypes from "prop-types";
import styles from "./Button.module.css";

const Button = ({ children, type, cancel, ...props }) => {
  return (
    <button
      className={cancel ? styles["button cancel"] : styles.button}
      type={type}
      {...props}
    >
      {children}
    </button>
  );
};

Button.propTypes = {
  children: PropTypes.node.isRequired,
  type: PropTypes.oneOf(["button", "submit", "reset"]),
  cancel: PropTypes.bool,
};

Button.defaultProps = {
  type: "button",
  cancel: false,
};

export default Button;
