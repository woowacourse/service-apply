import PropTypes from "prop-types";
import React from "react";
import Label from "../../@common/Label/Label";
import styles from "./CheckBox.module.css";

const CheckBox = ({ label, name, value, onChange, required, ...props }) => {
  return (
    <Label className={styles.checkbox}>
      <input
        type="checkbox"
        name={name}
        checked={value}
        onChange={onChange}
        required={required}
        {...props}
      />
      {label}
    </Label>
  );
};

CheckBox.propTypes = {
  value: PropTypes.bool.isRequired,
  onChange: PropTypes.func.isRequired,
  name: PropTypes.string,
  label: PropTypes.string,
  required: PropTypes.bool,
};

CheckBox.defaultProps = {
  label: "",
  required: false,
};

export default CheckBox;
