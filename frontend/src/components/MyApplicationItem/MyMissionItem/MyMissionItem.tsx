import classNames from "classnames";
import { Mission } from "../../../../types/domains/recruitments";
import { MISSION_STATUS } from "../../../constants/recruitment";
import MissionDetail from "../MissionDetail/MissionDetail";
import styles from "../MyApplicationItem.module.css";
import buttonStyles from "./ApplicationButtonStyles.module.css";
import RecruitmentDetail from "../RecruitmentDetail/RecruitmentDetail";
import useMission from "./useMission";
import Button from "../../@common/Button/Button";

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
      isRefreshAvailable,
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
              <Button
                className={buttonStyles["apply-button"]}
                disabled={missionItem.status !== MISSION_STATUS.SUBMITTING}
                onClick={routeToAssignmentSubmit({
                  recruitmentId: String(recruitmentId),
                  mission,
                })}
              >
                {applyButtonLabel}
              </Button>
            </li>
          </ul>
        </div>
        <hr className={styles["divider"]} />
        <MissionDetail judgment={missionItem.judgment}>
          <ul>
            {isRefreshAvailable && (
              <li>
                <Button className={buttonStyles["refresh-button"]} onClick={requestRefresh}>
                  새로고침
                </Button>
              </li>
            )}
            <li>
              <Button
                className={buttonStyles["judgment-button"]}
                disabled={!isJudgmentAvailable}
                onClick={requestMissionJudgment}
              >
                예제 테스트 실행
              </Button>
            </li>
          </ul>
        </MissionDetail>
      </div>
    </div>
  );
};

export default MyMissionItem;
