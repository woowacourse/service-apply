import classNames from "classnames";
import Label from "../../@common/Label/Label";
import Radio from "../../@common/Radio/Radio";
import styles from "./GenderField.module.css";

type GenderFieldProps = {
  className?: string;
  required?: boolean;
  value: string;
  onChange: React.ChangeEventHandler<HTMLInputElement>;
};

const GenderField = ({ className, required = false, value, onChange }: GenderFieldProps) => {
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

export default GenderField;
