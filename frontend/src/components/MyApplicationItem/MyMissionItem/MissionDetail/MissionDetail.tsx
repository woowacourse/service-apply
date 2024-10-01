import { Mission } from "../../../../../types/domains/recruitments";
import {
  PRIVATE_REPOSITORY_TOOLTIP_MESSAGE,
  PUBLIC_PULL_REQUEST_TOOLTIP_MESSAGE,
} from "../../../../constants/messages";
import { MISSION_SUBMISSION_METHOD } from "../../../../constants/recruitment";
import Button from "../../../@common/Button/Button";
import CommitHash from "../../CommitHash/CommitHash";
import JudgmentResultText from "../../JudgmentResult/JudgmentResult";
import Tooltip from "../../../@common/Tooltip/Tooltip";
import useMission from "../../../../hooks/useMission";
import buttonStyles from "../ApplicationButtonStyles.module.css";
import styles from "./MissionDetail.module.css";
import { BUTTON_LABEL } from "../../../../constants/recruitment";
import useRefresh from "../../../../hooks/useRefresh";
import useMissionJudgment from "../../../../hooks/useMissionJudgment";

type MissionDetailProps = {
  mission: Mission;
  recruitmentId: string;
  judgment: Mission["judgment"];
};

const TOOLTIP_MESSAGE = {
  [MISSION_SUBMISSION_METHOD.PRIVATE_REPOSITORY]: PRIVATE_REPOSITORY_TOOLTIP_MESSAGE,
  [MISSION_SUBMISSION_METHOD.PUBLIC_PULL_REQUEST]: PUBLIC_PULL_REQUEST_TOOLTIP_MESSAGE,
};

const MissionDetail = ({ mission, recruitmentId, judgment }: MissionDetailProps) => {
  const { missionItem, setMissionItem } = useMission({ mission, recruitmentId });

  const { isJudgmentAvailable, fetchJudgmentMissionResult } = useMissionJudgment({
    missionItem,
    recruitmentId: Number(recruitmentId),
  });

  const { isRefreshAvailable, fetchRefreshedResultData } = useRefresh({
    missionItem,
    recruitmentId: Number(recruitmentId),
  });

  const requestRefresh = async () => {
    try {
      const result = await fetchRefreshedResultData();
      setMissionItem(result);
    } catch (error) {
      error instanceof Error && alert(error.message);
    }
  };

  const requestMissionJudgment = async () => {
    try {
      const result = await fetchJudgmentMissionResult();

      setMissionItem(result);
    } catch (error) {
      error instanceof Error && alert(error.message);
    }
  };

  return (
    <div className={styles["detail-container"]}>
      <div className={styles["detail-status-container"]}>
        <JudgmentResultText judgment={judgment} />
        <ul>
          {isRefreshAvailable && (
            <li>
              <Button className={buttonStyles["refresh-button"]} onClick={requestRefresh}>
                {BUTTON_LABEL.REFRESH}
              </Button>
            </li>
          )}
          <li>
            <Button
              className={buttonStyles["judgment-button"]}
              disabled={!isJudgmentAvailable}
              onClick={requestMissionJudgment}
            >
              {BUTTON_LABEL.JUDGMENT}
            </Button>
          </li>
        </ul>
      </div>
      <CommitHash judgment={judgment} />
      <div className={styles["guide-container"]}>
        <p>예제 테스트 실행이 완료되는 데 3~5분 정도 소요될 수 있습니다.</p>
        <Tooltip
          tooltipId="auto-judgment-tooltip"
          messages={TOOLTIP_MESSAGE[mission.submissionMethod]}
        />
      </div>
    </div>
  );
};

export default MissionDetail;
