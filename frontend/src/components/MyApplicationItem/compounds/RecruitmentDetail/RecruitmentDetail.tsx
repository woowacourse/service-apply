import CalendarIcon from "../../../../assets/icon/calendar-icon.svg";
import styles from "./RecruitmentDetail.module.css";

type ContainerProps = {
  children: React.ReactNode;
};

type TitleProps = {
  children: string;
};

type DateProps = {
  startDate: string;
  endDate: string;
};

const Container = ({ children }: ContainerProps) => {
  return <>{children}</>;
};

const Title = ({ children }: TitleProps) => {
  return (
    <p className={styles.title}>
      <strong>{children}</strong>
    </p>
  );
};

const Date = ({ startDate, endDate }: DateProps) => {
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
