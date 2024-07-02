import { AxiosError } from "axios";
import classNames from "classnames";
import { Mission, Recruitment } from "../../../../types/domains/recruitments";
import { fetchMyMissionJudgment } from "../../../api";
import useTokenContext from "../../../hooks/useTokenContext";
import Button, { BUTTON_VARIANT } from "../../@common/Button/Button";
import styles from "./ApplicationButtons.module.css";
import useRefresh from "../MyMissionItem/useRefresh";

type RefreshButtonProps = {
  recruitmentId?: Recruitment["id"];
  missionItem: Mission;
  setMission: React.Dispatch<React.SetStateAction<Mission>>;
};

const RefreshButton = ({ recruitmentId, missionItem, setMission }: RefreshButtonProps) => {
  const { token } = useTokenContext();
  const missionId = missionItem.id;

  const { fetchRefreshedResultData } = useRefresh({ recruitmentId, missionItem });
  const requestRefresh = async () => {
    try {
      const result = await fetchRefreshedResultData({
        missionId: String(missionId),
        recruitmentId: String(recruitmentId),
        token,
      });

      setMission(result);
    } catch (error) {
      error instanceof Error && alert(error.message);
    }
  };

  return (
    <Button
      className={classNames(styles["refresh-button"])}
      type="button"
      variant={BUTTON_VARIANT.CONTAINED}
      cancel={false}
      onClick={() => {
        requestRefresh();
      }}
    >
      새로고침
    </Button>
  );
};

export default RefreshButton;
