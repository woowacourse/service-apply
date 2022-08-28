import axios from "axios";
import { headers } from "./api";
import { RequestWithToken, ResponseDataWithMessage } from "../../types/utility";

const COMMON_PATH = "/api/application-forms" as const;

type Answer = {
  contents: string;
  recruitmentItemId: number;
};

type ApplicationForm = {
  id: number;
  recruitmentId: number;
  referenceUrl: string;
  submitted: boolean;
  answers: Answer[];
  createdDateTime: string;
  modifiedDateTime: string;
  submittedDateTime: string | null;
};

type FetchMyApplicationFormsRequest = string;

type FetchMyApplicationFormsResponseData = ResponseDataWithMessage<
  { recruitmentId: number; submitted: boolean }[]
>;

type FetchFormRequest = RequestWithToken<{ recruitmentId: string | null }>;

type FetchFormResponseData = ResponseDataWithMessage<ApplicationForm>;

type CreateFormRequest = RequestWithToken<{ recruitmentId: string | null }>;

type CreateFormResponseData = ResponseDataWithMessage<ApplicationForm>;

type UpdateFormRequest = RequestWithToken<{
  data: {
    recruitmentId: string | null;
    referenceUrl: string;
    answers: Answer[];
    submitted: boolean;
  };
}>;

type UpdateFormResponseData = never;

export const fetchMyApplicationForms = (token: FetchMyApplicationFormsRequest) =>
  axios.get<FetchMyApplicationFormsResponseData>(`${COMMON_PATH}/me`, {
    headers: {
      Authorization: `Bearer ${token}`,
    },
  });

export const fetchForm = ({ token, recruitmentId }: FetchFormRequest) =>
  axios.get<FetchFormResponseData>(COMMON_PATH, {
    ...headers({ token }),
    params: {
      recruitmentId,
    },
  });

export const createForm = ({ token, recruitmentId }: CreateFormRequest) =>
  axios.post<CreateFormResponseData>(COMMON_PATH, { recruitmentId }, headers({ token }));

export const updateForm = ({ token, data }: UpdateFormRequest) =>
  axios.patch<UpdateFormResponseData>(COMMON_PATH, data, headers({ token }));
