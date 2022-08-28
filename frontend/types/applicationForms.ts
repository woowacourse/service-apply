import { RequestWithToken, ResponseDataWithMessage } from "./utility";

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

export type FetchMyApplicationFormsRequest = string;

export type FetchMyApplicationFormsResponseData = ResponseDataWithMessage<
  { recruitmentId: number; submitted: boolean }[]
>;

export type FetchFormRequest = RequestWithToken<{ recruitmentId: string | null }>;

export type FetchFormResponseData = ResponseDataWithMessage<ApplicationForm>;

export type CreateFormRequest = RequestWithToken<{ recruitmentId: string | null }>;

export type CreateFormResponseData = ResponseDataWithMessage<ApplicationForm>;

export type UpdateFormRequest = RequestWithToken<{
  recruitmentId: string | null;
  referenceUrl: string;
  answers: Answer[];
  submitted: boolean;
}>;

export type UpdateFormResponseData = never;
