import React from "react";
import PropTypes from "prop-types";
import classNames from "classnames";
import styles from "./TextInput.module.css";

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
        className={classNames(styles["text-input"], className)}
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
      className={classNames(styles["text-input"], className)}
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
  maxLength: undefined,
};

export default TextInput;
