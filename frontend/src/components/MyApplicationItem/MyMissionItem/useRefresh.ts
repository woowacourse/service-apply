import { AxiosError } from "axios";
import { Mission, Recruitment } from "../../../../types/domains/recruitments";
import { fetchMyMissionJudgment } from "../../../api";
import { JUDGMENT_STATUS } from "../../../constants/judgment";
import { MISSION_STATUS } from "../../../constants/recruitment";
import { isJudgmentTimedOut } from "../../../utils/validation/judgmentTime";
import useTokenContext from "../../../hooks/useTokenContext";

type Props = {
  recruitmentId?: Recruitment["id"];
  missionItem: Mission;
};

const useRefresh = ({ recruitmentId, missionItem }: Props) => {
  const { token } = useTokenContext();

  const isValidMissionId = missionItem.id !== undefined && recruitmentId !== undefined;
  const isJudgmentStarted = missionItem.judgment?.status === JUDGMENT_STATUS.STARTED;
  const isMissionSubmitting = missionItem.status === MISSION_STATUS.SUBMITTING;
  const isJudgmentNotTimedOut = !isJudgmentTimedOut(missionItem.judgment);

  const isRefreshAvailable =
    isValidMissionId && isJudgmentStarted && isMissionSubmitting && isJudgmentNotTimedOut;

  const fetchRefreshedResultData = async () => {
    try {
      const response = await fetchMyMissionJudgment({
        missionId: Number(missionItem.id),
        recruitmentId: Number(recruitmentId),
        token,
      });

      return { ...missionItem, judgment: response.data };
    } catch (error) {
      throw new Error((error as AxiosError).response?.data.message);
    }
  };

  return {
    isRefreshAvailable,
    fetchRefreshedResultData,
  };
};

export default useRefresh;
