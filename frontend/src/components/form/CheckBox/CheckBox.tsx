import React from "react";

import Label from "../../@common/Label/Label";
import styles from "./CheckBox.module.css";

type CheckBoxProps = React.InputHTMLAttributes<HTMLInputElement> & {
  label?: string;
};

const CheckBox = ({
  name,
  checked = false,
  required = false,
  label = "",
  onChange,
  ...props
}: CheckBoxProps) => {
  return (
    <Label className={styles.checkbox}>
      <input
        type="checkbox"
        name={name}
        checked={checked}
        onChange={onChange}
        required={required}
        {...props}
      />
      <div className={styles["custom-checkbox"]} />
      {label}
    </Label>
  );
};

export default CheckBox;
