import DatePicker, { ReactDatePickerProps, registerLocale } from "react-datepicker";
import classNames from "classnames";
import ko from "date-fns/locale/ko";
import "react-datepicker/dist/react-datepicker.css";

import Label from "../../@common/Label/Label";

import styles from "./BirthField.module.css";

registerLocale("ko", ko);

type BirthFieldProps = Omit<ReactDatePickerProps, "value"> & {
  value: Date | null;
};

const BirthField = ({
  value = null,
  onChange,
  required = false,
  className,
  name,
  ...props
}: BirthFieldProps) => {
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

export default BirthField;
