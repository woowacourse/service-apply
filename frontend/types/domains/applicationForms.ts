import { PARAM } from "../../src/constants/path";
import { ISO8601DateString } from "./common";

export type Answer = {
  contents: string;
  recruitmentItemId: number;
};

export type ApplicationForm = {
  id: number;
  recruitmentId: number;
  referenceUrl: string;
  submitted: boolean;
  answers: Answer[];
  createdDateTime: ISO8601DateString;
  modifiedDateTime: ISO8601DateString;
  submittedDateTime: ISO8601DateString | null;
};

export type ApplicationStatus = "new" | "edit";
