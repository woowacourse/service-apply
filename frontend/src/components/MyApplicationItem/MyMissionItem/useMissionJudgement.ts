import { AxiosError } from "axios";
import { Mission, MissionStatus, Recruitment } from "../../../../types/domains/recruitments";
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
    !([MISSION_STATUS.ENDED, MISSION_STATUS.UNSUBMITTABLE] as MissionStatus[]).includes(status);

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

export default useMissionJudgement;
