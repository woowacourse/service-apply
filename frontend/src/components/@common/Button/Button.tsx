import classNames from "classnames";
import styles from "./Button.module.css";

export const BUTTON_VARIANT = {
  CONTAINED: "contained",
  OUTLINED: "outlined",
} as const;

export type ButtonProps = React.ButtonHTMLAttributes<HTMLButtonElement> & {
  variant?: typeof BUTTON_VARIANT[keyof typeof BUTTON_VARIANT];
  cancel?: boolean;
  disabled?: boolean;
};

const Button = ({
  className,
  variant = BUTTON_VARIANT.CONTAINED,
  cancel = false,
  disabled = false,
  children,
  ...props
}: ButtonProps) => {
  return (
    <button
      className={classNames(className, styles[variant], styles.button, {
        [styles.cancel]: cancel,
      })}
      disabled={disabled}
      {...props}
    >
      {children}
    </button>
  );
};

export default Button;
