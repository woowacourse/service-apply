import CalendarIcon from "../../../assets/icon/calendar-icon.svg";
import styles from "./RecruitmentDetail.module.css";

type RecruitmentDetailProps = {
  children: string;
  startDate: string;
  endDate: string;
};

const RecruitmentDetail = ({ children, startDate, endDate }: RecruitmentDetailProps) => {
  return (
    <>
      <p className={styles.title}>
        <strong>{children}</strong>
      </p>
      <div className={styles.date}>
        <img src={CalendarIcon} alt="달력 아이콘" className={styles.icon} />
        <p>
          {startDate} ~ {endDate}
        </p>
      </div>
    </>
  );
};

export default RecruitmentDetail;
