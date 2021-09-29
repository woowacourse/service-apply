import React, { useEffect } from "react";
import Field from "../Field/Field";
import Label from "../Label/Label";
import TextInput from "../TextInput/TextInput";
import styles from "./BirthField.module.css";
import useFormContext from "../../../hooks/useFormContext";

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

const BirthField = () => {
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
    <>
      <Field className={styles["birth-field"]}>
        <Label for="year">생년월일</Label>
        <div className={styles.birth}>
          <TextInput
            className={styles.year}
            type="number"
            id="year"
            name="year"
            list="years"
            placeholder="YYYY"
            min="0"
            max={currentYear}
            onChange={handleChange}
            value={value.year}
          />
          <datalist id="years">
            {years.map((year) => (
              <option key={year}>{year}</option>
            ))}
          </datalist>
          <TextInput
            className={styles.month}
            name="month"
            list="months"
            type="number"
            placeholder="MM"
            min="1"
            max="12"
            onChange={handleChange}
            value={value.month}
          />
          <datalist id="months">
            {months.map((month) => (
              <option key={month}>{month}</option>
            ))}
          </datalist>
          <TextInput
            className={styles.day}
            name="day"
            list="days"
            type="number"
            min="1"
            max="31"
            placeholder="DD"
            onChange={handleChange}
            value={value.day}
          />
          <datalist id="days">
            {days.map((day) => (
              <option key={day}>{day}</option>
            ))}
          </datalist>
        </div>
      </Field>

      <p className={styles["rule-field"]}>
        {errorMessage.year || errorMessage.month || errorMessage.day}
      </p>
    </>
  );
};

export default BirthField;
