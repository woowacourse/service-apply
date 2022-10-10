import classNames from "classnames";
import styles from "./Label.module.css";

export type LabelProps = {
  htmlFor?: string;
  className?: string;
  required?: boolean;
  children: React.ReactNode;
};

const Label = ({ htmlFor, className = "", required = false, children }: LabelProps) => {
  return (
    <label
      htmlFor={htmlFor}
      className={classNames(styles.label, className, {
        [styles.required]: required,
      })}
    >
      {children}
    </label>
  );
};

export default Label;
