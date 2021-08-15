import axios from "axios";

const BASE_URL = "/api/application-forms";

export const fetchMyApplicationForms = (token) =>
  axios.get(`${BASE_URL}/me`, {
    headers: {
      Authorization: `Bearer ${token}`,
    },
  });

export const fetchForm = ({ token, recruitmentId }) =>
  axios.get(`${BASE_URL}`, {
    headers: {
      Authorization: `Bearer ${token}`,
    },
    params: {
      recruitmentId,
    },
  });

export const createForm = ({ token, recruitmentId }) =>
  axios.post(
    `${BASE_URL}`,
    { recruitmentId },
    {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    }
  );

export const updateForm = ({ token, data }) =>
  axios.patch(`${BASE_URL}`, data, {
    headers: {
      Authorization: `Bearer ${token}`,
    },
  });
