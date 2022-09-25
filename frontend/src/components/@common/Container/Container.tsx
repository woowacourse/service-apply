import classNames from "classnames";
<<<<<<< HEAD:frontend/src/components/@common/Container/Container.js
import PropTypes from "prop-types";
=======
>>>>>>> 63fa356 (refactor: migrate common files to ts):frontend/src/components/@common/Container/Container.tsx
import styles from "./Container.module.css";

export const CONTAINER_SIZE = {
  DEFAULT: "default",
  NARROW: "narrow",
} as const;

export type ContainerProps = React.HTMLAttributes<HTMLDivElement> & {
  size?: typeof CONTAINER_SIZE[keyof typeof CONTAINER_SIZE];
  className?: string;
  title?: string;
  children: React.ReactNode;
};

<<<<<<< HEAD:frontend/src/components/@common/Container/Container.js
export const TITLE_ALIGN = {
  LEFT: "left",
  CENTER: "center",
  RIGHT: "right",
};

const Container = ({ title, titleAlign, size, children, className, ...props }) => {
=======
const Container = ({ size = "default", className, title, children, ...props }: ContainerProps) => {
>>>>>>> 63fa356 (refactor: migrate common files to ts):frontend/src/components/@common/Container/Container.tsx
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
<<<<<<< HEAD:frontend/src/components/@common/Container/Container.js

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
=======
>>>>>>> 63fa356 (refactor: migrate common files to ts):frontend/src/components/@common/Container/Container.tsx
