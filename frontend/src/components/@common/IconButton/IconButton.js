import React from "react";
import PropTypes from "prop-types";
import styles from "./IconButton.module.css";

const IconButton = ({ children, type, className, src, alt, ...props }) => {
  return (
    <button type={type} className={styles.button} {...props}>
      <img src={src} alt={alt} className={className} />
    </button>
  );
};

IconButton.propTypes = {
  type: PropTypes.oneOf(["button", "submit", "reset"]),
  className: PropTypes.string,
  src: PropTypes.string.isRequired,
  alt: PropTypes.string,
};

export default IconButton;
