import classNames from "classnames";
import { useMemo } from "react";

import { BUTTON_LABEL, Mission, Recruitment } from "../../pages/MyApplication/MyApplication";
import { formatDateTime } from "../../utils/format/date";
import ApplicationButtons from "./ApplicationButtons";
import MissionDetail from "./MissionDetail";
import styles from "./MyApplicationItem.module.css";
import RecruitmentDetail from "./RecruitmentDetail";

type ValueOf<T> = T[keyof T];

type MyApplicationItemType = {
  recruitment: Recruitment | Mission;
  buttonLabel: ValueOf<typeof BUTTON_LABEL>;
  isButtonDisabled: boolean;
  onClickButton: () => void;
  className?: string;
  isMission: boolean;
  recruitmentId?: string;
  missionId?: string;
  handleSetMissions: (missions: Record<string, Mission[]>) => void;
};

function isMissionType(recruitment: Recruitment | Mission): recruitment is Mission {
  return true;
}

const MyApplicationItem = ({
  recruitment,
  buttonLabel,
  isButtonDisabled,
  onClickButton,
  className,
  isMission,
  recruitmentId,
  missionId,
  handleSetMissions,
  ...props
}: MyApplicationItemType) => {
  const formattedStartDateTime = useMemo(
    () => (recruitment.startDateTime ? formatDateTime(new Date(recruitment.startDateTime)) : ""),
    [recruitment.startDateTime]
  );

  const formattedEndDateTime = useMemo(
    () => (recruitment.endDateTime ? formatDateTime(new Date(recruitment.endDateTime)) : ""),
    [recruitment.endDateTime]
  );

  return (
    <div className={classNames(styles["content-wrapper"], className)} {...props}>
      <div className={styles["text-container"]}>
        <RecruitmentDetail>
          <RecruitmentDetail.Title>{recruitment.title}</RecruitmentDetail.Title>
          <RecruitmentDetail.Date
            startDate={formattedStartDateTime}
            endDate={formattedEndDateTime}
          />
        </RecruitmentDetail>
        {isMission && (
          <>
            <hr className={styles["hr"]} />
            {isMissionType(recruitment) && (
              <MissionDetail>
                {isMissionType(recruitment) && (
                  <>
                    <MissionDetail.TestResult
                      {...recruitment.judgement}
                      recruitmentId={recruitmentId as string}
                      missionId={missionId as string}
                      handleSetMissions={handleSetMissions}
                    />
                    <MissionDetail.PullRequestAddress>
                      {recruitment.judgement.pullRequestUrl}
                    </MissionDetail.PullRequestAddress>
                    <MissionDetail.CommitHash>
                      {recruitment.judgement.commitUrl}
                    </MissionDetail.CommitHash>
                    <MissionDetail.Guide />
                  </>
                )}
              </MissionDetail>
            )}
          </>
        )}
        <ApplicationButtons>
          {isMission && isMissionType(recruitment) && (
            <>
              <ApplicationButtons.Judgement
                testStatus={recruitment.judgement.testStatus}
                recruitmentStatus={recruitment.status}
              />
            </>
          )}
          <ApplicationButtons.Apply isButtonDisabled={isButtonDisabled}>
            {buttonLabel}
          </ApplicationButtons.Apply>
        </ApplicationButtons>
      </div>
    </div>
  );
};

export default MyApplicationItem;
