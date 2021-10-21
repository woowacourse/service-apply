import classNames from "classnames";
import PropTypes from "prop-types";
import React, { useState } from "react";
import DatePicker, { registerLocale } from "react-datepicker";
import { formatDate } from "../../../utils/format/date";
import Label from "../../@common/Label/Label";
import styles from "./BirthField.module.css";
import "react-datepicker/dist/react-datepicker.css";
import ko from "date-fns/locale/ko";

registerLocale("ko", ko);

const BirthField = ({ value, onChange, required, className, ...props }) => {
  return (
    <div className={classNames(styles.box, className)}>
      <div className={styles["input-box"]}>
        <Label htmlFor="birthday" required={required} className={styles.label}>
          생년월일
        </Label>
        <DatePicker
          id="birthday"
          locale="ko"
          dateFormat="yyyy-MM-dd"
          selected={value}
          onChange={onChange}
          maxDate={new Date()}
          className={styles.input}
          placeholderText="YYYY-MM-DD"
          {...props}
        />
      </div>
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
