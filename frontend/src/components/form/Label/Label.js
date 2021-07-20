import React from 'react';
import PropTypes from 'prop-types';
import './Label.css';
import classNames from 'classnames';

const Label = ({ children, className, required }) => {
  return (
    <label className={classNames('label', className, { required })}>
      {children}
    </label>
  );
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
