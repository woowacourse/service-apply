import { Mission } from "../../../../types/domains/recruitments";
import {
  PRIVATE_REPOSITORY_TOOLTIP_MESSAGE,
  PUBLIC_PULL_REQUEST_TOOLTIP_MESSAGE,
} from "../../../constants/messages";
import { MISSION_SUBMISSION_METHOD } from "../../../constants/recruitment";
import Tooltip from "../../@common/Tooltip/Tooltip";
import CommitHash from "../CommitHash/CommitHash";
import JudgmentResultText from "../JudgmentResult/JudgmentResult";
import styles from "./MissionDetail.module.css";

type MissionDetailProps = {
  judgment: Mission["judgment"];
  submissionMethod: keyof typeof MISSION_SUBMISSION_METHOD;
};

const TOOLTIP_MESSAGE = {
  [MISSION_SUBMISSION_METHOD.PRIVATE_REPOSITORY]: PRIVATE_REPOSITORY_TOOLTIP_MESSAGE,
  [MISSION_SUBMISSION_METHOD.PUBLIC_PULL_REQUEST]: PUBLIC_PULL_REQUEST_TOOLTIP_MESSAGE,
};

const MissionDetail = ({
  judgment,
  submissionMethod = MISSION_SUBMISSION_METHOD.PUBLIC_PULL_REQUEST,
}: MissionDetailProps) => {
  return (
    <div className={styles["detail-container"]}>
      <JudgmentResultText judgment={judgment} />
      <CommitHash judgment={judgment} />
      <div className={styles["guide-container"]}>
        <p>테스트 코드 실행이 끝나기까지 3 ~ 5분이 걸릴 수 있습니다</p>
        <Tooltip tooltipId="auto-judgment-tooltip" messages={TOOLTIP_MESSAGE[submissionMethod]} />
      </div>
    </div>
  );
};

export default MissionDetail;
