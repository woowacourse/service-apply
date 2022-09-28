import classNames from "classnames";
import PropTypes from "prop-types";
import React from "react";
import styles from "./Container.module.css";

export const CONTAINER_SIZE = {
  DEFAULT: "default",
  NARROW: "narrow",
};

const Container = ({ title, titleAlign = "center", size, children, className, ...props }) => {
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
  titleAlign: PropTypes.oneOf(["left", "center", "right"]),
  size: PropTypes.oneOf(["default", "narrow"]),
  children: PropTypes.node,
};

Container.defaultProps = {
  size: "default",
  titleAlign: "center",
};
