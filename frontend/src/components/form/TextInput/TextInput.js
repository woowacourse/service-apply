import React from "react";
import PropTypes from "prop-types";
import "./TextInput.css";
import classNames from "classnames";

const TextInput = ({
  className,
  type,
  readOnly,
  value,
  maxLength,
  ...props
}) => {
  if (type === "textarea") {
    return (
      <textarea
        value={value}
        maxLength={maxLength}
        className={classNames("text-input", className)}
        readOnly={readOnly}
        {...props}
      />
    );
  }

  return (
    <input
      type={type}
      value={value}
      maxLength={maxLength}
      className={classNames("text-input", className)}
      readOnly={readOnly}
      {...props}
    />
  );
};

TextInput.propTypes = {
  className: PropTypes.string,
  type: PropTypes.oneOf(["text", "email", "password", "textarea", "url"]),
  readOnly: PropTypes.bool,
  value: PropTypes.string,
  maxLength: PropTypes.number,
};

TextInput.defaultProps = {
  className: "",
  type: "text",
  readOnly: false,
  value: "",
  maxLength: 0,
};

export default TextInput;
