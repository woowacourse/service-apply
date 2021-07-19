import React from 'react';
import PropTypes from 'prop-types';
import classNames from 'classnames';
import './Field.css';

const Field = ({ children, className }) => {
  return <div className={classNames('field', className)}>{children}</div>;
};

Field.propTypes = {
  children: PropTypes.node.isRequired,
  className: PropTypes.string,
};

Field.defaultProps = {
  className: '',
};

export default Field;
