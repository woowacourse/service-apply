import React from "react";
import PropTypes from "prop-types";
import Field from "../Field/Field";
import Label from "../Label/Label";
import Description from "../Description/Description";
import styles from "./TextareaField.module.css";
import Textarea from "../Textarea/Textarea";

const TextareaField = ({
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
        {maxLength && maxLength > 0 && (
          <div className={styles["length-limit"]}>
            {value.length} / {maxLength}
          </div>
        )}
        <Textarea
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

TextareaField.propTypes = {
  label: PropTypes.string,
  value: PropTypes.string,
  required: PropTypes.bool,
  description: PropTypes.node,
  maxLength: PropTypes.number,
  errorMessage: PropTypes.string,
};

TextareaField.defaultProps = {
  label: "",
  value: "",
  required: false,
  description: "",
  maxLength: undefined,
};

export default TextareaField;
