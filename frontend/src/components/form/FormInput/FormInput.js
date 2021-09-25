import React, { useEffect } from "react";
import PropTypes from "prop-types";
import useFormContext from "../../../hooks/useFormContext";
import Label from "../../@common/Label/Label";
import Description from "../../@common/Description/Description";
import TextInput from "../../@common/TextInput/TextInput";
import styles from "./FormInput.module.css";

const FormInput = ({
  label,
  description,
  initialValue,
  name,
  maxLength,
  required,
  ...props
}) => {
  const { value, errorMessage, handleChange, register, unRegister } =
    useFormContext();

  useEffect(() => {
    register(name, initialValue);

    return () => {
      unRegister(name);
    };
  }, [name, initialValue]);

  return (
    <>
      <div className={styles["text-field"]}>
        <Label required={required}>{label}</Label>
        {description && <Description>{description}</Description>}
        <TextInput
          required={required}
          value={value}
          name={name}
          errorMessage={errorMessage[name]}
          maxLength={maxLength}
          onChange={handleChange}
          {...props}
        />
      </div>
      <p className={styles["rule-field"]}>{errorMessage}</p>
    </>
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
};

FormInput.defaultProps = {
  label: "",
  initialValue: "",
  required: false,
  description: "",
};

export default FormInput;
