import classNames from "classnames";
import styles from "./Spacing.module.css";

interface SpacingProps {
  direction?: "vertical" | "horizontal";
  size?: number;
}

const Spacing = ({ direction = "vertical", size = 8 }: SpacingProps) => {
  const directionClass = direction === "vertical" ? styles.vertical : styles.horizontal;

  return (
    <div
      className={classNames(styles.spacing, directionClass)}
      style={
        {
          "--spacing-size": `${size}px`,
        } as React.CSSProperties & { "--spacing-size": string }
      }
    />
  );
};

export default Spacing;
