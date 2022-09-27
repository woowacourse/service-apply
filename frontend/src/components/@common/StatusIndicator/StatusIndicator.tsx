import classNames from "classnames";
import styles from "./StatusIndicator.module.css";

export type StatusIndicatorProps = {
  text: string;
  className: string;
  active: boolean;
};

const StatusIndicator = ({ text, className, active, ...props }: StatusIndicatorProps) => {
  return (
    <span className={styles["statusIndicator-box"]}>
      <span
        className={classNames(className, styles.statusIndicator, active ? styles.active : "")}
        {...props}
      >
        {text}
      </span>
    </span>
  );
};

export default StatusIndicator;
