import { useMemo } from "react";
import classNames from "classnames";

import StatusIndicator from "../@common/StatusIndicator/StatusIndicator";

import { formatDateTime } from "../../utils/format/date";
import { RECRUITMENT_STATUS } from "../../constants/recruitment";
import { Recruitment } from "../../../types/domains/recruitments";
import CalendarIcon from "../../assets/icon/calendar-icon.svg";
import styles from "./RecruitmentItem.module.css";

export type RecruitmentItemProps = React.HTMLAttributes<HTMLDivElement> & {
  className?: string;
  recruitment: Recruitment;
  onClickButton?: React.MouseEventHandler<HTMLDivElement>;
};

const INDICATOR_LABEL = {
  [RECRUITMENT_STATUS.RECRUITING]: "모집 중",
  [RECRUITMENT_STATUS.RECRUITABLE]: "모집 예정",
  [RECRUITMENT_STATUS.UNRECRUITABLE]: "일시 중지",
  [RECRUITMENT_STATUS.ENDED]: "모집 종료",
} as const;

const RecruitmentItem = ({
  className,
  recruitment,
  onClickButton,
  ...props
}: RecruitmentItemProps) => {
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
      onClick={active ? onClickButton : () => {}}
      {...props}
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
