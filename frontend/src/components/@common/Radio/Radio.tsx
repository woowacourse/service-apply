import Label from "../Label/Label";
import styles from "./Radio.module.css";

export type RadioProps = React.InputHTMLAttributes<HTMLInputElement> & {
  required?: boolean;
  label?: string;
};

const Radio = ({ required = false, label = "", ...props }: RadioProps) => {
  return (
    <Label className={styles["radio-label"]}>
      <input type="radio" required={required} {...props} />
      <div className={styles["custom-radio"]} />
      {label}
    </Label>
  );
};

export default Radio;
