import { useMemo } from "react";

import { missionDummy, myApplicationDummy } from "../../mock/dummy";
import Panel from "../../components/@common/Panel/Panel";
import useRecruitmentContext from "../../hooks/useRecruitmentContext";
import styles from "./MyApplication.module.css";
import RecruitmentItem from "../../components/RecruitmentItem/RecruitmentItem";
import Container from "../../components/@common/Container/Container";

const isBeforeDeadline = (endDate) => new Date() < new Date(endDate);

const missionLabel = (submitted, isBeforeDeadline) => {
  if (isBeforeDeadline) {
    return submitted ? "수정하기" : "제출하기";
  }

  return submitted ? "제출완료" : "미제출";
};

const MyApplication = () => {
  const { recruitment } = useRecruitmentContext();

  const myRecruitments = useMemo(
    () =>
      myApplicationDummy.map(({ recruitmentId, submitted }) => ({
        ...recruitment.findById(recruitmentId),
        submitted,
      })),
    []
  );

  return (
    <div className={styles.box}>
      <Container title="내 지원 현황" className={styles["page-title"]} />
      {myRecruitments.map(({ submitted, ...recruitment }) => (
        <Panel
          key={recruitment.recruitmentId}
          title={recruitment.title}
          className={styles["recruit-panel"]}
        >
          <div className={styles["recruite-panel-inner"]}>
            <RecruitmentItem
              recruitment={{ ...recruitment, title: "내 지원서" }}
              buttonLabel={submitted ? "수정하기" : "제출완료"}
              isButtonDisabled={submitted}
            />
            <hr className={styles.hr} />
            {missionDummy.map(({ submitted, ...mission }) => (
              <RecruitmentItem
                className={styles["mission-recruit-item"]}
                recruitment={mission}
                buttonLabel={missionLabel(
                  submitted,
                  isBeforeDeadline(mission.endDateTime)
                )}
                isButtonDisabled={isBeforeDeadline(mission.endDateTime)}
              />
            ))}
          </div>
        </Panel>
      ))}
    </div>
  );
};

export default MyApplication;
