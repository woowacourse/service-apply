import React, { useEffect } from "react";
import PropTypes from "prop-types";
import useFormContext from "../../../hooks/useFormContext";
import MessageTextInput from "../../@common/MessageTextInput/MessageTextInput";
import styles from "./FormInput.module.css";

const FormInput = ({
  label,
  description,
  initialValue,
  name,
  maxLength,
  required,
  rightButton,
  ...props
}) => {
  const { value, errorMessage, handleChange, register, unRegister } =
    useFormContext();

  useEffect(() => {
    register(name, initialValue, required);

    return () => {
      unRegister(name);
    };
  }, [name, initialValue]);

  return (
    <MessageTextInput
      value={value[name]}
      onChange={handleChange}
      errorMessage={errorMessage[name]}
      label={label}
      description={description}
      initialValue={initialValue}
      name={name}
      maxLength={maxLength}
      required={required}
      rightButton={rightButton}
      className={styles["form-input"]}
      {...props}
    />
  );
};

FormInput.propTypes = {
  label: PropTypes.string,
  initialValue: PropTypes.string,
  name: PropTypes.string.isRequired,
  required: PropTypes.bool,
  description: PropTypes.node,
  maxLength: PropTypes.number,
  errorMessage: PropTypes.string,
  rightButton: PropTypes.node,
};

FormInput.defaultProps = {
  label: "",
  initialValue: "",
  required: false,
  description: "",
};

export default FormInput;
