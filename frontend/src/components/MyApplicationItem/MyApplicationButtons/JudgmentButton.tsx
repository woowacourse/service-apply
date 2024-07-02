import classNames from "classnames";
import { Mission, Recruitment } from "../../../../types/domains/recruitments";
import Button, { BUTTON_VARIANT } from "../../@common/Button/Button";
import styles from "./ApplicationButtons.module.css";
import useMissionJudgement from "../MyMissionItem/useMissionJudgement";

type JudgmentButtonProps = {
  recruitmentId: Recruitment["id"];
  missionItem: Mission;
  setMission: React.Dispatch<React.SetStateAction<Mission>>;
};

const JudgmentButton = ({ missionItem, recruitmentId, setMission }: JudgmentButtonProps) => {
  const { handleJudgeMission, isJudgmentAvailable } = useMissionJudgement({
    missionItem,
    recruitmentId,
  });

  return (
    <Button
      className={classNames(styles["judgment-button"])}
      type="button"
      variant={BUTTON_VARIANT.CONTAINED}
      cancel={false}
      disabled={!isJudgmentAvailable}
      onClick={async () => {
        const result = await handleJudgeMission();

        if (result) {
          setMission(result);
        }
      }}
    >
      예제 테스트 실행
    </Button>
  );
};

export default JudgmentButton;
