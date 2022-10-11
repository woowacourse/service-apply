import { Mission } from "../../../../types/domains/recruitments";
import { MY_MISSION_TOOLTIP_MESSAGE } from "../../../constants/messages";
import Tooltip from "../../@common/Tooltip/Tooltip";
import CommitHash from "../CommitHash/CommitHash";
import JudgmentResultText from "../JudgmentResult/JudgmentResult";
import PullRequestUrl from "../PullRequestUrl/PullRequestUrl";
import styles from "./MissionDetail.module.css";

type MissionDetailProps = {
  judgment: Mission["judgment"];
};

const MissionDetail = ({ judgment }: MissionDetailProps) => {
  return (
    <div className={styles["detail-container"]}>
      <JudgmentResultText judgment={judgment} />
      <PullRequestUrl judgment={judgment} />
      <CommitHash judgment={judgment} />
      <div className={styles["guide-container"]}>
        <p>테스트 코드 실행이 끝나기까지 3 ~ 5분이 걸릴 수 있습니다</p>
        <Tooltip tooltipId="auto-judgment-tooltip" messages={MY_MISSION_TOOLTIP_MESSAGE} />
      </div>
    </div>
  );
};

export default MissionDetail;
