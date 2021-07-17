import React from 'react';
import PropTypes from 'prop-types';
import './Field.css';

const Field = ({ children, className }) => {
  const getClassName = () => {
    const classList = ['field'];

    if (className) {
      classList.push(className);
    }

    return classList.join(' ');
  };

  return <div className={getClassName()}>{children}</div>;
};

Field.propTypes = {
  children: PropTypes.node.isRequired,
  className: PropTypes.string,
};

export default Field;
