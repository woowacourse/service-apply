const now = new Date();
const prevMonth = new Date().setMonth(now.getMonth() - 1);
const nextMonth = new Date().setMonth(now.getMonth() + 1);

export const recruitmentDummy = [
  {
    id: 1,
    title: "지원할 제목",
    recruitable: true,
    hidden: false,
    startDateTime: "2020-10-05T10:00:00",
    endDateTime: "2020-11-05T10:00:00",
    status: "ENDED",
  },
  {
    id: 2,
    title: "웹 백엔드 2기",
    recruitable: true,
    hidden: false,
    startDateTime: "2019-10-25T10:00:00",
    endDateTime: "2019-11-05T10:00:00",
    status: "ENDED",
  },
  {
    id: 3,
    title: "웹 프론트엔드 3기",
    recruitable: true,
    hidden: false,
    startDateTime: "2020-10-25T15:00:00",
    endDateTime: "2021-11-30T10:00:00",
    status: "RECRUITING",
  },
  {
    id: 4,
    title: "웹 프론트엔드 3기",
    recruitable: true,
    hidden: false,
    startDateTime: "2020-10-25T15:00:00",
    endDateTime: "2021-11-30T10:00:00",
    status: "RECRUITING",
  },
];

export const myApplicationDummy = [
  {
    recruitmentId: 1,
    submitted: false,
  },
  {
    recruitmentId: 2,
    submitted: true,
  },
  {
    recruitmentId: 4,
    submitted: false,
  },
];

export const missionDummy = [
  {
    id: 1,
    title: "1차 프리코스",
    startDateTime: "2020-10-25T15:00:00",
    endDateTime: nextMonth,
    submitted: false,
  },
  {
    id: 2,
    title: "2차 프리코스",
    startDateTime: "2020-10-25T15:00:00",
    endDateTime: nextMonth,
    submitted: true,
  },
  {
    id: 3,
    title: "3차 프리코스",
    startDateTime: "2020-10-25T15:00:00",
    endDateTime: prevMonth,
    submitted: true,
  },
  {
    id: 4,
    title: "4차 프리코스",
    startDateTime: "2020-10-25T15:00:00",
    endDateTime: prevMonth,
    submitted: false,
  },
];

export const userInfoDummy = {
  id: 1,
  name: "썬",
  email: "sun@woowa.com",
  phoneNumber: "010-1234-1234",
  gender: "FEMALE",
  birthday: "2000-01-01",
};
