import PropTypes from "prop-types";
import React from "react";
import CheckBox from "../CheckBox/CheckBox";
import styles from "./SummaryCheckField.module.css";

const SummaryCheckField = ({ children, label, required, ...props }) => {
  return (
    <div className={styles.box}>
      <CheckBox label={label} required={required} {...props} />
      <div className={styles.summary}>
        <div className={styles.text}>{children}</div>
      </div>
    </div>
  );
};

SummaryCheckField.propTypes = {
  children: PropTypes.node.isRequired,
  label: PropTypes.string,
  required: PropTypes.bool,
};

SummaryCheckField.defaultProps = {
  label: "",
  required: false,
};

export default SummaryCheckField;
