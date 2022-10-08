import classNames from "classnames";
import { Mission } from "../../../../../types/domains/recruitments";
import { MY_MISSION_TOOLTIP_MESSAGE } from "../../../../constants/messages";
import { JUDGMENT_STATUS } from "./../../../../constants/judgment";
import Tooltip from "./../../../@common/Tooltip/Tooltip";
import styles from "./MissionDetail.module.css";

type ContainerProps = {
  children: React.ReactNode;
};

type TestResultProps = {
  judgment: Mission["judgment"];
};

type PullRequestUrlProps = {
  judgment: Mission["judgment"];
};

type CommitHashProps = {
  judgment: Mission["judgment"];
};

const Container = ({ children }: ContainerProps) => {
  return <div className={styles["detail-container"]}>{children}</div>;
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

const PullRequestUrl = ({ judgment }: PullRequestUrlProps) => {
  if (judgment === null) {
    return <span>{`Pull request 주소 : - `} </span>;
  }

  const pullRequestUrl = judgment.pullRequestUrl;

  return (
    <div>
      <span>{`Pull request 주소 : ${pullRequestUrl}`}</span>
    </div>
  );
};

const CommitHash = ({ judgment }: CommitHashProps) => {
  if (judgment === null) {
    return <span>{`예제 테스트 실행 Commit : - `} </span>;
  }

  const commitUrl = judgment.commitUrl;
  const shortCommitHash = `@${judgment.commitHash.slice(0, 6)}`;

  return (
    <div>
      <p>
        예제 테스트 실행 Commit :&nbsp;
        <a
          href={commitUrl}
          target="_blank"
          rel="noreferrer noopener"
          aria-label="GitHub Commit"
          className={styles["commit-hash"]}
          title={commitUrl}
        >
          {shortCommitHash}
        </a>
      </p>
    </div>
  );
};

const Guide = () => {
  return (
    <div className={styles["guide-container"]}>
      <p>테스트 코드 실행이 끝나기까지 3 ~ 5분이 걸릴 수 있습니다</p>
      <Tooltip tooltipId="auto-judgment-tooltip" messages={MY_MISSION_TOOLTIP_MESSAGE} />
    </div>
  );
};

const MissionDetail = Object.assign(Container, {
  TestResult,
  PullRequestUrl,
  CommitHash,
  Guide,
});

export default MissionDetail;
