import CalendarIcon from "../../assets/icon/calendar-icon.svg";
import styles from "./MyApplicationItem.module.css";

const Container = ({ children }: { children: React.ReactNode }) => {
  return <>{children}</>;
};

const Title = ({ children }: { children: string }) => {
  return (
    <p className={styles.title}>
      <strong>{children}</strong>
    </p>
  );
};

const Date = ({ startDate, endDate }: { startDate: string; endDate: string }) => {
  return (
    <div className={styles.date}>
      <img src={CalendarIcon} alt="달력 아이콘" className={styles.icon} />
      <p>
        {startDate} ~ {endDate}
      </p>
    </div>
  );
};

const RecruitmentDetail = Object.assign(Container, {
  Title,
  Date,
});

export default RecruitmentDetail;
