import classNames from "classnames";
import PropTypes from "prop-types";
import styles from "./Container.module.css";

export const CONTAINER_SIZE = {
  DEFAULT: "default",
  NARROW: "narrow",
} as const;

export const TITLE_ALIGN = {
  LEFT: "left",
  CENTER: "center",
  RIGHT: "right",
} as const;

export type ContainerProps = React.HTMLAttributes<HTMLDivElement> & {
  size?: typeof CONTAINER_SIZE[keyof typeof CONTAINER_SIZE];
  className?: string;
  title?: string;
  titleAlign: typeof TITLE_ALIGN[keyof typeof TITLE_ALIGN];
  onClick?: React.MouseEventHandler<HTMLDivElement>;
};

const Container = ({
  size = CONTAINER_SIZE.DEFAULT,
  className,
  title,
  titleAlign = TITLE_ALIGN.CENTER,
  children,
  onClick = () => {},
}: ContainerProps) => {
  return (
    <div
      className={classNames(
        styles.box,
        { [styles.narrow]: size === CONTAINER_SIZE.NARROW },
        className
      )}
      onClick={onClick}
    >
      {title && (
        <h2
          className={classNames(styles.title, styles[`title-align-${titleAlign}`], {
            [styles["title-with-children"]]: children,
          })}
        >
          {title}
        </h2>
      )}
      {children}
    </div>
  );
};

export default Container;
