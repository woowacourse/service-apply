import classNames from "classnames";
import styles from "./ToggleButton.module.css";

interface ToggleButtonProps {
  checked: boolean;
  onChange: (checked: boolean) => void;
  ariaLabel: string;
  children: React.ReactNode;
}

const ToggleButton = ({ checked, onChange, ariaLabel, children }: ToggleButtonProps) => {
  return (
    <label
      className={classNames(styles["toggle-label"], {
        [styles.checked]: checked,
        [styles.unchecked]: !checked,
      })}
      aria-label={ariaLabel}
    >
      {children}
      <input
        type="checkbox"
        className={styles["toggle-checkbox"]}
        checked={checked}
        onChange={(e) => onChange(e.target.checked)}
      />
    </label>
  );
};

export default ToggleButton;
