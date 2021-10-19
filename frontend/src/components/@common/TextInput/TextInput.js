import React from "react";
import PropTypes from "prop-types";
import classNames from "classnames";
import styles from "./TextInput.module.css";

const TextInput = ({ className, type, readOnly, value, maxLength, onChange, ...props }) => {
  const handleChange = (event) => {
    if (maxLength !== undefined && event.target.value.length > maxLength) return;

    onChange(event);
  };

  return (
    <input
      type={type}
      value={value}
      maxLength={maxLength}
      className={classNames(styles["text-input"], className)}
      readOnly={readOnly}
      onChange={handleChange}
      {...props}
    />
  );
};

TextInput.propTypes = {
  className: PropTypes.string,
  type: PropTypes.oneOf(["text", "email", "password", "tel", "number", "url"]),
  readOnly: PropTypes.bool,
  value: PropTypes.string,
  maxLength: PropTypes.number,
};

TextInput.defaultProps = {
  className: "",
  type: "text",
  readOnly: false,
  value: "",
};

export default TextInput;
