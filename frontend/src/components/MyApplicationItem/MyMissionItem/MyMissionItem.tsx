import classNames from "classnames";
import { Mission } from "../../../../types/domains/recruitments";
import { BUTTON_LABEL, MISSION_STATUS } from "../../../constants/recruitment";
import MissionDetail from "../MissionDetail/MissionDetail";
import ApplyButton from "../MyApplicationButtons/ApplyButton";
import JudgmentButton from "../MyApplicationButtons/JudgmentButton";
import RefreshButton from "../MyApplicationButtons/RefreshButton";
import styles from "../MyApplicationItem.module.css";
import RecruitmentDetail from "../RecruitmentDetail/RecruitmentDetail";
import useMission from "./useMission";

type MyMissionItemProps = {
  mission: Mission;
  recruitmentId: string;
};

const MyMissionItem = ({ mission, recruitmentId }: MyMissionItemProps) => {
  const {
    getter: {
      missionItem,
      applyButtonLabel,
      formattedStartDateTime,
      formattedEndDateTime,
      isJudgmentAvailable,
      refreshAvailable,
    },
    routeToAssignmentSubmit,
    requestRefresh,
    requestMissionJudgment,
  } = useMission({ mission, recruitmentId });

  return (
    <div className={classNames(styles["content-box"])}>
      <div className={styles["content-wrapper"]}>
        <div className={styles["title-container"]}>
          <RecruitmentDetail startDate={formattedStartDateTime} endDate={formattedEndDateTime}>
            {missionItem.title}
          </RecruitmentDetail>
          <ul className={styles["title-button-list"]}>
            <li>
              <ApplyButton
                isButtonDisabled={missionItem.status !== MISSION_STATUS.SUBMITTING}
                onClick={routeToAssignmentSubmit({
                  recruitmentId: String(recruitmentId),
                  mission,
                })}
                label={applyButtonLabel}
              />
            </li>
          </ul>
        </div>

        <hr className={styles["divider"]} />

        <MissionDetail judgment={missionItem.judgment}>
          <ul>
            {refreshAvailable && (
              <li>
                <RefreshButton onClick={requestRefresh} />
              </li>
            )}
            <li>
              <JudgmentButton disabled={!isJudgmentAvailable} onClick={requestMissionJudgment} />
            </li>
          </ul>
        </MissionDetail>
      </div>
    </div>
  );
};

export default MyMissionItem;
