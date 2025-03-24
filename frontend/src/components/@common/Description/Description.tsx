import classNames from "classnames";
import styles from "./Description.module.css";

export type DescriptionProps = {
  className?: string;
  translate?: "yes" | "no";
  children: React.ReactNode;
};

const Description = ({ className, translate, children }: DescriptionProps) => {
  return (
    <div className={classNames(styles.description, className)} translate={translate}>
      {children}
    </div>
  );
};

export default Description;
