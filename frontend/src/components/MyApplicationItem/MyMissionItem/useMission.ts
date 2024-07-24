import { useEffect, useMemo, useState } from "react";
import { formatDateTime } from "../../../utils/format/date";
import { Mission } from "../../../../types/domains/recruitments";
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
  const [missionItem, setMissionItem] = useState<Mission>({ ...mission });

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

  return {
    missionItem,
    applyButtonLabel,
    formattedStartDateTime,
    formattedEndDateTime,

    setMissionItem,
  };
};

export default useMission;
