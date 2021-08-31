import React from "react";
import PropTypes from "prop-types";
import Field from "../Field/Field";
import Label from "../Label/Label";
import Radio from "../Radio/Radio";
import styles from "./GenderField.module.css";

const GenderField = ({ onChange }) => {
  return (
    <Field className={styles["gender-field"]}>
      <Label required>성별</Label>
      <div className={styles["gender-group"]}>
        <Radio
          onChange={onChange}
          name="gender"
          label="남자"
          value="male"
          required
        />
        <Radio
          onChange={onChange}
          name="gender"
          label="여자"
          value="female"
          required
        />
      </div>
    </Field>
  );
};

GenderField.propTypes = {
  onChange: PropTypes.func.isRequired,
};

export default GenderField;
