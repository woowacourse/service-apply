import PropTypes from "prop-types";
import React from "react";
import Label from "../../@common/Label/Label";
import styles from "./CheckBox.module.css";

const CheckBox = ({ label, name, checked, onChange, required, ...props }) => {
  return (
    <Label className={styles.checkbox}>
      <input
        type="checkbox"
        name={name}
        checked={checked}
        onChange={onChange}
        required={required}
        {...props}
      />
      <div className={styles["custom-checkbox"]} />
      {label}
    </Label>
  );
};

CheckBox.propTypes = {
  checked: PropTypes.bool.isRequired,
  onChange: PropTypes.func.isRequired,
  name: PropTypes.string,
  label: PropTypes.string,
  required: PropTypes.bool,
};

CheckBox.defaultProps = {
  label: "",
  required: false,
  checked: false,
};

export default CheckBox;
