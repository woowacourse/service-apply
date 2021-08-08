import React, { useMemo } from "react";
import PropTypes from "prop-types";

import { formatDateTime } from "../../utils/date";

import styles from "./BaseItem.module.css";

const BaseItem = ({ title, startDateTime, endDateTime }) => {
  const formattedStartDateTime = useMemo(
    () => formatDateTime(new Date(startDateTime)),
    [startDateTime]
  );
  const formattedEndDateTime = useMemo(
    () => formatDateTime(new Date(endDateTime)),
    [endDateTime]
  );

  return (
    <div className={styles["base-item"]}>
      <div className={styles.title}>{title}</div>
      <div className={styles.period}>
        <div className={styles.date}>
          <span className={styles.start}>{formattedStartDateTime}</span>
          <span className={styles.between}></span>
          <span>{formattedEndDateTime}</span>
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
