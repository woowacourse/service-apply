import axios from "axios"

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
            id: 5,
            referenceUrl: "https://www.google.com",
            submitted: false,
            answers: {
              items: [
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
          },
          {
            id: 4,
            referenceUrl: "https://www.google.com",
            submitted: false,
            answers: {
              items: [
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
          },
          {
            id: 3,
            referenceUrl: "https://www.google.com",
            submitted: true,
            answers: {
              items: [
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
          },
          {
            id: 2,
            referenceUrl: "https://www.google.com",
            submitted: true,
            answers: {
              items: [
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
          },
        ],
      }),
    )
}
