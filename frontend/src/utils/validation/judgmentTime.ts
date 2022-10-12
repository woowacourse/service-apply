import { Mission } from "../../../types/domains/recruitments";

export const isJudgmentTimedOut = (judgment: Mission["judgment"]) => {
  if (!judgment) {
    return false;
  }
  const timeoutMinute = 5;
  const timeoutMillisecond = timeoutMinute * 60 * 1000;

  const now = new Date();
  const startedDateTime = new Date(judgment.startedDateTime);

  return now.getTime() - startedDateTime.getTime() > timeoutMillisecond;
};
