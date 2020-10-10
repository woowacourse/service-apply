import axios from "axios"
import "./interceptor"

const BASE_URL = "/api/application-forms"

export const fetchMyApplicationForms = token => {
  return axios
    .get(`${BASE_URL}/me`, {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    })
    .catch(() =>
      Promise.resolve({
        data: [
          {
            id: 1,
            referenceUrl: "https://www.google.com",
            submitted: false,
            recruitmentId: 1,
          },
          {
            id: 2,
            referenceUrl: "https://www.google.com",
            submitted: false,
            recruitmentId: 2,
          },
        ],
      }),
    )
}

export const fetchForm = ({ token, recruitmentId }) =>
  axios
    .get(`${BASE_URL}`, {
      headers: {
        Authorization: `Bearer ${token}`,
      },
      params: {
        recruitmentId,
      },
    })
    .catch(() =>
      Promise.resolve({
        data: {
          referenceUrl: "https://www.google.com",
          answers: [
            {
              contents: "스타트업을 하고 싶습니다.",
              recruitmentItemId: 1,
            },
            {
              contents: "책임감",
              recruitmentItemId: 2,
            },
          ],
        },
      }),
    )

export const createForm = ({ token, recruitmentId }) =>
  axios.post(
    `${BASE_URL}`,
    { recruitmentId },
    {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    },
  )

export const saveForm = ({ token, data }) =>
  axios.patch(`${BASE_URL}`, data, {
    headers: {
      Authorization: `Bearer ${token}`,
    },
  })
