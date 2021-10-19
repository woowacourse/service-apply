import classNames from "classnames";
import PropTypes from "prop-types";
import React from "react";
import { formatDate } from "../../../utils/format/date";
import Label from "../../@common/Label/Label";
import styles from "./BirthField.module.css";

const BirthField = ({ value, onChange, errorMessage, required, className, ...props }) => {
  return (
    <div className={classNames(styles.box, className)}>
      <div className={styles["input-box"]}>
        <Label htmlFor="birthday" required={required} className={styles.label}>
          생년월일
        </Label>
        <input
          id="birthday"
          type="date"
          value={value}
          onKeyDown={(event) => event.preventDefault()}
          onChange={onChange}
          max={formatDate(new Date())}
          className={styles.input}
          {...props}
        />
      </div>
      {errorMessage && <p className={styles["rule-field"]}>{errorMessage}</p>}
    </div>
  );
};

BirthField.propTypes = {
  value: PropTypes.string.isRequired,
  onChange: PropTypes.func.isRequired,
  errorMessage: PropTypes.string,
  required: PropTypes.bool,

  className: PropTypes.string,
};

BirthField.defaultProps = {
  required: false,
};

export default BirthField;
