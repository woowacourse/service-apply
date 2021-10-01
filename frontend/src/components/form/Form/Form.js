import React from "react";
import PropTypes from "prop-types";
import classNames from "classnames";
import styles from "./Form.module.css";

const Form = ({ children, actions, className, ...props }) => {
  return (
    <form className={classNames(styles.form, className)} {...props}>
      {children}
    </form>
  );
};

Form.propTypes = {
  children: PropTypes.node.isRequired,
  className: PropTypes.string,
};

Form.defaultProps = {
  className: "",
};

export default Form;
