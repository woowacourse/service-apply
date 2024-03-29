import classNames from "classnames";
import ko from "date-fns/locale/ko";
import PropTypes from "prop-types";
import DatePicker, { registerLocale } from "react-datepicker";
import "react-datepicker/dist/react-datepicker.css";
import Label from "../../@common/Label/Label";
import styles from "./BirthField.module.css";

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
          showYearDropdown
          showMonthDropdown
          popperPlacement="bottom-start"
          dropdownMode="select"
          autoComplete="off"
          {...props}
        />
      </div>
    </div>
  );
};

BirthField.propTypes = {
  value: PropTypes.instanceOf(Date),
  onChange: PropTypes.func,
  errorMessage: PropTypes.string,
  required: PropTypes.bool,

  className: PropTypes.string,
};

BirthField.defaultProps = {
  value: null,
  required: false,
};

export default BirthField;
