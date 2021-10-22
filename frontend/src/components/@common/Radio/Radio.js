import React from "react";
import PropTypes from "prop-types";
import Label from "../Label/Label";
import styles from "./Radio.module.css";

const Radio = ({ label, required, ...props }) => {
  return (
    <Label className={styles["radio-label"]}>
      <input type="radio" required={required} {...props} />
      <div className={styles["custom-radio"]} />
      {label}
    </Label>
  );
};

Radio.propTypes = {
  label: PropTypes.string,
  required: PropTypes.bool,
};

Radio.defaultProps = {
  label: "",
  required: false,
};

export default Radio;
