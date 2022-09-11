import axios from "axios";
import { Answer, ApplicationForm } from "../../types/domains/applicationForms";
import { ResponseDataWithMessage, RequestWithToken } from "../../types/utility";
import { headers } from "./api";

const COMMON_PATH = "/api/application-forms";

export type FetchMyApplicationFormsRequest = string;

export type FetchMyApplicationFormsResponseData = ResponseDataWithMessage<
  { recruitmentId: number; submitted: boolean }[]
>;

export type FetchFormRequest = RequestWithToken<{ recruitmentId: string | null }>;

export type FetchFormResponseData = ResponseDataWithMessage<ApplicationForm>;

export type CreateFormRequest = RequestWithToken<{ recruitmentId: string | null }>;

export type CreateFormResponseData = ResponseDataWithMessage<ApplicationForm>;

export type UpdateFormRequest = RequestWithToken<{
  data: {
    recruitmentId: string | null;
    referenceUrl: string;
    answers: Answer[];
    submitted: boolean;
  };
}>;

export type UpdateFormResponseData = null;

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
