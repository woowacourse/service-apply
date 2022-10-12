import { ISO8601DateString } from "../../types/domains/common";
import { JudgmentStatus, MissionStatus } from "../../types/domains/recruitments";

const now = new Date();
const min = 3;
const afterFiveMinuteFromNow = new Date(
  now.getTime() - min * 60 * 1000 - now.getTimezoneOffset() * 60000
);
export const startedJudgeDateTime = afterFiveMinuteFromNow.toISOString().replace(/\..*/, "");

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
    title: "우아한테크코스 3기",
    recruitable: true,
    hidden: false,
    startDateTime: "2020-10-25T15:00:00" as ISO8601DateString,
    endDateTime: "2021-11-30T10:00:00" as ISO8601DateString,
    status: "RECRUITING",
  },
  {
    id: 4,
    title: "우아한테크캠프 1기",
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
    recruitmentId: 4,
    submitted: false,
  },
  {
    recruitmentId: 3,
    submitted: false,
  },
  {
    recruitmentId: 2,
    submitted: true,
  },
  {
    recruitmentId: 1,
    submitted: true,
  },
];

export const missionsDummy = {
  1: [
    {
      id: 1,
      title: "과제제출 기간 전",
      description: "설명",
      startDateTime: "2020-10-25T15:00:00" as ISO8601DateString,
      endDateTime: "2020-11-25T15:00:00" as ISO8601DateString,
      submitted: false,
      submittable: true,
      status: "SUBMITTABLE" as MissionStatus,
      isAutomation: true,
      judgment: null,
    },
  ],
  2: [
    {
      id: 2,
      title: "과제제출 시작 & 제출 전 & 실행전",
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
      id: 3,
      title: "과제제출 시작 & 제출 후 & 실행 전",
      description: "설명",
      startDateTime: "2020-10-25T15:00:00" as ISO8601DateString,
      endDateTime: "2020-11-25T15:00:00" as ISO8601DateString,
      submitted: true,
      submittable: true,
      status: "SUBMITTING" as MissionStatus,
      isAutomation: true,
      judgment: null,
    },
    {
      id: 4,
      title: "과제제출 시작 & 제출 후 & STARTED & 5분전",
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
        status: "STARTED" as JudgmentStatus,
        passCount: 0,
        totalCount: 0,
        message: "빌드를 실패했습니다.",
        startedDateTime: startedJudgeDateTime as ISO8601DateString,
        commitUrl:
          "https://github.com/woowacourse/service-apply/pull/367/commits/642951e1324eaf66914bd53df339d94cad5667e3",
      },
    },
    {
      id: 5,
      title: "과제제출 시작 & 제출 후 & STARTED & 5분 후",
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
        status: "SUCCEEDED" as JudgmentStatus,
        passCount: 5,
        totalCount: 5,
        message: "",
        startedDateTime: "2022-10-04T19:36:16.621467" as ISO8601DateString,
        commitUrl:
          "https://github.com/woowacourse/service-apply/pull/367/commits/642951e1324eaf66914bd53df339d94cad5667e3",
      },
    },
    {
      id: 6,
      title: "과제제출 시작 & 제출 후 & 완료(전체케이스통과)",
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
        status: "SUCCEEDED" as JudgmentStatus,
        passCount: 5,
        totalCount: 5,
        message: "",
        startedDateTime: startedJudgeDateTime as ISO8601DateString,
        commitUrl:
          "https://github.com/woowacourse/service-apply/pull/367/commits/642951e1324eaf66914bd53df339d94cad5667e3",
      },
    },
    {
      id: 7,
      title: "과제제출 시작 & 제출 후 & 완료(실패 케이스 존재)",
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
        status: "SUCCEEDED" as JudgmentStatus,
        passCount: 4,
        totalCount: 5,
        message: "",
        startedDateTime: startedJudgeDateTime as ISO8601DateString,
        commitUrl:
          "https://github.com/woowacourse/service-apply/pull/367/commits/642951e1324eaf66914bd53df339d94cad5667e3",
      },
    },
    {
      id: 8,
      title: "과제제출 시작 & 제출 후 & 완료(빌드실패)",
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
        status: "FAILED" as JudgmentStatus,
        passCount: 0,
        totalCount: 0,
        message: "빌드에 실패했습니다",
        startedDateTime: startedJudgeDateTime as ISO8601DateString,
        commitUrl:
          "https://github.com/woowacourse/service-apply/pull/367/commits/642951e1324eaf66914bd53df339d94cad5667e3",
      },
    },
  ],
  3: [
    {
      id: 9,
      title: "과제제출 마감 후 & 제출안함",
      description: "설명",
      startDateTime: "2020-10-25T15:00:00" as ISO8601DateString,
      endDateTime: "2020-11-25T15:00:00" as ISO8601DateString,
      submitted: false,
      submittable: false,
      status: "ENDED" as MissionStatus,
      isAutomation: true,
      judgment: null,
    },
    {
      id: 10,
      title: "과제제출 마감 후 & 제출함 & 예제 테스트 실행안함",
      description: "설명",
      startDateTime: "2020-10-25T15:00:00" as ISO8601DateString,
      endDateTime: "2020-11-25T15:00:00" as ISO8601DateString,
      submitted: true,
      submittable: false,
      status: "ENDED" as MissionStatus,
      isAutomation: true,
      judgment: null,
    },
    {
      id: 11,
      title: "과제제출 마감 후 & 제출함 & STARTED & 5분전",
      description: "설명",
      startDateTime: "2020-10-25T15:00:00" as ISO8601DateString,
      endDateTime: "2020-11-25T15:00:00" as ISO8601DateString,
      submitted: true,
      submittable: false,
      status: "ENDED" as MissionStatus,
      isAutomation: true,
      judgment: {
        pullRequestUrl: "https://github.com/woowacourse/service-apply/pull/367",
        commitHash: "642951e1324eaf66914bd53df339d94cad5667e3",
        status: "STARTED",
        passCount: 0,
        totalCount: 0,
        message: "",
        startedDateTime: startedJudgeDateTime as ISO8601DateString,
        commitUrl:
          "https://github.com/woowacourse/service-apply/pull/367/commits/642951e1324eaf66914bd53df339d94cad5667e3",
      },
    },
    {
      id: 12,
      title: "과제제출 마감 후 & 제출함 & STARTED & 5분후",
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
        status: "STARTED" as JudgmentStatus,
        passCount: 0,
        totalCount: 0,
        message: "",
        startedDateTime: "2022-10-04T19:36:16.621467" as ISO8601DateString,
        commitUrl:
          "https://github.com/woowacourse/service-apply/pull/367/commits/642951e1324eaf66914bd53df339d94cad5667e3",
      },
    },
    {
      id: 13,
      title: "과제제출 마감 후 & 제출함 & 완료(전체케이스통과)",
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
        status: "SUCCEEDED" as JudgmentStatus,
        passCount: 5,
        totalCount: 5,
        message: "빌드를 실패했습니다.",
        startedDateTime: startedJudgeDateTime as ISO8601DateString,
        commitUrl:
          "https://github.com/woowacourse/service-apply/pull/367/commits/642951e1324eaf66914bd53df339d94cad5667e3",
      },
    },
    {
      id: 14,
      title: "과제제출 마감 후 & 제출함 & 완료(실패케이스있음)",
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
        status: "SUCCEEDED" as JudgmentStatus,
        passCount: 4,
        totalCount: 5,
        message: "빌드를 실패했습니다.",
        startedDateTime: startedJudgeDateTime as ISO8601DateString,
        commitUrl:
          "https://github.com/woowacourse/service-apply/pull/367/commits/642951e1324eaf66914bd53df339d94cad5667e3",
      },
    },
    {
      id: 15,
      title: "과제제출 마감 후 & 제출함 & 완료(빌드실패)",
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
        status: "FAILED" as JudgmentStatus,
        passCount: 0,
        totalCount: 0,
        message: "빌드를 실패했습니다.",
        startedDateTime: startedJudgeDateTime as ISO8601DateString,
        commitUrl:
          "https://github.com/woowacourse/service-apply/pull/367/commits/642951e1324eaf66914bd53df339d94cad5667e3",
      },
    },
  ],
  4: [
    {
      id: 16,
      title: "과제제출 중지 & 제출안함",
      description: "설명",
      startDateTime: "2020-10-25T15:00:00" as ISO8601DateString,
      endDateTime: "2020-11-25T15:00:00" as ISO8601DateString,
      submitted: false,
      submittable: false,
      status: "UNSUBMITTABLE" as MissionStatus,
      isAutomation: true,
      judgment: null,
    },
    {
      id: 17,
      title: "과제제출 중지 & 제출함 & 예제 테스트 실행안함",
      description: "설명",
      startDateTime: "2020-10-25T15:00:00" as ISO8601DateString,
      endDateTime: "2020-11-25T15:00:00" as ISO8601DateString,
      submitted: true,
      submittable: false,
      status: "UNSUBMITTABLE" as MissionStatus,
      isAutomation: true,
      judgment: null,
    },
    {
      id: 18,
      title: "과제제출 중지 & 제출함 & STARTED & 5분전",
      description: "설명",
      startDateTime: "2020-10-25T15:00:00" as ISO8601DateString,
      endDateTime: "2020-11-25T15:00:00" as ISO8601DateString,
      submitted: true,
      submittable: false,
      status: "UNSUBMITTABLE" as MissionStatus,
      isAutomation: true,
      judgment: {
        pullRequestUrl: "https://github.com/woowacourse/service-apply/pull/367",
        commitHash: "642951e1324eaf66914bd53df339d94cad5667e3",
        status: "STARTED",
        passCount: 0,
        totalCount: 0,
        message: "",
        startedDateTime: startedJudgeDateTime as ISO8601DateString,
        commitUrl:
          "https://github.com/woowacourse/service-apply/pull/367/commits/642951e1324eaf66914bd53df339d94cad5667e3",
      },
    },
    {
      id: 19,
      title: "과제제출 중지 & 제출함 & STARTED & 5분후",
      description: "설명",
      startDateTime: "2020-10-25T15:00:00" as ISO8601DateString,
      endDateTime: "2020-11-25T15:00:00" as ISO8601DateString,
      submitted: true,
      submittable: true,
      status: "UNSUBMITTABLE" as MissionStatus,
      isAutomation: true,
      judgment: {
        pullRequestUrl: "https://github.com/woowacourse/service-apply/pull/367",
        commitHash: "642951e1324eaf66914bd53df339d94cad5667e3",
        status: "STARTED" as JudgmentStatus,
        passCount: 0,
        totalCount: 0,
        message: "",
        startedDateTime: "2022-10-04T19:36:16.621467" as ISO8601DateString,
        commitUrl:
          "https://github.com/woowacourse/service-apply/pull/367/commits/642951e1324eaf66914bd53df339d94cad5667e3",
      },
    },
    {
      id: 20,
      title: "과제제출 중지 & 제출함 & 완료(전체케이스통과)",
      description: "설명",
      startDateTime: "2020-10-25T15:00:00" as ISO8601DateString,
      endDateTime: "2020-11-25T15:00:00" as ISO8601DateString,
      submitted: true,
      submittable: true,
      status: "UNSUBMITTABLE" as MissionStatus,
      isAutomation: true,
      judgment: {
        pullRequestUrl: "https://github.com/woowacourse/service-apply/pull/367",
        commitHash: "642951e1324eaf66914bd53df339d94cad5667e3",
        status: "SUCCEEDED" as JudgmentStatus,
        passCount: 5,
        totalCount: 5,
        message: "빌드를 실패했습니다.",
        startedDateTime: startedJudgeDateTime as ISO8601DateString,
        commitUrl:
          "https://github.com/woowacourse/service-apply/pull/367/commits/642951e1324eaf66914bd53df339d94cad5667e3",
      },
    },
    {
      id: 21,
      title: "과제제출 중지 & 제출함 & 완료(실패케이스있음)",
      description: "설명",
      startDateTime: "2020-10-25T15:00:00" as ISO8601DateString,
      endDateTime: "2020-11-25T15:00:00" as ISO8601DateString,
      submitted: true,
      submittable: true,
      status: "UNSUBMITTABLE" as MissionStatus,
      isAutomation: true,
      judgment: {
        pullRequestUrl: "https://github.com/woowacourse/service-apply/pull/367",
        commitHash: "642951e1324eaf66914bd53df339d94cad5667e3",
        status: "SUCCEEDED" as JudgmentStatus,
        passCount: 4,
        totalCount: 5,
        message: "빌드를 실패했습니다.",
        startedDateTime: startedJudgeDateTime as ISO8601DateString,
        commitUrl:
          "https://github.com/woowacourse/service-apply/pull/367/commits/642951e1324eaf66914bd53df339d94cad5667e3",
      },
    },
    {
      id: 22,
      title: "과제제출 중지 & 제출함 & 완료(빌드실패)",
      description: "설명",
      startDateTime: "2020-10-25T15:00:00" as ISO8601DateString,
      endDateTime: "2020-11-25T15:00:00" as ISO8601DateString,
      submitted: true,
      submittable: true,
      status: "UNSUBMITTABLE" as MissionStatus,
      isAutomation: true,
      judgment: {
        pullRequestUrl: "https://github.com/woowacourse/service-apply/pull/367",
        commitHash: "642951e1324eaf66914bd53df339d94cad5667e3",
        status: "FAILED" as JudgmentStatus,
        passCount: 0,
        totalCount: 0,
        message: "빌드를 실패했습니다.",
        startedDateTime: startedJudgeDateTime as ISO8601DateString,
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
  status: "SUCCEEDED" as JudgmentStatus,
  passCount: 5,
  totalCount: 5,
  message: "빌드에 성공했습니다",
  startedDateTime: "2022-10-04T19:36:16.621467",
  commitUrl:
    "https://github.com/woowacourse/jwp-dashboard-http/pull/298/commits/6385bfdd2e9a4ae8c41cfffc1f7d8b28e79740ad",
};
