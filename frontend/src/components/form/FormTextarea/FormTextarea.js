import React, { useEffect } from "react";
import PropTypes from "prop-types";

import useFormContext from "../../../hooks/useFormContext";
import MessageTextarea from "../../@common/MessageTextarea/MessageTextarea";

const FormTextarea = ({
  label,
  name,
  initialValue,
  description,
  maxLength,
  required,
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
    <MessageTextarea
      name={name}
      label={label}
      value={value[name]}
      onChange={handleChange}
      errorMessage={errorMessage[name]}
      description={description}
      maxLength={maxLength}
      required={required}
      {...props}
    />
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
  initialValue: "",
  required: false,
  description: "",
};

export default FormTextarea;
