import axios from "axios";
import { headers } from "./api";
import * as T from "../../types/applicationForms";

const COMMON_PATH = "/api/application-forms" as const;

export const fetchMyApplicationForms = (token: T.FetchMyApplicationFormsRequest) =>
  axios.get<T.FetchMyApplicationFormsResponseData>(`${COMMON_PATH}/me`, {
    headers: {
      Authorization: `Bearer ${token}`,
    },
  });

export const fetchForm = ({ token, recruitmentId }: T.FetchFormRequest) =>
  axios.get<T.FetchFormResponseData>(COMMON_PATH, {
    ...headers({ token }),
    params: {
      recruitmentId,
    },
  });

export const createForm = ({ token, recruitmentId }: T.CreateFormRequest) =>
  axios.post<T.CreateFormResponseData>(COMMON_PATH, { recruitmentId }, headers({ token }));

export const updateForm = ({ token, data }: T.UpdateFormRequest) =>
  axios.patch<T.UpdateFormResponseData>(COMMON_PATH, data, headers({ token }));
