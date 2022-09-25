import React from "react";
import classNames from "classnames";
import styles from "./Label.module.css";

export type LabelProps = {
  className?: string;
  required?: boolean;
  children: React.ReactNode;
};

const Label = ({ className = "", required = false, children }: LabelProps) => {
  return (
    <label
      className={classNames(styles.label, className, {
        [styles.required]: required,
      })}
    >
      {children}
    </label>
  );
};

export default Label;
