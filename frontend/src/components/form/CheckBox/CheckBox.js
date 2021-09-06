import React, { useEffect } from "react";
import PropTypes from "prop-types";
import Label from "../Label/Label";
import styles from "./CheckBox.module.css";
import useFormContext from "../../../hooks/useFormContext";

const CheckBox = ({ name, label, required, ...props }) => {
  const { value, onChange, register, unRegister } = useFormContext();

  useEffect(() => {
    register(name);

    return () => {
      unRegister(name);
    };
  }, []);

  return (
    <Label className={styles.checkbox} required={required}>
      <input
        type="checkbox"
        name={name}
        value={value[name]}
        onChange={onChange}
        required={required}
        {...props}
      />
      {label}
    </Label>
  );
};

CheckBox.propTypes = {
  name: PropTypes.string,
  label: PropTypes.string,
  required: PropTypes.bool,
};

CheckBox.defaultProps = {
  label: "",
  required: false,
};

export default CheckBox;
