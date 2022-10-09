import { useMemo } from "react";
import classNames from "classnames";

import StatusIndicator from "../@common/StatusIndicator/StatusIndicator";

import { formatDateTime } from "../../utils/format/date";
import { RECRUITMENT_STATUS } from "../../constants/recruitment";
import { Recruitment } from "../../../types/domains/recruitments";
import CalendarIcon from "../../assets/icon/calendar-icon.svg";
import styles from "./RecruitmentItem.module.css";

const INDICATOR_LABEL = {
  [RECRUITMENT_STATUS.RECRUITING]: "모집 중",
  [RECRUITMENT_STATUS.RECRUITABLE]: "모집 예정",
  [RECRUITMENT_STATUS.UNRECRUITABLE]: "일시 중지",
  [RECRUITMENT_STATUS.ENDED]: "모집 종료",
} as const;

export type RecruitmentItemProps = React.HTMLAttributes<HTMLDivElement> & {
  className?: string;
  recruitment: Recruitment;
  onClickButton?: React.MouseEventHandler<HTMLDivElement>;
};

const RecruitmentItem = ({
  className,
  recruitment,
  onClickButton,
  ...props
}: RecruitmentItemProps) => {
  const active = recruitment.status === RECRUITMENT_STATUS.RECRUITING;
  const indicatorText = INDICATOR_LABEL[recruitment.status];

  /**
   * FIXME
   * 1. formatDateTime util 함수의 반환값이 string | Date 형식이다.
   * 2. Date로 추론되기 때문에 아래와 같은 에러 메시지가 발생한다.
   *    2-1. This JSX tag's 'children' prop expects a single child of type 'ReactNode', but multiple children were provided.
   * 3. formatDateTime util 함수의 반환값에 대해서 논의가 필요하다.
   * 4. 우선, 해당 코드에서는 string으로 타입 단언을 진행했다.
   */
  const formattedStartDateTime = useMemo(
    () => (recruitment.startDateTime ? formatDateTime(new Date(recruitment.startDateTime)) : ""),
    [recruitment.startDateTime]
  ) as string;

  const formattedEndDateTime = useMemo(
    () => (recruitment.endDateTime ? formatDateTime(new Date(recruitment.endDateTime)) : ""),
    [recruitment.endDateTime]
  ) as string;

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
