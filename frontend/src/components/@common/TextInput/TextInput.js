import React from "react";
import PropTypes from "prop-types";
import classNames from "classnames";
import styles from "./TextInput.module.css";

const TextInput = ({ className, type, readOnly, value, maxLength, onChange, ...props }) => {
  const handleChange = (event) => {
    if (event.nativeEvent.isComposing) {
      event.target.value = event.target.value.replaceAll(" ", "");
    }

    if (maxLength !== undefined && event.target.value.length > maxLength) {
      return;
    }

    onChange(event);
  };

  const handleWhiteSpace = (event) => {
    if (event.key === " ") {
      event.preventDefault();
    }
  };

  return (
    <input
      type={type}
      value={value}
      className={classNames(styles["text-input"], className)}
      readOnly={readOnly}
      onKeyDown={handleWhiteSpace}
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
