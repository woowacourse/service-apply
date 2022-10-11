import classNames from "classnames";
import { Mission } from "../../../../types/domains/recruitments";
import { JUDGMENT_STATUS } from "../../../constants/judgment";
import { isJudgmentTimedOut } from "../../../utils/validation/judgmentTime";
import styles from "./JudgmentResult.module.css";

type JudgmentResultProps = {
  judgment: Mission["judgment"];
};

const JudgmentResult = ({ judgment }: JudgmentResultProps) => {
  if (judgment === null) {
    return <span className={styles["judgment-result-text"]}>예제 테스트 결과 : 없음</span>;
  }

  if (isJudgmentTimedOut(judgment.startedDateTime)) {
    return <span className={styles["judgment-result-text"]}>예제 테스트 결과 : 시간초과</span>;
  }

  const { passCount, totalCount, message, status } = judgment;

  const resultText = () => {
    switch (status) {
      case JUDGMENT_STATUS.STARTED:
        return (
          <span className={styles["pending-container"]}>
            <span className={classNames(styles["judgment-result-score"], styles["pending"])}>
              테스트 중
            </span>
          </span>
        );
      case JUDGMENT_STATUS.SUCCESS:
        const isPass = passCount / totalCount === 1;

        return (
          <span
            className={classNames(
              styles["judgment-result-score"],
              styles[isPass ? "pass" : "fail"]
            )}
          >{`${passCount} / ${totalCount}`}</span>
        );
      case JUDGMENT_STATUS.FAIL:
        return (
          <span className={classNames(styles["judgment-result-score"], styles["fail"])}>
            {message}
          </span>
        );
      default:
        return "";
    }
  };

  return (
    <div className={styles["judgment-result-text"]}>
      <span>예제 테스트 결과 : </span>
      {resultText()}
    </div>
  );
};

export default JudgmentResult;
