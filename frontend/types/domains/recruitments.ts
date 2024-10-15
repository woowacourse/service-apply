import { JUDGMENT_STATUS } from "./../../src/constants/judgment";
import {
  MISSION_STATUS,
  MISSION_SUBMISSION_METHOD,
  RECRUITMENT_STATUS,
} from "./../../src/constants/recruitment";
import { ISO8601DateString } from "./common";
export type RecruitmentStatus = keyof typeof RECRUITMENT_STATUS;

export type Recruitment = {
  id: number;
  title: string;
  term: {
    id: number;
    name: string;
  };
  recruitable: boolean;
  hidden: boolean;
  startDateTime: ISO8601DateString;
  endDateTime: ISO8601DateString;
  status: RecruitmentStatus;
};

export type RecruitmentItem = {
  id: number;
  recruitmentId: number;
  title: string;
  position: number;
  maximumLength: number;
  description: string;
};

export type Mission = {
  id: number;
  title: string;
  startDateTime: ISO8601DateString;
  endDateTime: ISO8601DateString;
  description: string;
  submissionMethod: keyof typeof MISSION_SUBMISSION_METHOD;
  status: MissionStatus;
  submitted: boolean;
  testable: boolean;
  judgment: Judgment;
};

export type Judgment = {
  url: string;
  commitHash: string;
  status: JudgmentStatus;
  passCount: number;
  totalCount: number;
  message: string;
  startedDateTime: ISO8601DateString;
  commitUrl: string;
} | null;

export type Assignment = {
  id: number;
  githubUsername: string;
  url: string;
  note: string;
};

export type AssignmentData = Omit<Assignment, "id">;

export type MissionStatus = keyof typeof MISSION_STATUS;

export type JudgmentStatus = keyof typeof JUDGMENT_STATUS;

export type MyApplicationType = {
  recruitmentId: number;
  submitted: boolean;
};
