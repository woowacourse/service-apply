import React from "react";
import PropTypes from "prop-types";
import classNames from "classnames";
import styles from "./Textarea.module.css";

const Textarea = ({ className, readOnly, value, maxLength, onChange, ...props }) => {
  return (
    <textarea
      value={value}
      onChange={onChange}
      maxLength={maxLength}
      className={classNames(styles["text-input"], className)}
      readOnly={readOnly}
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
