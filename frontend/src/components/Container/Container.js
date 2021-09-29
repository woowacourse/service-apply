import React from "react";
import PropTypes from "prop-types";
import classNames from "classnames";

import styles from "./Container.module.css";

export const CONTAINER_SIZE = {
  DEFAULT: "default",
  NARROW: "narrow",
};

const Container = ({ title, size, children, className }) => {
  return (
    <div
      className={classNames(
        styles.box,
        { [styles.narrow]: size === CONTAINER_SIZE.NARROW },
        className
      )}
    >
      {title && <h2 className={styles.title}>{title}</h2>}
      {children}
    </div>
  );
};

export default Container;

Container.propTypes = {
  title: PropTypes.string,
  size: PropTypes.oneOf(["default", "narrow"]),
  children: PropTypes.node.isRequired,
};

Container.defaultProps = {
  size: "default",
};
