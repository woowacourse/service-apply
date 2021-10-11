import axios from "axios";
import { headers } from "./api";

const BASE_URL = "/api/application-forms";

export const fetchMyApplicationForms = (token) =>
  axios.get(`${BASE_URL}/me`, {
    headers: {
      Authorization: `Bearer ${token}`,
    },
  });

export const fetchForm = ({ token, recruitmentId }) =>
  axios.get(`${BASE_URL}`, {
    ...headers({ token }),
    params: {
      recruitmentId,
    },
  });

export const createForm = ({ token, recruitmentId }) =>
  axios.post(`${BASE_URL}`, { recruitmentId }, headers({ token }));

export const updateForm = ({ token, data }) =>
  axios.patch(`${BASE_URL}`, data, headers({ token }));
