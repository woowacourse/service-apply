import React from "react";
import PropTypes from "prop-types";
import styles from "./Form.module.css";

const Form = ({ children, actions, footer, ...props }) => {
  return (
    <form className={styles.form} {...props}>
      {children}
      <div className={styles.actions}>{actions}</div>
      <footer>
        <a className={styles.logo} href="#">
          <img
            src={process.env.PUBLIC_URL + "/assets/logo/logo_full_dark.png"}
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
  actions: PropTypes.node,
  footer: PropTypes.node,
};

Form.defaultProps = {
  actions: null,
  footer: null,
};

export default Form;
