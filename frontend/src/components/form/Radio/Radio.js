import React from 'react';
import PropTypes from 'prop-types';
import Label from '../Label/Label';
import './Radio.css';

const Radio = ({ label, required, ...props }) => {
  return (
    <Label className="radio-label" required={required}>
      <input type="radio" required={required} {...props} />
      {label}
    </Label>
  );
};

Radio.propTypes = {
  label: PropTypes.string,
  required: PropTypes.bool,
};

export default Radio;
