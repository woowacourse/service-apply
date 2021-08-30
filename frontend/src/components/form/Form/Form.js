import React from "react";
import PropTypes from "prop-types";
import classNames from "classnames";
import styles from "./Form.module.css";

const Form = ({ children, actions, footer, className, ...props }) => {
  return (
    <form className={classNames(styles.form, className)} {...props}>
      {children}
      <div className={styles.actions}>{actions}</div>
      <footer>
        <div className={styles.logo}>
          <img
            src={process.env.PUBLIC_URL + "/assets/logo/logo_full_dark.png"}
            alt="우아한테크코스 로고"
          />
        </div>
        {footer}
      </footer>
    </form>
  );
};

Form.propTypes = {
  children: PropTypes.node.isRequired,
  actions: PropTypes.node,
  footer: PropTypes.node,
  className: PropTypes.string,
};

Form.defaultProps = {
  actions: null,
  footer: null,
  className: "",
};

export default Form;
