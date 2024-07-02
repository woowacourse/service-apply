import { AxiosError } from "axios";
import { Mission, Recruitment } from "../../../../types/domains/recruitments";
import { fetchMyMissionJudgment } from "../../../api";
import { JUDGMENT_STATUS } from "../../../constants/judgment";
import { MISSION_STATUS } from "../../../constants/recruitment";
import { isJudgmentTimedOut } from "../../../utils/validation/judgmentTime";

type Props = {
  recruitmentId?: Recruitment["id"];
  missionItem: Mission;
};

const useRefresh = ({ recruitmentId, missionItem }: Props) => {
  const isValidMissionId = missionItem.id && recruitmentId;

  const refreshAvailable =
    isValidMissionId &&
    missionItem.judgment?.status === JUDGMENT_STATUS.STARTED &&
    missionItem.status !== MISSION_STATUS.SUBMITTING &&
    !isJudgmentTimedOut(missionItem.judgment);

  const fetchRefreshedResultData = async ({
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

      return { ...missionItem, judgment: response.data };
    } catch (error) {
      throw new Error((error as AxiosError).response?.data.message);
    }
  };

  return {
    refreshAvailable,
    fetchRefreshedResultData,
  };
};

export default useRefresh;
