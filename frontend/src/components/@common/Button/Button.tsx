import classNames from "classnames";
import styles from "./Button.module.css";

export const BUTTON_VARIANT = {
  CONTAINED: "contained",
  OUTLINED: "outlined",
} as const;

export type ButtonProps = React.ButtonHTMLAttributes<HTMLButtonElement> & {
  className?: string;
  variant?: typeof BUTTON_VARIANT[keyof typeof BUTTON_VARIANT];
  cancel?: boolean;
  children: React.ReactNode;
};

const Button = ({
  className,
  variant = BUTTON_VARIANT.CONTAINED,
  cancel = false,
  type,
  children,
  ...props
}: ButtonProps) => {
  return (
    <button
      className={classNames(className, styles[variant], styles.button, {
        [styles.cancel]: cancel,
      })}
      type={type}
      {...props}
    >
      {children}
    </button>
  );
};

export default Button;
