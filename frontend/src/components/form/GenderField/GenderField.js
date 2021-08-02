import React from "react";
import Field from "../Field/Field";
import Label from "../Label/Label";
import Radio from "../Radio/Radio";
import styles from "./GenderField.module.css";

const GenderField = () => {
  return (
    <Field className={styles["gender-field"]}>
      <Label required>성별</Label>
      <div className={styles["gender-group"]}>
        <Radio name="gender" label="남자" value="male" required />
        <Radio name="gender" label="여자" value="female" required />
      </div>
    </Field>
  );
};

export default GenderField;
