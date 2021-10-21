import classNames from "classnames";
import PropTypes from "prop-types";
import React from "react";
import Label from "../../@common/Label/Label";
import Radio from "../../@common/Radio/Radio";
import styles from "./GenderField.module.css";

const GenderField = ({ required, className, value, onChange }) => {
  return (
    <div className={classNames(styles["gender-field"], className)}>
      <Label className={styles.label} required={required}>
        성별
      </Label>
      <div className={styles["gender-group"]}>
        <Radio
          onChange={onChange}
          name="gender"
          label="남자"
          value="MALE"
          checked={value === "MALE"}
          required={required}
        />
        <Radio
          onChange={onChange}
          name="gender"
          label="여자"
          value="FEMALE"
          checked={value === "FEMALE"}
          required={required}
        />
      </div>
    </div>
  );
};

GenderField.propTypes = {
  value: PropTypes.string.isRequired,
  onChange: PropTypes.func.isRequired,
  required: PropTypes.bool,
  className: PropTypes.string,
};

GenderField.defaultProps = {
  required: false,
};

export default GenderField;
