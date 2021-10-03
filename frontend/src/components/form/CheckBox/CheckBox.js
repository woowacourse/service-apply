import React, { useEffect } from "react";
import PropTypes from "prop-types";
import Label from "../../@common/Label/Label";
import styles from "./CheckBox.module.css";
import useFormContext from "../../../hooks/useFormContext";

const CheckBox = ({ name, label, required, ...props }) => {
  const { value, handleChange, register, unRegister } = useFormContext();

  useEffect(() => {
    register(name, "", required);

    return () => {
      unRegister(name);
    };
  }, []);

  return (
    <Label className={styles.checkbox}>
      <input
        type="checkbox"
        name={name}
        value={value[name]}
        onChange={handleChange}
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
