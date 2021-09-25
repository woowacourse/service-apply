import React from "react";
import PropTypes from "prop-types";
import Field from "../Field/Field";
import Label from "../Label/Label";
import Description from "../Description/Description";
import TextInput from "../TextInput/TextInput";
import styles from "./TextInputField.module.css";

const TextInputField = ({
  required,
  label,
  value,
  description,
  maxLength,
  errorMessage,
  ...props
}) => {
  return (
    <>
      <Field className={styles["text-field"]}>
        <Label required={required}>{label}</Label>
        {description && <Description>{description}</Description>}
        <TextInput
          required={required}
          value={value}
          maxLength={maxLength}
          {...props}
        />
      </Field>
      <p className={styles["rule-field"]}>{errorMessage}</p>
    </>
  );
};

TextInputField.propTypes = {
  label: PropTypes.string,
  value: PropTypes.string,
  required: PropTypes.bool,
  description: PropTypes.node,
  maxLength: PropTypes.number,
  errorMessage: PropTypes.string,
};

TextInputField.defaultProps = {
  label: "",
  value: "",
  required: false,
  description: "",
  maxLength: undefined,
};

export default TextInputField;
