import React from 'react';
import PropTypes from 'prop-types';
import './Description.css';

const Description = ({ children }) => {
  return <div className="description">{children}</div>;
};

Description.propTypes = {
  children: PropTypes.node.isRequired,
};

export default Description;
