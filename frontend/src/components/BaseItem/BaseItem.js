import React from "react";
import PropTypes from "prop-types";

import styles from "./BaseItem.module.css";

const BaseItem = ({ title, startDateTime, endDateTime }) => {
  return (
    <div className={styles["base-item"]}>
      <div className={styles.title}>{title}</div>
      <div className={styles.period}>
        <div className={styles.date}>
          <span className={styles.start}>{startDateTime}</span>
          <span className={styles.between}></span>
          <span>{endDateTime}</span>
        </div>
      </div>
    </div>
  );
};

export default BaseItem;

BaseItem.propTypes = {
  title: PropTypes.string.isRequired,
  startDateTime: PropTypes.string.isRequired,
  endDateTime: PropTypes.string.isRequired,
};
