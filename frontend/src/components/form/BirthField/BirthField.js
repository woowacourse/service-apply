import PropTypes from "prop-types";
import React, { useEffect } from "react";
import useFormContext from "../../../hooks/useFormContext";
import Label from "../../@common/Label/Label";
import TextInput from "../../@common/TextInput/TextInput";
import styles from "./BirthField.module.css";

const currentYear = new Date().getFullYear();

const years = Array(50)
  .fill(null)
  .map((_, idx) => currentYear - idx);

const months = Array(12)
  .fill(null)
  .map((_, idx) => idx + 1);

const days = Array(31)
  .fill(null)
  .map((_, idx) => idx + 1);

const BirthField = ({ required, className }) => {
  const { value, errorMessage, handleChange, register, unRegister } =
    useFormContext();

  useEffect(() => {
    register("year");
    register("month");
    register("day");

    return () => {
      unRegister("year");
      unRegister("month");
      unRegister("day");
    };
  }, []);

  return (
    <div className={className}>
      <div className={styles.box}>
        <Label for="year" required={required} className={styles.label}>
          생년월일
        </Label>
        <div className={styles.birth}>
          <TextInput
            className={styles["input-box"]}
            type="number"
            id="year"
            name="year"
            list="years"
            placeholder="YYYY"
            min="0"
            max={currentYear}
            onChange={handleChange}
            value={value.year}
            required={required}
            autoComplete="off"
          />
          <datalist id="years">
            {years.map((year) => (
              <option key={year}>{year}</option>
            ))}
          </datalist>
          <TextInput
            className={styles["input-box"]}
            name="month"
            list="months"
            type="number"
            placeholder="MM"
            min="1"
            max="12"
            onChange={handleChange}
            value={value.month}
            required={required}
            autoComplete="off"
          />
          <datalist id="months">
            {months.map((month) => (
              <option key={month}>{month}</option>
            ))}
          </datalist>
          <TextInput
            className={styles["input-box"]}
            name="day"
            list="days"
            type="number"
            min="1"
            max="31"
            placeholder="DD"
            onChange={handleChange}
            value={value.day}
            required={required}
            autoComplete="off"
          />
          <datalist id="days">
            {days.map((day) => (
              <option key={day}>{day}</option>
            ))}
          </datalist>
        </div>
      </div>

      <p className={styles["rule-field"]}>
        {errorMessage.year || errorMessage.month || errorMessage.day}
      </p>
    </div>
  );
};

BirthField.propTypes = {
  required: PropTypes.bool,
  className: PropTypes.string,
};

BirthField.defaultProps = {
  required: false,
};

export default BirthField;
