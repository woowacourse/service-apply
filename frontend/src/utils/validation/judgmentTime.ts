import { Mission } from "../../../types/domains/recruitments";

export const isJudgmentTimedOut = (judgment: Mission["judgment"]) => {
  if (!judgment) {
    return false;
  }

  const now = new Date();
  const startedDateTime = new Date(judgment.startedDateTime);
  return now.getTime() - startedDateTime.getTime() > 300000;
};
