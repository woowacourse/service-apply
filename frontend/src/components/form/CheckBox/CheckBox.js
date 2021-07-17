import React from 'react';
import PropTypes from 'prop-types';
import Label from '../Label/Label';
import './CheckBox.css';

const CheckBox = ({ label, required, ...props }) => {
  return (
    <Label className="checkbox">
      <input type="checkbox" required={required} {...props} />
      {label}
    </Label>
  );
};

CheckBox.propTypes = {
  label: PropTypes.string,
  required: PropTypes.bool,
};

export default CheckBox;
