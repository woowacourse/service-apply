import React from "react";
import PropTypes from "prop-types";
import Field from "../Field/Field";
import Label from "../Label/Label";
import Description from "../Description/Description";
import TextInput from "../TextInput/TextInput";
import styles from "./TextField.module.css";

const TextField = ({
  required,
  label,
  value,
  description,
  maxLength,
  ...props
}) => {
  return (
    <Field className={styles["text-field"]}>
      <label className={styles["text-field"]}>
        <div>
          <Label required={required}>{label}</Label>
        </div>
        {description && <Description>{description}</Description>}
        {maxLength && maxLength > 0 && (
          <div className={styles["length-limit"]}>
            {value.length} / {maxLength}
          </div>
        )}
        <TextInput
          required={required}
          value={value}
          maxLength={maxLength}
          {...props}
        />
      </label>
    </Field>
  );
};

TextField.propTypes = {
  label: PropTypes.string,
  value: PropTypes.string,
  required: PropTypes.bool,
  description: PropTypes.node,
  maxLength: PropTypes.number,
};

TextField.defaultProps = {
  label: "",
  value: "",
  required: false,
  description: "",
  maxLength: undefined,
};

export default TextField;
