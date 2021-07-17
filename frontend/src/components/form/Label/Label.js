import React from 'react';
import PropTypes from 'prop-types';
import './Label.css';

const Label = ({ required, children }) => {
  return (
    <label className={required ? 'label required' : 'label'}>{children}</label>
  );
};

Label.propTypes = {
  required: PropTypes.bool,
  children: PropTypes.node,
};

export default Label;
