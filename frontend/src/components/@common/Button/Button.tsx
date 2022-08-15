import classNames from "classnames";
import PropTypes from "prop-types";
import React from "react";
import styles from "./Button.module.css";

export const BUTTON_VARIANT = {
  CONTAINED: "contained",
  OUTLINED: "outlined",
};

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
  variant: PropTypes.oneOf(Object.values(BUTTON_VARIANT)),
  cancel: PropTypes.bool,
  className: PropTypes.string,
};

Button.defaultProps = {
  variant: BUTTON_VARIANT.CONTAINED,
  cancel: false,
};

export default Button;
