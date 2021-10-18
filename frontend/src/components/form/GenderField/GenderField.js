import PropTypes from "prop-types";
import React, { useEffect } from "react";
import classNames from "classnames";
import useFormContext from "../../../hooks/useFormContext";
import Label from "../../@common/Label/Label";
import Radio from "../../@common/Radio/Radio";
import styles from "./GenderField.module.css";

const GenderField = ({ required, className }) => {
  const { handleChange, register, unRegister } = useFormContext();

  useEffect(() => {
    register("gender");

    return () => {
      unRegister("gender");
    };
  }, []);

  return (
    <div className={classNames(styles["gender-field"], className)}>
      <Label className={styles.label} required={required}>
        성별
      </Label>
      <div className={styles["gender-group"]}>
        <Radio
          onChange={handleChange}
          name="gender"
          label="남자"
          value="MALE"
          required={required}
        />
        <Radio
          onChange={handleChange}
          name="gender"
          label="여자"
          value="FEMALE"
          required={required}
        />
      </div>
    </div>
  );
};

GenderField.propTypes = {
  required: PropTypes.bool,
  className: PropTypes.string,
};

GenderField.defaultProps = {
  required: false,
};

export default GenderField;
