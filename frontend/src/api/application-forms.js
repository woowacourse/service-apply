import axios from 'axios';
import { headers } from './api';

const COMMON_PATH = '/api/application-forms';

export const fetchMyApplicationForms = (token) =>
  axios.get(`${COMMON_PATH}/me`, {
    headers: {
      Authorization: `Bearer ${token}`,
    },
  });

export const fetchForm = ({ token, recruitmentId }) =>
  axios.get(COMMON_PATH, {
    ...headers({ token }),
    params: {
      recruitmentId,
    },
  });

export const createForm = ({ token, recruitmentId }) =>
  axios.post(COMMON_PATH, { recruitmentId }, headers({ token }));

export const updateForm = ({ token, data }) => axios.patch(COMMON_PATH, data, headers({ token }));
