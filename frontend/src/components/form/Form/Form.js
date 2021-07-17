import React from 'react';
import PropTypes from 'prop-types';
import './Form.css';

const Form = ({ children, actions, footer, ...props }) => {
  return (
    <form className="form" {...props}>
      {children}
      <div className="actions">{actions}</div>
      <footer>
        <a className="logo" href="#">
          <img
            src={process.env.PUBLIC_URL + 'assets/logo/logo_full_dark.png'}
            alt="우아한테크코스 로고"
          />
        </a>
        {footer}
      </footer>
    </form>
  );
};

Form.propTypes = {
  children: PropTypes.node.isRequired,
  actions: PropTypes.node.isRequired,
  footer: PropTypes.node,
};

export default Form;
