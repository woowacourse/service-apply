import React from "react";
import PropTypes from "prop-types";
import Field from "../Field/Field";
import Label from "../Label/Label";
import TextInput from "../TextInput/TextInput";
import styles from "./BirthField.module.css";

const BirthField = ({
  required,
  year,
  month,
  day,
  onChangeYear,
  onChangeMonth,
  onChangeDay,
}) => {
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
          onChange={onChangeYear}
          value={year}
          required={required}
        />
        <TextInput
          className={styles.month}
          name="month"
          type="text"
          placeholder="MM"
          onChange={onChangeMonth}
          value={month}
          required={required}
        />
        <TextInput
          className={styles.day}
          name="day"
          type="text"
          placeholder="DD"
          onChange={onChangeDay}
          value={day}
          required={required}
        />
      </div>
    </Field>
  );
};

BirthField.propTypes = {
  required: PropTypes.bool,
  year: PropTypes.string.isRequired,
  month: PropTypes.string.isRequired,
  day: PropTypes.string.isRequired,
  onChangeYear: PropTypes.func.isRequired,
  onChangeMonth: PropTypes.func.isRequired,
  onChangeDay: PropTypes.func.isRequired,
};

BirthField.defaultProps = {
  required: false,
};

export default BirthField;
