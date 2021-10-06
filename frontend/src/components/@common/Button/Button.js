import classNames from "classnames";
import PropTypes from "prop-types";
import React from "react";
import styles from "./Button.module.css";

const Button = ({ children, type, variant, cancel, className, ...props }) => {
  return (
    <button
      className={classNames(className, styles[variant], styles.button, {
        [styles.cancel]: cancel,
      })}
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
  variant: PropTypes.oneOf(["contained", "outlined"]),
  cancel: PropTypes.bool,
  className: PropTypes.string,
};

Button.defaultProps = {
  variant: "contained",
  cancel: false,
};

export default Button;
