import React from "react";
import classNames from "classnames";
import styles from "./Description.module.css";

export type DescriptionProps = {
  className?: string;
  children: React.ReactNode;
};

const Description = ({ className, children }: DescriptionProps) => {
  return <div className={classNames(styles.description, className)}>{children}</div>;
};

export default Description;
