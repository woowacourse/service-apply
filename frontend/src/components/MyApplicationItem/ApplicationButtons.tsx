import classNames from "classnames";
import { Mission } from "../../pages/MyApplication/MyApplicationType";
import Button from "../@common/Button/Button";
import styles from "./MyApplicationItem.module.css";

const Container = ({ children }: { children: React.ReactNode }) => {
  return <div className={styles["button-container"]}>{children}</div>;
};

const Judgement = ({
  testStatus,
  recruitmentStatus,
}: {
  testStatus: Mission["judgement"]["testStatus"];
  recruitmentStatus: Mission["status"];
}) => {
  if (testStatus === "NONE") {
    return null;
  }

  return (
    <Button
      className={classNames(styles["judgement-button"])}
      type="button"
      variant="contained"
      cancel={false}
      disabled={
        testStatus === "PENDING" ||
        recruitmentStatus === "ENDED" ||
        recruitmentStatus === "UNSUBMITTABLE"
      }
    >
      예제 테스트 실행
    </Button>
  );
};

const Apply = ({ children, isButtonDisabled }: { children: string; isButtonDisabled: boolean }) => {
  return (
    <Button
      className={classNames(styles["apply-button"])}
      type="button"
      variant="contained"
      cancel={false}
      disabled={isButtonDisabled} //작성, 수정하기 일 때 active, 아닐때 inactive
    >
      {children}
    </Button>
  );
};

export default Object.assign(Container, {
  Judgement,
  Apply,
});
