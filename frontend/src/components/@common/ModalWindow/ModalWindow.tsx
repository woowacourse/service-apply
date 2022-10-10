import classNames from "classnames";
import styles from "./ModalWindow.module.css";

type ModalWindowProps = Omit<React.HTMLAttributes<HTMLDivElement>, "role" | "aria-label">;

const ModalWindow = ({ className, children, ...props }: ModalWindowProps) => {
  return (
    <div
      tabIndex={-1}
      role="dialog"
      aria-label="dialogTitle"
      className={classNames(styles.box, className)}
      {...props}
    >
      {children}
    </div>
  );
};

export default ModalWindow;
