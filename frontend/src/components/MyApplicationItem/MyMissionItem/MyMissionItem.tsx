import classNames from "classnames";
import { useEffect, useMemo, useState } from "react";
import { generatePath } from "react-router";
import { useNavigate } from "react-router-dom";
import { Mission } from "../../../../types/domains/recruitments";
import { PARAM } from "../../../constants/path";
import { BUTTON_LABEL, MISSION_STATUS } from "../../../constants/recruitment";
import { formatDateTime } from "../../../utils/format/date";
import MissionDetail from "../MissionDetail/MissionDetail";
import ApplyButton from "../MyApplicationButtons/ApplyButton";
import JudgmentButton from "../MyApplicationButtons/JudgmentButton";
import RefreshButton from "../MyApplicationButtons/RefreshButton";
import styles from "../MyApplicationItem.module.css";
import RecruitmentDetail from "../RecruitmentDetail/RecruitmentDetail";
import { PATH } from "./../../../constants/path";

type MyMissionItemProps = {
  mission: Mission;
  recruitmentId: string;
};

const missionLabel = (submitted: boolean, missionStatus: Mission["status"]) => {
  const labelMap = {
    SUBMITTABLE: BUTTON_LABEL.BEFORE_SUBMIT,
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
    <div className={classNames(styles["content-box"])}>
      <div className={styles["text-container"]}>
        <RecruitmentDetail startDate={formattedStartDateTime} endDate={formattedEndDateTime}>
          {missionItem.title}
        </RecruitmentDetail>

        <hr className={styles["auto-judgment-detail-contour"]} />

        <MissionDetail
          judgment={missionItem.judgment}
          submissionMethod={missionItem.submissionMethod}
        />

        <div className={styles["button-container"]}>
          <RefreshButton
            recruitmentId={Number(recruitmentId)}
            missionItem={missionItem}
            setMission={setMissionItem}
          />
          <JudgmentButton
            recruitmentId={Number(recruitmentId)}
            missionItem={missionItem}
            setMission={setMissionItem}
          />
          <ApplyButton
            isButtonDisabled={missionItem.status !== MISSION_STATUS.SUBMITTING}
            onClick={routeToAssignmentSubmit({
              recruitmentId: String(recruitmentId),
              mission,
            })}
            label={applyButtonLabel}
          />
        </div>
      </div>
    </div>
  );
};

export default MyMissionItem;
