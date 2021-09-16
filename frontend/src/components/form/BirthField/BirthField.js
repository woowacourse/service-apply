import React, { useEffect } from "react";
import Field from "../Field/Field";
import Label from "../Label/Label";
import TextInput from "../TextInput/TextInput";
import styles from "./BirthField.module.css";
import useFormContext from "../../../hooks/useFormContext";

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
            id="year"
            name="year"
            type="text"
            placeholder="YYYY"
            onChange={handleChange}
            value={value.year}
          />
          <TextInput
            className={styles.month}
            name="month"
            type="text"
            placeholder="MM"
            onChange={handleChange}
            value={value.month}
          />
          <TextInput
            className={styles.day}
            name="day"
            type="text"
            placeholder="DD"
            onChange={handleChange}
            value={value.day}
          />
        </div>
      </Field>

      <p className={styles["rule-field"]}>
        {errorMessage.year || errorMessage.month || errorMessage.day}
      </p>
    </>
  );
};

export default BirthField;
