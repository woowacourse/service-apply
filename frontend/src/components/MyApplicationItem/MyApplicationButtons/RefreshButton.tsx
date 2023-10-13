import { AxiosError } from "axios";
import classNames from "classnames";
import { Mission, Recruitment } from "../../../../types/domains/recruitments";
import { fetchMyMissionJudgment } from "../../../api";
import { JUDGMENT_STATUS } from "../../../constants/judgment";
import { MISSION_STATUS } from "../../../constants/recruitment";
import useTokenContext from "../../../hooks/useTokenContext";
import Button, { BUTTON_VARIANT } from "../../@common/Button/Button";
import { isJudgmentTimedOut } from "./../../../utils/validation/judgmentTime";
import styles from "./ApplicationButtons.module.css";

type RefreshButtonProps = {
  recruitmentId?: Recruitment["id"];
  missionItem: Mission;
  setMission: React.Dispatch<React.SetStateAction<Mission>>;
};

const RefreshButton = ({ recruitmentId, missionItem, setMission }: RefreshButtonProps) => {
  const { token } = useTokenContext();
  const missionId = missionItem.id;
  const status = missionItem.judgment?.status;

  if (
    missionItem.testable === false ||
    status !== JUDGMENT_STATUS.STARTED ||
    missionItem.status !== MISSION_STATUS.SUBMITTING
  ) {
    return null;
  }

  if (missionId === undefined || recruitmentId === undefined) {
    return null;
  }

  if (isJudgmentTimedOut(missionItem.judgment)) {
    return null;
  }

  const handleRefreshMission = async ({
    missionId,
    recruitmentId,
    token,
  }: {
    missionId: string;
    recruitmentId: string;
    token: string;
  }) => {
    try {
      const response = await fetchMyMissionJudgment({
        recruitmentId: Number(recruitmentId),
        missionId: Number(missionId),
        token,
      });
      setMission({ ...missionItem, judgment: response.data });

      alert("새로고침 되었습니다");
    } catch (error) {
      alert((error as AxiosError).response?.data.message);
    }
  };

  return (
    <Button
      className={classNames(styles["refresh-button"])}
      type="button"
      variant={BUTTON_VARIANT.CONTAINED}
      cancel={false}
      onClick={() => {
        handleRefreshMission({
          missionId: String(missionId),
          recruitmentId: String(recruitmentId),
          token,
        });
      }}
    >
      새로고침
    </Button>
  );
};

export default RefreshButton;
