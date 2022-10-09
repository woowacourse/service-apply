import React from "react";

import CheckBox from "../CheckBox/CheckBox";
import styles from "./SummaryCheckField.module.css";

type SummaryCheckFieldProps = React.InputHTMLAttributes<HTMLInputElement> & {
  label: string;
};

const SummaryCheckField = ({
  label,
  required = false,
  children,
  ...props
}: SummaryCheckFieldProps) => {
  return (
    <div className={styles.box}>
      <CheckBox label={label} required={required} {...props} />
      <div className={styles.summary}>
        <div className={styles.text}>{children}</div>
      </div>
    </div>
  );
};

export default SummaryCheckField;
