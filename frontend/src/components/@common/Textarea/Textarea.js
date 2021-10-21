import React from "react";
import PropTypes from "prop-types";
import classNames from "classnames";
import styles from "./Textarea.module.css";

const Textarea = ({ className, readOnly, value, maxLength, onChange, ...props }) => {
  const handleChange = (event) => {
    event.target.value = event.target.value.replaceAll("\r\n", "\n");

    if (maxLength !== undefined && event.target.value.length > maxLength) {
      event.target.value = event.target.value.slice(0, maxLength);
    }

    onChange(event);
  };

  const trimEnd = (event) => {
    event.target.value = event.target.value.trimEnd();

    onChange(event);
  };

  return (
    <textarea
      value={value}
      onChange={handleChange}
      className={classNames(styles["text-input"], className)}
      readOnly={readOnly}
      onBlur={trimEnd}
      {...props}
    />
  );
};

Textarea.propTypes = {
  className: PropTypes.string,
  readOnly: PropTypes.bool,
  value: PropTypes.string,
  maxLength: PropTypes.number,
};

Textarea.defaultProps = {
  className: "",
  readOnly: false,
  value: "",
};

export default Textarea;
