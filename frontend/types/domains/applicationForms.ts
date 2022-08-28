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
  createdDateTime: string;
  modifiedDateTime: string;
  submittedDateTime: string | null;
};
