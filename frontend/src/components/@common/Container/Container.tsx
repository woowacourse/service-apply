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
};

export type ContainerProps = React.HTMLAttributes<HTMLDivElement> & {
  size?: typeof CONTAINER_SIZE[keyof typeof CONTAINER_SIZE];
  title?: string;
  children: React.ReactNode;
  titleAlign: typeof TITLE_ALIGN[keyof typeof TITLE_ALIGN];
};

const Container = ({ title, titleAlign, size, children, className, ...props }: ContainerProps) => {
  return (
    <div
      className={classNames(
        styles.box,
        { [styles.narrow]: size === CONTAINER_SIZE.NARROW },
        className
      )}
      {...props}
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

Container.propTypes = {
  title: PropTypes.string,
  titleAlign: PropTypes.oneOf(Object.values(TITLE_ALIGN)),
  size: PropTypes.oneOf(Object.values(CONTAINER_SIZE)),
  children: PropTypes.node,
};

Container.defaultProps = {
  size: "default",
  titleAlign: "center",
};
