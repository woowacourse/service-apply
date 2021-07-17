import React from 'react';
import PropTypes from 'prop-types';
import './Button.css';

const Button = ({ children, type, cancel, ...props }) => {
  return (
    <button
      className={cancel ? 'button cancel' : 'button'}
      type={type}
      {...props}
    >
      {children}
    </button>
  );
};

Button.propTypes = {
  children: PropTypes.node.isRequired,
  type: PropTypes.oneOf(['button', 'submit', 'reset']),
  cancel: PropTypes.bool,
};

Button.defaultProps = {
  type: 'button',
};

export default Button;
