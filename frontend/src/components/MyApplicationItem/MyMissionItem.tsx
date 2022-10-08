import classNames from "classnames";
import { useEffect, useMemo, useState } from "react";
import { generatePath } from "react-router";
import { useNavigate } from "react-router-dom";
import { Mission } from "../../../types/domains/recruitments";
import { PARAM } from "../../constants/path";
import { MISSION_STATUS } from "../../constants/recruitment";
import { BUTTON_LABEL } from "../../pages/MyApplication/MyApplication";
import { formatDateTime } from "../../utils/format/date";
import { PATH } from "./../../constants/path";
import ApplicationButtons from "./compounds/ApplicationButtons/ApplicationButtons";
import MissionDetail from "./compounds/MissionDetail/MissionDetail";
import RecruitmentDetail from "./compounds/RecruitmentDetail/RecruitmentDetail";
import styles from "./MyApplicationItem.module.css";

type MyMissionItemProps = {
  mission: Mission;
  recruitmentId: string;
};

const missionLabel = (submitted: boolean, missionStatus: Mission["status"]) => {
  const labelMap = {
    SUBMITTABLE: BUTTON_LABEL.BEFORE_SUBMISSION,
    SUBMITTING: submitted ? BUTTON_LABEL.EDIT : BUTTON_LABEL.SUBMIT,
    UNSUBMITTABLE: BUTTON_LABEL.UNSUBMITTABLE,
    ENDED: submitted ? BUTTON_LABEL.COMPLETE : BUTTON_LABEL.UNSUBMITTED,
  } as const;

  return labelMap[missionStatus];
};

const MyMissionItem = ({ mission, recruitmentId }: MyMissionItemProps) => {
  const navigate = useNavigate();
  const [missionItem, setMissionItem] = useState<Mission>({ ...mission });

  const applyButtonLabel = missionLabel(mission.submitted, mission.status);

  useEffect(() => {
    setMissionItem(mission);
  }, [mission]);

  const formattedStartDateTime = useMemo(
    () => (missionItem.startDateTime ? formatDateTime(new Date(missionItem.startDateTime)) : ""),
    [missionItem.startDateTime]
  );

  const formattedEndDateTime = useMemo(
    () => (missionItem.endDateTime ? formatDateTime(new Date(missionItem.endDateTime)) : ""),
    [missionItem.endDateTime]
  );

  const routeToAssignmentSubmit =
    ({ recruitmentId, mission }: { recruitmentId: string; mission: Mission }) =>
    () => {
      const isSubmitted = mission.submitted;

      navigate(
        {
          pathname: generatePath(PATH.ASSIGNMENT, {
            status: isSubmitted ? PARAM.ASSIGNMENT_STATUS.EDIT : PARAM.ASSIGNMENT_STATUS.NEW,
          }),
        },
        {
          state: {
            recruitmentId,
            currentMission: mission,
          },
        }
      );
    };

  return (
    <div className={classNames(styles["content-wrapper"], styles["mission-recruit-item"])}>
      <div className={styles["text-container"]}>
        <RecruitmentDetail>
          <RecruitmentDetail.Title>{missionItem.title}</RecruitmentDetail.Title>
          <RecruitmentDetail.Date
            startDate={formattedStartDateTime}
            endDate={formattedEndDateTime}
          />
        </RecruitmentDetail>

        <hr className={styles["auto-judgment-detail-contour"]} />

        <MissionDetail>
          <MissionDetail.TestResult judgment={missionItem.judgment} />
          <MissionDetail.PullRequestUrl judgment={missionItem.judgment} />
          <MissionDetail.CommitHash judgment={missionItem.judgment} />
          <MissionDetail.Guide />
        </MissionDetail>

        <ApplicationButtons>
          <ApplicationButtons.Refresh
            recruitmentId={Number(recruitmentId)}
            missionItem={missionItem}
            setMission={setMissionItem}
          />
          <ApplicationButtons.Judgment
            recruitmentId={Number(recruitmentId)}
            missionItem={missionItem}
            setMission={setMissionItem}
          />
          <ApplicationButtons.Apply
            isButtonDisabled={missionItem.status !== MISSION_STATUS.SUBMITTING}
            onClick={routeToAssignmentSubmit({
              recruitmentId: String(recruitmentId),
              mission,
            })}
          >
            {applyButtonLabel}
          </ApplicationButtons.Apply>
        </ApplicationButtons>
      </div>
    </div>
  );
};

export default MyMissionItem;
