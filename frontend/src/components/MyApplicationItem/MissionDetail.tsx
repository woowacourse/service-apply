import classNames from "classnames";
import { useMemo } from "react";
import { TbRefresh } from "react-icons/tb";
import useTokenContext from "../../hooks/useTokenContext";
import { judgementDummy, missionsDummy } from "../../mock/dummy";
import { Mission } from "../../pages/MyApplication/MyApplication";
import HelpPanel from "../@common/HelpPanel/HelpPanel";
import styles from "./MyApplicationItem.module.css";

const Container = ({ children }: { children: React.ReactNode }) => {
  return <div className={styles["detail-container"]}>{children}</div>;
};

const TestResult = ({
  testStatus,
  passCount,
  totalCount,
  message,
  recruitmentId,
  missionId,
  handleSetMissions,
}: Pick<Mission["judgement"], "testStatus" | "passCount" | "totalCount" | "message"> & {
  recruitmentId: string;
  missionId: string;
  handleSetMissions: (missions: Record<string, Mission[]>) => void;
}) => {
  const { token } = useTokenContext();
  const handleRefreshMission = async ({
    missionId,
    recruitmentId,
    token,
  }: {
    missionId: string;
    recruitmentId: string;
    token: string;
  }) => {
    // FIXME : const response = await fetchMyMissionJudgement({ recruitmentId, missionId, token });
    const response = judgementDummy;
    const missions = missionsDummy[recruitmentId as unknown as keyof typeof missionsDummy];
    const newMissions = missions.map((mission) => {
      if (String(mission.id) === String(missionId)) {
        //@ts-ignore
        mission.judgement = response.judgement;
        return mission;
      }
      return mission;
    });
    //@ts-ignore
    missionsDummy[recruitmentId as unknown as keyof typeof missionsDummy] = newMissions;
    //@ts-ignore
    handleSetMissions(missionsDummy);
  };

  const resultText = useMemo(() => {
    switch (testStatus) {
      case "NONE":
        return <span className={classNames(styles["test-result-score"])}>없음</span>;
      case "PENDING":
        return (
          <span className={styles["pending-container"]}>
            <span className={classNames(styles["test-result-score"], styles["pending"])}>
              테스트 중
            </span>
            <TbRefresh
              className={styles["refresh-button"]}
              onClick={() => {
                handleRefreshMission({
                  recruitmentId,
                  missionId,
                  token,
                });
              }}
            />
          </span>
        );
      case "SUCCESS":
        return (
          <span
            className={classNames(
              styles["test-result-score"],
              passCount / totalCount === 1 ? styles["pass"] : styles["fail"]
            )}
          >{`${passCount} / ${totalCount}`}</span>
        );
      case "FAIL":
        return (
          <span className={classNames(styles["test-result-score"], styles["fail"])}>{message}</span>
        );
      default:
        return "";
    }
  }, [testStatus]);

  return (
    <div className={styles["test-result-text"]}>
      <span>예제 테스트 결과 : </span>
      {resultText}
    </div>
  );
};

const PullRequestAddress = ({ children: pullRequestUrl }: { children: string | null }) => {
  return (
    <div>
      <span>{`Pull request 주소 : `}</span>
      {(pullRequestUrl && <span>{pullRequestUrl}</span>) || <span> - </span>}
    </div>
  );
};

const CommitHash = ({ children: commitUrl }: { children: string | null }) => {
  return (
    <div>
      <p>
        예제 테스트 실행 Commit :&nbsp;
        {(commitUrl && (
          <a
            href={commitUrl}
            target="_blank"
            rel="noreferrer noopener"
            aria-label="GitHub Commit"
            className={styles["commit-hash"]}
            title={commitUrl}
          >
            {`@${commitUrl.split("/").slice(-1)[0].substring(0, 6)}`}
          </a>
        )) || <span> -</span>}
      </p>
    </div>
  );
};

const Guide = () => {
  return (
    <div className={styles["guide-container"]}>
      <p>테스트 코드는 3 ~ 5분이 걸릴 수 있습니다</p>
      <HelpPanel />
    </div>
  );
};

const MissionDetail = Object.assign(Container, {
  TestResult,
  PullRequestAddress,
  CommitHash,
  Guide,
});

export default MissionDetail;
