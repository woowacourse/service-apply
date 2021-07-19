import React from 'react';
import PropTypes from 'prop-types';
import './Field.css';

const Field = ({ children, className }) => {
  return (
    <div className={className ? `${className} field` : 'field'}>{children}</div>
  );
};

Field.propTypes = {
  children: PropTypes.node.isRequired,
  className: PropTypes.string,
};

Field.defaultProps = {
  className: '',
};

export default Field;
