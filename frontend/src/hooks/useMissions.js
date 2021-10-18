import { useState, useEffect } from "react";
import * as Api from "../api";
import useTokenContext from "./useTokenContext";

const useMissions = (recruitmentIds) => {
  const { token } = useTokenContext();

  const [missions, setMissions] = useState(null);

  const initMissions = async () => {
    const responses = await Promise.all(
      recruitmentIds.map((id) => Api.fetchMyMissions({ token, recruitmentId: id }))
    );

    const missionMap = {};
    responses.forEach((response, index) => (missionMap[recruitmentIds[index]] = response.data));

    setMissions(missionMap);
  };

  useEffect(() => {
    if (recruitmentIds.length === 0) return;

    initMissions();
  }, [recruitmentIds]);

  return { missions };
};

export default useMissions;
