import React from "react";
import PropTypes from "prop-types";
import Field from "../Field/Field";
import Label from "../Label/Label";
import TextInput from "../TextInput/TextInput";
import styles from "./BirthField.module.css";

const BirthField = ({ required, value, onChange }) => {
  return (
    <Field className={styles["birth-field"]}>
      <Label for="year" required={required}>
        생년월일
      </Label>
      <div className={styles.birth}>
        <TextInput
          className="year"
          id="year"
          name="year"
          type="text"
          placeholder="YYYY"
          onChange={onChange}
          value={value.year}
          required={required}
        />
        <TextInput
          className={styles.month}
          name="month"
          type="text"
          placeholder="MM"
          onChange={onChange}
          value={value.month}
          required={required}
        />
        <TextInput
          className={styles.day}
          name="day"
          type="text"
          placeholder="DD"
          onChange={onChange}
          value={value.day}
          required={required}
        />
      </div>
    </Field>
  );
};

BirthField.propTypes = {
  required: PropTypes.bool,
  value: PropTypes.shape({
    year: PropTypes.string.isRequired,
    month: PropTypes.string.isRequired,
    day: PropTypes.string.isRequired,
  }).isRequired,
  onChange: PropTypes.func.isRequired,
};

BirthField.defaultProps = {
  required: false,
};

export default BirthField;
