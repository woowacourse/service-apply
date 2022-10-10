import classNames from "classnames";
import { Mission, Recruitment } from "../../../../types/domains/recruitments";
import { postMyMissionJudgment } from "../../../api/recruitments";
import { JUDGMENT_STATUS } from "../../../constants/judgment";
import { MISSION_STATUS } from "../../../constants/recruitment";
import useTokenContext from "../../../hooks/useTokenContext";
import Button, { BUTTON_VARIANT } from "../../@common/Button/Button";
import styles from "./ApplicationButtons.module.css";

type JudgmentButtonProps = {
  recruitmentId: Recruitment["id"];
  missionItem: Mission;
  setMission: React.Dispatch<React.SetStateAction<Mission>>;
};

const JudgmentButton = ({ missionItem, recruitmentId, setMission }: JudgmentButtonProps) => {
  const { token } = useTokenContext();
  const missionStatus = missionItem.status;
  const judgment = missionItem.judgment;

  const handleJudgeMission = async ({
    missionId,
    recruitmentId,
    token,
  }: {
    missionId: string;
    recruitmentId: string;
    token: string;
  }) => {
    const response = await postMyMissionJudgment({
      recruitmentId: Number(recruitmentId),
      missionId: Number(missionId),
      token,
    });
    setMission({ ...missionItem, judgment: response.data });
  };

  return (
    <Button
      className={classNames(styles["judgment-button"])}
      type="button"
      variant={BUTTON_VARIANT.CONTAINED}
      cancel={false}
      disabled={
        missionItem.submitted === false ||
        judgment?.status === JUDGMENT_STATUS.STARTED ||
        missionStatus === MISSION_STATUS.ENDED ||
        missionStatus === MISSION_STATUS.UNSUBMITTABLE
      }
      onClick={() => {
        handleJudgeMission({
          missionId: String(missionItem.id),
          recruitmentId: String(recruitmentId),
          token,
        });
      }}
    >
      예제 테스트 실행
    </Button>
  );
};

export default JudgmentButton;
