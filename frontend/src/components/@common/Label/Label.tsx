import classNames from "classnames";
import styles from "./Label.module.css";

export type LabelProps = {
  htmlFor?: string;
  className?: string;
  required?: boolean;
  translate?: "yes" | "no";
  children: React.ReactNode;
};

const Label = ({ htmlFor, className = "", required = false, translate, children }: LabelProps) => {
  return (
    <label
      htmlFor={htmlFor}
      className={classNames(styles.label, className, {
        [styles.required]: required,
      })}
      translate={translate}
    >
      {children}
    </label>
  );
};

export default Label;
