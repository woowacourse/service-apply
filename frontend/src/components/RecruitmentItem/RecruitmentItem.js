import classNames from "classnames";
import PropTypes from "prop-types";
import React, { useMemo } from "react";
import CalendarIcon from "../../assets/icon/calendar-icon.svg";
import { formatDateTime } from "../../utils/format/date";
import Button from "../@common/Button/Button";
import styles from "./RecruitmentItem.module.css";

const RecruitmentItem = ({
  recruitment,
  buttonLabel,
  isButtonDisabled,
  onClickButton,
  className,
  ...props
}) => {
  const formattedStartDateTime = useMemo(
    () => (recruitment.startDateTime ? formatDateTime(new Date(recruitment.startDateTime)) : ""),
    [recruitment.startDateTime]
  );

  const formattedEndDateTime = useMemo(
    () => (recruitment.endDateTime ? formatDateTime(new Date(recruitment.endDateTime)) : ""),
    [recruitment.endDateTime]
  );

  return (
    <div className={classNames(styles["content-wrapper"], className)} {...props}>
      <div className={styles["text-container"]}>
        <p className={styles.title}>
          <strong>{recruitment.title}</strong>
        </p>
        <div className={styles.date}>
          <img src={CalendarIcon} alt="달력 아이콘" className={styles.icon} />
          <p>
            {formattedStartDateTime} ~ {formattedEndDateTime}
          </p>
        </div>
      </div>

      {buttonLabel && (
        <Button className={styles.button} disabled={isButtonDisabled} onClick={onClickButton}>
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
  isButtonDisabled: PropTypes.bool,
  onClickButton: PropTypes.func,
  className: PropTypes.string,
};
