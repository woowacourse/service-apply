import React, { useEffect } from "react";
import Label from "../Label/Label";
import Radio from "../Radio/Radio";
import styles from "./GenderField.module.css";
import useFormContext from "../../../hooks/useFormContext";

const GenderField = () => {
  const { handleChange, register, unRegister } = useFormContext();

  useEffect(() => {
    register("gender");

    return () => {
      unRegister("gender");
    };
  }, []);

  return (
    <div className={styles["gender-field"]}>
      <Label required>성별</Label>
      <div className={styles["gender-group"]}>
        <Radio
          onChange={handleChange}
          name="gender"
          label="남자"
          value="MALE"
          required
        />
        <Radio
          onChange={handleChange}
          name="gender"
          label="여자"
          value="FEMALE"
          required
        />
      </div>
    </div>
  );
};

export default GenderField;
