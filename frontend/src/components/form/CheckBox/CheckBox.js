import React from "react";
import PropTypes from "prop-types";
import Label from "../Label/Label";
import styles from "./CheckBox.module.css";

const CheckBox = ({ label, required, ...props }) => {
  return (
    <Label className={styles.checkbox} required={required}>
      <input type="checkbox" required={required} {...props} />
      {label}
    </Label>
  );
};

CheckBox.propTypes = {
  label: PropTypes.string,
  required: PropTypes.bool,
};

CheckBox.defaultProps = {
  label: "",
  required: false,
};

export default CheckBox;
