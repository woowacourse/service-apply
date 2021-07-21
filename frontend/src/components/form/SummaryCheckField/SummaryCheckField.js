import React from "react";
import PropTypes from "prop-types";
import Field from "../Field/Field";
import CheckBox from "../CheckBox/CheckBox";
import "./SummaryCheckField.css";

const SummaryCheckField = ({ children, label, required, ...props }) => {
  return (
    <Field>
      <CheckBox label={label} required={required} {...props} />
      <div className="summary" tabIndex="-1">
        <div className="text">{children}</div>
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
