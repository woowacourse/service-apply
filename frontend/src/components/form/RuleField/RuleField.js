import React from "react";
import PropTypes from "prop-types";
import "./RuleField.css";

const RuleField = ({ rules, target }) => {
  const result = rules.reduce((prev, curr) => prev && curr(target), true);
  const incorrect = result === true ? true : result;

  return (
    <div className="rule-field">{incorrect && <span>{incorrect}</span>}</div>
  );
};

RuleField.propTypes = {
  rules: PropTypes.array.isRequired,
  target: PropTypes.string.isRequired,
};

export default RuleField;