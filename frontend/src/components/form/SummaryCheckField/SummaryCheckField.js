import React from "react";
import PropTypes from "prop-types";
import Field from "../Field/Field";
import CheckBox from "../CheckBox/CheckBox";
import { TAB_INDEX } from "../../../constants/style";
import styles from "./SummaryCheckField.module.css";

const SummaryCheckField = ({ children, label, required, ...props }) => {
  return (
    <Field>
      <CheckBox label={label} required={required} {...props} />
      <div className={styles.summary} tabIndex={TAB_INDEX.SUMMARY}>
        <div className={styles.text}>{children}</div>
      </div>
    </Field>
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
