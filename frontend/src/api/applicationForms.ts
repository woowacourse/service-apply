import axios from "axios";
import { Answer, ApplicationForm } from "../../types/domains/applicationForms";
import { MyApplicationType } from "../../types/domains/recruitments";
import { RequestWithToken } from "../../types/utility";
import { headers } from "./api";

export type FetchMyApplicationFormsRequest = string;

export type FetchMyApplicationFormsResponseData = MyApplicationType[];

export type FetchFormRequest = RequestWithToken<{ recruitmentId: number | null }>;

export type FetchFormResponseData = ApplicationForm;

export type FetchFormErrorResponseData = { body: unknown | null; message: string };

export type CreateFormRequest = RequestWithToken<{ recruitmentId: number | null }>;

export type CreateFormResponseData = ApplicationForm;

export type UpdateFormRequest = RequestWithToken<{
  data: {
    recruitmentId: number | null;
    referenceUrl: string;
    answers: Answer[];
    submitted: boolean;
  };
}>;

export type UpdateFormResponseData = void;

export const fetchMyApplicationForms = (token: FetchMyApplicationFormsRequest) =>
  axios.get<FetchMyApplicationFormsResponseData>("/api/application-forms/me", {
    headers: {
      Authorization: `Bearer ${token}`,
    },
  });

export const fetchForm = ({ token, recruitmentId }: FetchFormRequest) =>
  axios.get<FetchFormResponseData>("/api/application-forms", {
    ...headers({ token }),
    params: {
      recruitmentId,
    },
  });

export const createForm = ({ token, recruitmentId }: CreateFormRequest) =>
  axios.post<CreateFormResponseData>(
    "/api/application-forms",
    { recruitmentId },
    headers({ token })
  );

export const updateForm = ({ token, data }: UpdateFormRequest) =>
  axios.patch<UpdateFormResponseData>("/api/application-forms", data, headers({ token }));
