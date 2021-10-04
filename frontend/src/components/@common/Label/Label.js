import React from "react";
import PropTypes from "prop-types";
import classNames from "classnames";
import styles from "./Label.module.css";

const Label = ({ children, className, required }) => {
  return (
    <label
      className={classNames(styles.label, className, {
        [styles.required]: required,
      })}
    >
      {children}
    </label>
  );
};

Label.propTypes = {
  children: PropTypes.node.isRequired,
  className: PropTypes.string,
  required: PropTypes.bool,
};

Label.defaultProps = {
  className: "",
  required: false,
};

export default Label;
