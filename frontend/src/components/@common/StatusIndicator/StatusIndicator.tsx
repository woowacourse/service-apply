import classNames from "classnames";
import styles from "./StatusIndicator.module.css";

export type StatusIndicatorProps = React.HTMLAttributes<HTMLSpanElement> & {
  className: string;
  active: boolean;
  text: string;
};

const StatusIndicator = ({ className, active, text, ...props }: StatusIndicatorProps) => {
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
