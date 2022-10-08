import classNames from "classnames";
import { Mission } from "../../../../types/domains/recruitments";
import { JUDGMENT_STATUS } from "../../../constants/judgment";
import styles from "./TestResult.module.css";

type TestResultProps = {
  judgment: Mission["judgment"];
};

const TestResult = ({ judgment }: TestResultProps) => {
  if (judgment === null) {
    return <span className={classNames(styles["test-result-score"])}>없음</span>;
  }

  const { passCount, totalCount, message, status } = judgment;

  const resultText = () => {
    switch (status) {
      case JUDGMENT_STATUS.STARTED:
        return (
          <span className={styles["pending-container"]}>
            <span className={classNames(styles["test-result-score"], styles["pending"])}>
              테스트 중
            </span>
          </span>
        );
      case JUDGMENT_STATUS.SUCCESS:
        const isPass = passCount / totalCount === 1;

        return (
          <span
            className={classNames(styles["test-result-score"], styles[isPass ? "pass" : "fail"])}
          >{`${passCount} / ${totalCount}`}</span>
        );
      case JUDGMENT_STATUS.FAIL:
        return (
          <span className={classNames(styles["test-result-score"], styles["fail"])}>{message}</span>
        );
      default:
        return "";
    }
  };

  return (
    <div className={styles["test-result-text"]}>
      <span>예제 테스트 결과 : </span>
      {resultText()}
    </div>
  );
};

export default TestResult;
