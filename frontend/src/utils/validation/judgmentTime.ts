import { ISO8601DateString } from "../../../types/domains/common";

export const isJudgmentTimedOut = (time: ISO8601DateString) => {
  const startedDateTime = new Date(time);
  const now = new Date();
  return now.getTime() - startedDateTime.getTime() > 300000;
};
