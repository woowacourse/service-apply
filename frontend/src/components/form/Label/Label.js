import React from 'react';
import PropTypes from 'prop-types';
import './Label.css';

const Label = ({ children, className, required }) => {
  const getClassName = () => {
    const classList = ['label'];

    if (required) {
      classList.push('required');
    }

    if (className) {
      classList.push(className);
    }

    return classList.join(' ');
  };

  return <label className={getClassName()}>{children}</label>;
};

Label.propTypes = {
  children: PropTypes.node.isRequired,
  className: PropTypes.string,
  required: PropTypes.bool,
};

Label.defaultProps = {
  className: '',
  required: false,
};

export default Label;
