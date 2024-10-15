import { Judgment, Mission } from "../../../../../types/domains/recruitments";
import { MISSION_STATUS } from "../../../../constants/recruitment";
import { JUDGMENT_STATUS } from "../../../../constants/judgment";
import { ISO8601DateString } from "../../../../../types/domains/common";

export function createMockMission(overrides: Partial<Mission> = {}): Mission {
  const defaultMission: Mission = {
    id: 1,
    title: "테스트 미션",
    startDateTime: "2023-01-01T00:00:00" as ISO8601DateString,
    endDateTime: "2023-12-31T23:59:59" as ISO8601DateString,
    description: "테스트 설명",
    submissionMethod: "PUBLIC_PULL_REQUEST",
    status: MISSION_STATUS.SUBMITTING,
    submitted: false,
    testable: true,
    judgment: null,
  };

  return {
    ...defaultMission,
    ...overrides,
  };
}

export function createMockJudgment(overrides: Partial<Judgment> = {}): Judgment {
  const defaultJudgment = {
    pullRequestUrl: "https://github.com/test/pr",
    commitHash: "abcdef1234567890",
    status: JUDGMENT_STATUS.STARTED,
    passCount: 0,
    totalCount: 10,
    message: "",
    startedDateTime: "2023-06-01T12:00:00" as ISO8601DateString,
    commitUrl: "https://github.com/test/commit",
    url: "https://github.com/test",
  };

  return {
    ...defaultJudgment,
    ...overrides,
  };
}
