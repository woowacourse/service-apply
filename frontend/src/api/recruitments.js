import axios from "axios"

const BASE_URL = "/api/recruitments"

export const fetchItems = recruitmentId => axios.get(`${BASE_URL}/${recruitmentId}/items`)

export const fetchRecruitments = () =>
  axios.get(BASE_URL).catch(() =>
    Promise.resolve({
      data: [
        {
          id: 6,
          title: "웹 백엔드 3기",
          startDateTime: "2020-10-24T15:00:00",
          endDateTime: "2020-11-09T23:59:00",
          status: "RECRUITING",
        },
        {
          id: 5,
          title: "웹 프론트엔드 3기",
          startDateTime: "2020-10-24T15:00:00",
          endDateTime: "2020-11-09T23:59:00",
          status: "RECRUITING",
        },
        {
          id: 4,
          title: "모바일(iOS) 3기",
          startDateTime: "2020-10-27T15:00:00",
          endDateTime: "2020-11-09T23:59:00",
          status: "RECRUITABLE",
        },
        {
          id: 3,
          title: "모바일(Android) 3기",
          startDateTime: "2020-10-24T15:00:00",
          endDateTime: "2020-11-09T23:59:00",
          status: "UNRECRUITABLE",
        },
        {
          id: 2,
          title: "웹 백엔드 2기",
          startDateTime: "2019-10-24T15:00:00",
          endDateTime: "2019-11-09T23:59:00",
          status: "ENDED",
        },
        {
          id: 1,
          title: "웹 백엔드 1기",
          startDateTime: "2019-01-24T15:00:00",
          endDateTime: "2020-02-09T23:59:00",
          status: "ENDED",
        },
      ],
    }),
  )

export const fetchMyRecruitments = token =>
  axios
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
            title: "웹 프론트엔드 3기",
            startDateTime: "2020-09-24T15:00:00",
            endDateTime: "2020-11-09T23:59:00",
            status: "RECRUITING",
          },
          {
            id: 4,
            title: "모바일(iOS) 3기",
            startDateTime: "2020-10-27T15:00:00",
            endDateTime: "2020-11-09T23:59:00",
            status: "RECRUITABLE",
          },
          {
            id: 3,
            title: "모바일(Android) 3기",
            startDateTime: "2020-10-24T15:00:00",
            endDateTime: "2020-11-09T23:59:00",
            status: "UNRECRUITABLE",
          },
          {
            id: 2,
            title: "웹 백엔드 2기",
            startDateTime: "2019-10-24T15:00:00",
            endDateTime: "2019-11-09T23:59:00",
            status: "ENDED",
          },
        ],
      }),
    )
