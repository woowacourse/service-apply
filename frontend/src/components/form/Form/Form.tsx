import React from "react";
import classNames from "classnames";

import styles from "./Form.module.css";

type FormProps = {
  className?: string;
  children: JSX.Element;
  onSubmit: React.FormEventHandler<HTMLFormElement>;
};

const Form = ({ className = "", children, onSubmit }: FormProps) => {
  return (
    <form className={classNames(styles.form, className)} onSubmit={onSubmit}>
      {children}
    </form>
  );
};

export default Form;
