import axios from "axios"

export const fetchForm = ({ token, recruitmentId }) =>
  axios
    .get("/api/application-forms", {
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
      }),
    )
