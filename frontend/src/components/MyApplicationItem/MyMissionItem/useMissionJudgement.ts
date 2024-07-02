import { AxiosError } from "axios";
import { Mission, Recruitment } from "../../../../types/domains/recruitments";
import { postMyMissionJudgment } from "../../../api";
import useTokenContext from "../../../hooks/useTokenContext";
import { isJudgmentTimedOut } from "../../../utils/validation/judgmentTime";
import { MISSION_STATUS } from "../../../constants/recruitment";
import { JUDGMENT_STATUS } from "../../../constants/judgment";

type MissionJudgementProps = {
  missionItem: Mission;
  recruitmentId: Recruitment["id"];
};

const useMissionJudgement = ({ missionItem, recruitmentId }: MissionJudgementProps) => {
  const { token } = useTokenContext();

  const { submitted, judgment, status } = missionItem;
  const isJudgmentAvailable =
    submitted &&
    (judgment?.status !== JUDGMENT_STATUS.STARTED || isJudgmentTimedOut(judgment)) &&
    status !== MISSION_STATUS.ENDED &&
    status !== MISSION_STATUS.UNSUBMITTABLE;

  const handleJudgeError = async (error: AxiosError) => {
    if (!error) return;

    const errorMessage = error.response?.data.message;
    alert(errorMessage);
  };

  const handleJudgeMission = async () => {
    try {
      const response = await postMyMissionJudgment({
        recruitmentId: Number(recruitmentId),
        missionId: Number(missionItem.id),
        token,
      });

      return { ...missionItem, judgment: response.data };
    } catch (error) {
      handleJudgeError(error as AxiosError);
    }
  };

  return {
    isJudgmentAvailable,

    handleJudgeError,
    handleJudgeMission,
  };
};

export default useMissionJudgement;
