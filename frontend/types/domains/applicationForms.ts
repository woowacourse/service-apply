import { APPLICATION_REGISTER_FORM_NAME } from "../../src/constants/application";
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

export type UnprocessedApplicationForm = {
  [APPLICATION_REGISTER_FORM_NAME.ANSWERS]?: string[];
  [APPLICATION_REGISTER_FORM_NAME.IS_TERM_AGREED]?: boolean;
  [APPLICATION_REGISTER_FORM_NAME.REFERENCE_URL]: string;
};
