import { AxiosError } from "axios";
import { Mission, MissionStatus, Recruitment } from "../../types/domains/recruitments";
import { postMyMissionJudgment } from "../api";
import useTokenContext from "./useTokenContext";
import { isJudgmentTimedOut } from "../utils/validation/judgmentTime";
import { MISSION_STATUS } from "../constants/recruitment";
import { JUDGMENT_STATUS } from "../constants/judgment";

type MissionJudgementProps = {
  missionItem: Mission;
  recruitmentId: Recruitment["id"];
};

const useMissionJudgment = ({ missionItem, recruitmentId }: MissionJudgementProps) => {
  const { token } = useTokenContext();

  const { submitted, judgment, status } = missionItem;

  const isSubmitted = submitted;
  const isJudgmentNotStartedOrTimedOut =
    judgment?.status !== JUDGMENT_STATUS.STARTED || isJudgmentTimedOut(judgment);
  const isMissionActive = status === MISSION_STATUS.SUBMITTING;

  const isJudgmentAvailable = isSubmitted && isJudgmentNotStartedOrTimedOut && isMissionActive;

  const fetchJudgmentMissionResult = async () => {
    try {
      const response = await postMyMissionJudgment({
        recruitmentId: Number(recruitmentId),
        missionId: Number(missionItem.id),
        token,
      });

      return { ...missionItem, judgment: response.data };
    } catch (error) {
      throw new Error((error as AxiosError).response?.data.message);
    }
  };

  return {
    isJudgmentAvailable,
    fetchJudgmentMissionResult,
  };
};

export default useMissionJudgment;
