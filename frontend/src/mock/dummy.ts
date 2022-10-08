import { ISO8601DateString } from "../../types/domains/common";
import { MissionStatus, TestStatus } from "../../types/domains/recruitments";

export const recruitmentDummy = [
  {
    id: 1,
    title: "우아한테크코스 1기",
    recruitable: true,
    hidden: false,
    startDateTime: "2020-10-05T10:00:00" as ISO8601DateString,
    endDateTime: "2020-11-05T10:00:00" as ISO8601DateString,
    status: "ENDED",
  },
  {
    id: 2,
    title: "우아한테크코스 2기",
    recruitable: true,
    hidden: false,
    startDateTime: "2019-10-25T10:00:00" as ISO8601DateString,
    endDateTime: "2019-11-05T10:00:00" as ISO8601DateString,
    status: "ENDED",
  },
  {
    id: 3,
    title: "우아한테크캠프 2기",
    recruitable: true,
    hidden: false,
    startDateTime: "2020-10-25T15:00:00" as ISO8601DateString,
    endDateTime: "2021-11-30T10:00:00" as ISO8601DateString,
    status: "RECRUITING",
  },
  {
    id: 4,
    title: "우아한테크캠프 Pro 3기",
    recruitable: true,
    hidden: false,
    startDateTime: "2020-10-25T15:00:00" as ISO8601DateString,
    endDateTime: "2021-11-30T10:00:00" as ISO8601DateString,
    status: "RECRUITING",
  },
  {
    id: 5,
    title: "우아한테크캠프 Pro 5기",
    recruitable: true,
    hidden: false,
    startDateTime: "2020-10-25T15:00:00" as ISO8601DateString,
    endDateTime: "2021-11-30T10:00:00" as ISO8601DateString,
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
];

export const missionsDummy = {
  1: [
    {
      id: 1,
      title: "1차 프리코스",
      description: "설명",
      startDateTime: "2020-10-25T15:00:00" as ISO8601DateString,
      endDateTime: "2020-11-25T15:00:00" as ISO8601DateString,
      submitted: false,
      submittable: true,
      status: "SUBMITTING" as MissionStatus,
      isAutomation: true,
      judgment: null,
    },
    {
      id: 2,
      title: "2차 프리코스",
      description: "설명",
      startDateTime: "2020-10-25T15:00:00" as ISO8601DateString,
      endDateTime: "2020-11-25T15:00:00" as ISO8601DateString,
      submitted: true,
      submittable: true,
      status: "SUBMITTING" as MissionStatus,
      isAutomation: true,
      judgment: {
        pullRequestUrl: "https://github.com/woowacourse/service-apply/pull/367",
        commitHash: "642951e1324eaf66914bd53df339d94cad5667e3",
        status: "STARTED" as TestStatus,
        passCount: 0,
        totalCount: 0,
        message: "",
        startedDateTime: "2022-10-04T19:36:16.621467" as ISO8601DateString,
        commitUrl:
          "https://github.com/woowacourse/service-apply/pull/367/commits/642951e1324eaf66914bd53df339d94cad5667e3",
      },
    },
    {
      id: 3,
      title: "3차 프리코스",
      description: "설명",
      startDateTime: "2020-10-25T15:00:00" as ISO8601DateString,
      endDateTime: "2020-11-25T15:00:00" as ISO8601DateString,
      submitted: true,
      submittable: true,
      status: "SUBMITTING" as MissionStatus,
      isAutomation: true,
      judgment: {
        pullRequestUrl: "https://github.com/woowacourse/service-apply/pull/367",
        commitHash: "642951e1324eaf66914bd53df339d94cad5667e3",
        status: "SUCCESS" as TestStatus,
        passCount: 1,
        totalCount: 5,
        message: "",
        startedDateTime: "2022-10-04T19:36:16.621467" as ISO8601DateString,
        commitUrl:
          "https://github.com/woowacourse/service-apply/pull/367/commits/642951e1324eaf66914bd53df339d94cad5667e3",
      },
    },
    {
      id: 4,
      title: "4차 프리코스",
      description: "설명",
      startDateTime: "2020-10-25T15:00:00" as ISO8601DateString,
      endDateTime: "2020-11-25T15:00:00" as ISO8601DateString,
      submitted: true,
      submittable: true,
      status: "SUBMITTING" as MissionStatus,
      isAutomation: true,
      judgment: {
        pullRequestUrl: "https://github.com/woowacourse/service-apply/pull/367",
        commitHash: "642951e1324eaf66914bd53df339d94cad5667e3",
        status: "FAIL" as TestStatus,
        passCount: 0,
        totalCount: 0,
        message: "빌드를 실패했습니다.",
        startedDateTime: "2022-10-04T19:36:16.621467" as ISO8601DateString,
        commitUrl:
          "https://github.com/woowacourse/service-apply/pull/367/commits/642951e1324eaf66914bd53df339d94cad5667e3",
      },
    },
    {
      id: 5,
      title: "5차 프리코스",
      description: "설명",
      startDateTime: "2020-10-25T15:00:00" as ISO8601DateString,
      endDateTime: "2020-11-25T15:00:00" as ISO8601DateString,
      submitted: true,
      submittable: true,
      status: "ENDED" as MissionStatus,
      isAutomation: true,
      judgment: {
        pullRequestUrl: "https://github.com/woowacourse/service-apply/pull/367",
        commitHash: "642951e1324eaf66914bd53df339d94cad5667e3",
        status: "SUCCESS" as TestStatus,
        passCount: 5,
        totalCount: 5,
        message: "",
        startedDateTime: "2022-10-04T19:36:16.621467" as ISO8601DateString,
        commitUrl:
          "https://github.com/woowacourse/service-apply/pull/367/commits/642951e1324eaf66914bd53df339d94cad5667e3",
      },
    },
  ],
  2: [
    {
      id: 6,
      title: "2-1차 프리코스",
      description: "설명",
      startDateTime: "2020-10-25T15:00:00" as ISO8601DateString,
      endDateTime: "2020-11-25T15:00:00" as ISO8601DateString,
      submitted: true,
      submittable: true,
      status: "ENDED" as MissionStatus,
      isAutomation: true,
      judgment: {
        pullRequestUrl: "https://github.com/woowacourse/service-apply/pull/367",
        commitHash: "642951e1324eaf66914bd53df339d94cad5667e3",
        status: "FAIL",
        passCount: 0,
        totalCount: 0,
        message: "빌드를 실패했습니다.",
        startedDateTime: "2022-10-04T19:36:16.621467" as ISO8601DateString,
        commitUrl:
          "https://github.com/woowacourse/service-apply/pull/367/commits/642951e1324eaf66914bd53df339d94cad5667e3",
      },
    },
  ],
};

export const userInfoDummy = {
  id: 1,
  name: "썬",
  email: "sun@woowa.com",
  phoneNumber: "010-1234-1234",
  gender: "FEMALE",
  birthday: "2000-01-01",
};

export const judgmentDummy = {
  pullRequestUrl: "https://github.com/woowacourse/jwp-dashboard-http/pull/298",
  commitHash: "642951e1324eaf66914bd53df339d94cad5667e3",
  status: "SUCCESS" as TestStatus,
  passCount: 5,
  totalCount: 5,
  message: "빌드에 성공했습니다",
  startedDateTime: "2022-10-04T19:36:16.621467",
  commitUrl:
    "https://github.com/woowacourse/jwp-dashboard-http/pull/298/commits/6385bfdd2e9a4ae8c41cfffc1f7d8b28e79740ad",
};
