import classNames from "classnames";
import styles from "./ModalWindow.module.css";

type ModalWindowProps = Omit<React.HTMLAttributes<HTMLDivElement>, "role" | "aria-label"> & {
  title: string;
};

const ModalWindow = ({ className, title, children, ...props }: ModalWindowProps) => {
  return (
    <div
      tabIndex={-1}
      role="dialog"
      aria-label="dialogTitle"
      className={classNames(styles.box, className)}
      {...props}
    >
      <h2 id="dialogTitle" className={styles.title}>
        {title}
      </h2>
      {children}
    </div>
  );
};

export default ModalWindow;
