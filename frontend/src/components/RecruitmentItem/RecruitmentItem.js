import classNames from "classnames";
import PropTypes from "prop-types";
import { useMemo } from "react";
import CalendarIcon from "../../assets/icon/calendar-icon.svg";
import { RECRUITMENT_STATUS } from "../../constants/recruitment";
import { formatDateTime } from "../../utils/format/date";
import StatusIndicator from "../@common/StatusIndicator/StatusIndicator";
import styles from "./RecruitmentItem.module.css";

const INDICATOR_LABEL = {
  [RECRUITMENT_STATUS.RECRUITING]: "모집 중",
  [RECRUITMENT_STATUS.RECRUITABLE]: "모집 예정",
  [RECRUITMENT_STATUS.UNRECRUITABLE]: "일시 중지",
  [RECRUITMENT_STATUS.ENDED]: "모집 종료",
};

const RecruitmentItem = ({ recruitment, onClickButton, className, ...props }) => {
  const active = recruitment.status === RECRUITMENT_STATUS.RECRUITING;
  const indicatorText = INDICATOR_LABEL[recruitment.status];

  const formattedStartDateTime = useMemo(
    () => (recruitment.startDateTime ? formatDateTime(new Date(recruitment.startDateTime)) : ""),
    [recruitment.startDateTime]
  );

  const formattedEndDateTime = useMemo(
    () => (recruitment.endDateTime ? formatDateTime(new Date(recruitment.endDateTime)) : ""),
    [recruitment.endDateTime]
  );

  return (
    <div
      className={classNames(styles["content-wrapper"], className, active ? styles.active : "")}
      {...props}
      onClick={active ? onClickButton : () => {}}
    >
      <h4 className={classNames(styles.title)}>{recruitment.title}</h4>
      <div className={styles.date}>
        <img src={CalendarIcon} alt="달력 아이콘" className={styles.icon} />
        <p>
          {formattedStartDateTime} ~ {formattedEndDateTime}
        </p>
      </div>
      {indicatorText && <StatusIndicator active={active} text={indicatorText} />}
    </div>
  );
};

export default RecruitmentItem;

RecruitmentItem.propTypes = {
  recruitment: PropTypes.object.isRequired,
  onClickButton: PropTypes.func,
  className: PropTypes.string,
};
