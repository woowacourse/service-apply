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
];

export const missionsDummy = {
  1: [
    {
      id: 1,
      title: "1차 프리코스",
      description: "설명",
      startDateTime: "2020-10-25T15:00:00",
      endDateTime: nextMonth,
      submitted: false,
      submittable: true,
      status: "SUBMITTING",
      //newly api
      isAutomation: true,
      judgement: {
        testStatus: "NONE", //'NONE' 'PENDING' "COMPLETE"
        pullRequestUrl: null,
        commitUrl: null,
        passCount: null,
        totalCount: null,
        message: null, //Pending일때만 빈문자열
      },
    },
    {
      id: 2,
      title: "2차 프리코스",
      description: "설명",
      startDateTime: "2020-10-25T15:00:00",
      endDateTime: nextMonth,
      submitted: true,
      submittable: true,
      status: "SUBMITTING",
      //newly api
      isAutomation: true,
      judgement: {
        testStatus: "PENDING", //'NONE' 'PENDING' "COMPLETE"
        pullRequestUrl: "https://github.com/woowacourse/jwp-dashboard-http/pull/298",
        commitUrl:
          "https://github.com/woowacourse/jwp-dashboard-http/pull/298/commits/6385bfdd2e9a4ae8c41cfffc1f7d8b28e79740ad",
        passCount: null,
        totalCount: null,
        message: "", //Pending일때만 빈문자열
      },
    },
    {
      id: 3,
      title: "3차 프리코스",
      description: "설명",
      startDateTime: "2020-10-25T15:00:00",
      endDateTime: nextMonth,
      submitted: true,
      submittable: true,
      status: "SUBMITTING",
      //newly api
      isAutomation: true,
      judgement: {
        testStatus: "SUCCESS", //'NONE' 'PENDING' "COMPLETE"
        pullRequestUrl: "https://github.com/woowacourse/jwp-dashboard-http/pull/298",
        commitUrl:
          "https://github.com/woowacourse/jwp-dashboard-http/pull/298/commits/6385bfdd2e9a4ae8c41cfffc1f7d8b28e79740ad",
        passCount: 1,
        totalCount: 5,
        message: "빌드에 성공했습니다", //Pending일때만 빈문자열
      },
    },
    {
      id: 4,
      title: "4차 프리코스",
      description: "설명",
      startDateTime: "2020-10-25T15:00:00",
      endDateTime: nextMonth,
      submitted: true,
      submittable: true,
      status: "SUBMITTING",
      //newly api
      isAutomation: true,
      judgement: {
        testStatus: "FAIL", //'NONE' 'PENDING' "COMPLETE"
        pullRequestUrl: "https://github.com/woowacourse/jwp-dashboard-http/pull/298",
        commitUrl:
          "https://github.com/woowacourse/jwp-dashboard-http/pull/298/commits/6385bfdd2e9a4ae8c41cfffc1f7d8b28e79740ad",
        passCount: null,
        totalCount: null,
        message: "빌드에 실패했습니다", //Pending일때만 빈문자열
      },
    },
    {
      id: 5,
      title: "5차 프리코스",
      description: "설명",
      startDateTime: "2020-10-25T15:00:00",
      endDateTime: nextMonth,
      submitted: true,
      submittable: true,
      status: "ENDED",
      //newly api
      isAutomation: true,
      judgement: {
        testStatus: "SUCCESS", //'NONE' 'PENDING' "COMPLETE"
        pullRequestUrl: "https://github.com/woowacourse/jwp-dashboard-http/pull/298",
        commitUrl:
          "https://github.com/woowacourse/jwp-dashboard-http/pull/298/commits/6385bfdd2e9a4ae8c41cfffc1f7d8b28e79740ad",
        passCount: 5,
        totalCount: 5,
        message: "빌드에 성공했습니다", //Pending일때만 빈문자열
      },
    },
  ],
  2: [
    {
      id: 6,
      title: "2-1차 프리코스",
      description: "설명",
      startDateTime: "2020-10-25T15:00:00",
      endDateTime: nextMonth,
      submitted: true,
      submittable: true,
      status: "ENDED",
      //newly api
      isAutomation: true,
      judgement: {
        testStatus: "SUCCESS", //'NONE' 'PENDING' "COMPLETE"
        pullRequestUrl: "https://github.com/woowacourse/jwp-dashboard-http/pull/298",
        commitUrl:
          "https://github.com/woowacourse/jwp-dashboard-http/pull/298/commits/6385bfdd2e9a4ae8c41cfffc1f7d8b28e79740ad",
        passCount: 5,
        totalCount: 5,
        message: "빌드에 성공했습니다", //Pending일때만 빈문자열
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

export const judgementDummy = {
  judgement: {
    testStatus: "SUCCESS", //'NONE' 'PENDING' "COMPLETE"
    pullRequestUrl: "https://github.com/woowacourse/jwp-dashboard-http/pull/298",
    commitUrl:
      "https://github.com/woowacourse/jwp-dashboard-http/pull/298/commits/6385bfdd2e9a4ae8c41cfffc1f7d8b28e79740ad",
    passCount: 5,
    totalCount: 5,
    message: "빌드에 성공했습니다", //Pending일때만 빈문자열
  },
};
