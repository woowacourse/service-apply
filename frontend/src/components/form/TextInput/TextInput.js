import React from 'react';
import PropTypes from 'prop-types';
import './TextInput.css';

const TextInput = ({ type, readOnly, value, maxLength }) => {
  if (type === 'textarea') {
    return (
      <textarea
        value={value}
        maxLength={maxLength}
        className="text-input"
        readOnly={readOnly}
      />
    );
  }

  return (
    <input
      type={type}
      value={value}
      maxLength={maxLength}
      className="text-input"
      readOnly={readOnly}
    />
  );
};

TextInput.propTypes = {
  type: PropTypes.oneOf(['text', 'email', 'password', 'textarea', 'url']),
  readOnly: PropTypes.bool,
  value: PropTypes.string,
  maxLength: PropTypes.number,
};

TextInput.defaultProps = {
  type: 'text',
  maxLength: 0,
};

export default TextInput;
