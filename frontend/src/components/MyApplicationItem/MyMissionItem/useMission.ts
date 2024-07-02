import { useEffect, useMemo, useState } from "react";
import { formatDateTime } from "../../../utils/format/date";
import useMissionJudgement from "./useMissionJudgement";
import { generatePath, useNavigate } from "react-router-dom";
import useRefresh from "./useRefresh";
import { Mission } from "../../../../types/domains/recruitments";
import { PARAM, PATH } from "../../../constants/path";
import { BUTTON_LABEL } from "../../../constants/recruitment";

type MissionProps = {
  mission: Mission;
  recruitmentId: string;
};

const missionLabel = (submitted: boolean, missionStatus: Mission["status"]) => {
  const labelMap = {
    SUBMITTABLE: BUTTON_LABEL.BEFORE_SUBMIT,
    SUBMITTING: submitted ? BUTTON_LABEL.EDIT : BUTTON_LABEL.SUBMIT,
    UNSUBMITTABLE: BUTTON_LABEL.UNSUBMITTABLE,
    ENDED: submitted ? BUTTON_LABEL.COMPLETE : BUTTON_LABEL.UNSUBMITTED,
  } as const;

  return labelMap[missionStatus];
};

const useMission = ({ mission, recruitmentId }: MissionProps) => {
  const navigate = useNavigate();

  const [missionItem, setMissionItem] = useState<Mission>({ ...mission });
  const { isRefreshAvailable, fetchRefreshedResultData } = useRefresh({
    missionItem,
    recruitmentId: Number(recruitmentId),
  });
  const { fetchJudgmentMissionResult, isJudgmentAvailable } = useMissionJudgement({
    missionItem,
    recruitmentId: Number(recruitmentId),
  });

  const applyButtonLabel = missionLabel(mission.submitted, mission.status);

  useEffect(() => {
    setMissionItem(mission);
  }, [mission]);

  const formattedStartDateTime = useMemo(
    () => (missionItem.startDateTime ? formatDateTime(new Date(missionItem.startDateTime)) : ""),
    [missionItem.startDateTime]
  );

  const formattedEndDateTime = useMemo(
    () => (missionItem.endDateTime ? formatDateTime(new Date(missionItem.endDateTime)) : ""),
    [missionItem.endDateTime]
  );

  const routeToAssignmentSubmit =
    ({ recruitmentId, mission }: { recruitmentId: string; mission: Mission }) =>
    () => {
      const isSubmitted = mission.submitted;

      navigate(
        {
          pathname: generatePath(PATH.ASSIGNMENT, {
            status: isSubmitted ? PARAM.ASSIGNMENT_STATUS.EDIT : PARAM.ASSIGNMENT_STATUS.NEW,
          }),
        },
        {
          state: {
            recruitmentId,
            currentMission: mission,
          },
        }
      );
    };

  const requestRefresh = async () => {
    try {
      const result = await fetchRefreshedResultData();
      setMissionItem(result);
    } catch (error) {
      error instanceof Error && alert(error.message);
    }
  };

  const requestMissionJudgment = async () => {
    try {
      const result = await fetchJudgmentMissionResult();

      setMissionItem(result);
    } catch (error) {
      error instanceof Error && alert(error.message);
    }
  };

  return {
    getter: {
      missionItem,
      applyButtonLabel,
      formattedStartDateTime,
      formattedEndDateTime,
      isJudgmentAvailable,
      isRefreshAvailable,
    },
    setter: {
      setMissionItem,
    },
    routeToAssignmentSubmit,
    requestRefresh,
    requestMissionJudgment,
  };
};

export default useMission;
