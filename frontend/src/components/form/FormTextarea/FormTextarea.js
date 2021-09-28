import React, { useEffect } from "react";
import PropTypes from "prop-types";
import Label from "../../@common/Label/Label";
import Description from "../../@common/Description/Description";
import styles from "./FormTextarea.module.css";
import Textarea from "../../@common/Textarea/Textarea";
import useFormContext from "../../../hooks/useFormContext";

const FormTextarea = ({
  required,
  label,
  name,
  initialValue,
  description,
  maxLength,
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
        {maxLength && maxLength > 0 && (
          <div className={styles["length-limit"]}>
            {value.length} / {maxLength}
          </div>
        )}
        <Textarea
          required={required}
          value={value[name]}
          maxLength={maxLength}
          onChange={handleChange}
          {...props}
        />
      </div>
      <p className={styles["rule-field"]}>{errorMessage[name]}</p>
    </>
  );
};

FormTextarea.propTypes = {
  label: PropTypes.string,
  required: PropTypes.bool,
  description: PropTypes.node,
  maxLength: PropTypes.number,
  initialValue: PropTypes.string,
  name: PropTypes.string.isRequired,
};

FormTextarea.defaultProps = {
  label: "",
  value: "",
  required: false,
  description: "",
  maxLength: undefined,
};

export default FormTextarea;
