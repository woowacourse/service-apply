import React, { useMemo } from "react";
import PropTypes from "prop-types";
import classNames from "classnames";

import CalendarIcon from "../../assets/icon/calendar-icon.svg";

import { Button } from "../form";

import { formatDateTime } from "../../utils/format/date";

import styles from "./RecruitmentItem.module.css";

const RecruitmentItem = ({
  recruitment,
  buttonLabel,
  activeButton,
  onClick,
  className,
  ...props
}) => {
  const formattedStartDateTime = useMemo(
    () =>
      recruitment.startDateTime
        ? formatDateTime(new Date(recruitment.startDateTime))
        : "",
    [recruitment.startDateTime]
  );

  const formattedEndDateTime = useMemo(
    () =>
      recruitment.endDateTime
        ? formatDateTime(new Date(recruitment.endDateTime))
        : "",
    [recruitment.endDateTime]
  );

  return (
    <div
      className={classNames(styles["content-wrapper"], className)}
      {...props}
    >
      <div className={styles["text-container"]}>
        <p className={styles.title}>
          <strong>{recruitment.title}</strong>
        </p>
        <div className={styles.date}>
          <img src={CalendarIcon} alt="달력 아이콘" />
          <p>
            {formattedStartDateTime} ~ {formattedEndDateTime}
          </p>
        </div>
      </div>

      {buttonLabel && (
        <Button disabled={!activeButton} onClick={onClick}>
          {buttonLabel}
        </Button>
      )}
    </div>
  );
};

export default RecruitmentItem;

RecruitmentItem.propTypes = {
  recruitment: PropTypes.object.isRequired,
  buttonLabel: PropTypes.string,
  activeButton: PropTypes.bool,
  onClick: PropTypes.func,
  className: PropTypes.string,
};
