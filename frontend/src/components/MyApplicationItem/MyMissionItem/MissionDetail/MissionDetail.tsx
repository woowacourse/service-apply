import { Mission } from "../../../../../types/domains/recruitments";
import { MY_MISSION_TOOLTIP_MESSAGE } from "../../../../constants/messages";
import Button from "../../../@common/Button/Button";
import CommitHash from "../../CommitHash/CommitHash";
import JudgmentResultText from "../../JudgmentResult/JudgmentResult";
import Tooltip from "../../../@common/Tooltip/Tooltip";
import useMission from "../useMission";
import buttonStyles from "../ApplicationButtonStyles.module.css";
import styles from "./MissionDetail.module.css";
import { BUTTON_LABEL } from "../../../../constants/recruitment";

type MissionDetailProps = {
  mission: Mission;
  recruitmentId: string;
  judgment: Mission["judgment"];
};

const MissionDetail = ({ mission, recruitmentId, judgment }: MissionDetailProps) => {
  const {
    getter: { isJudgmentAvailable, isRefreshAvailable },
    requestRefresh,
    requestMissionJudgment,
  } = useMission({ mission, recruitmentId });

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
        <p>테스트 코드 실행이 끝나기까지 3 ~ 5분이 걸릴 수 있습니다</p>
        <Tooltip tooltipId="auto-judgment-tooltip" messages={MY_MISSION_TOOLTIP_MESSAGE} />
      </div>
    </div>
  );
};

export default MissionDetail;
