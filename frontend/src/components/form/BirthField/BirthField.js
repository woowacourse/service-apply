import React, { useEffect } from "react";
import Label from "../../@common/Label/Label";
import TextInput from "../../@common/TextInput/TextInput";
import useFormContext from "../../../hooks/useFormContext";
import styles from "./BirthField.module.css";

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
      <div className={styles.container}>
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
      </div>

      <p className={styles["rule-field"]}>
        {errorMessage.year || errorMessage.month || errorMessage.day}
      </p>
    </>
  );
};

export default BirthField;
