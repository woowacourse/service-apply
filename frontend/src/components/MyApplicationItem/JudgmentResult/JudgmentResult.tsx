import classNames from "classnames";
import { Mission } from "../../../../types/domains/recruitments";
import formatJudgmentResult from "./../../../utils/format/judgmentResult";
import styles from "./JudgmentResult.module.css";

export type JudgmentResultProps = {
  judgment: Mission["judgment"];
};

const JudgmentResultText = ({ judgment }: JudgmentResultProps) => {
  const { text, type } = formatJudgmentResult(judgment);

  return (
    <div className={styles["judgment-result-text"]}>
      <span>예제 테스트 결과 : </span>
      <span className={classNames(styles["judgment-result-score"], styles[type])}>{text}</span>
    </div>
  );
};

export default JudgmentResultText;
