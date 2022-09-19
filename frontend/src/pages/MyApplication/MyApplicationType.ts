import { RECRUITMENT_STATUS } from "./../../constants/recruitment";

export type MyApplicationType = {
  recruitmentId: number;
  submitted: boolean;
};

//모집에 관한 타입 ex우아한테크코스 3기
export type Recruitment = {
  id: string;
  title: string;
  recruitable: boolean;
  hidden: boolean;
  startDateTime: `${string}-${string}-${string}T${string}:${string}:${string}`;
  endDateTime: `${string}-${string}-${string}T${string}:${string}:${string}`;
  status: keyof typeof RECRUITMENT_STATUS;
};

// 내 지원서에 대한 목록
export type MyRecruitmentForm = {
  recruitmentId: number;
  submitted: boolean;
};

// 내 지원에 대한 미션의 목록 ex프리코스 3차
export type Mission = {
  id: string;
  title: string;
  description: string;
  startDateTime: `${string}-${string}-${string}T${string}:${string}:${string}`;
  endDateTime: `${string}-${string}-${string}T${string}:${string}:${string}`;
  submitted: boolean;
  submittable: boolean;
  status: "SUBMITTABLE" | "SUBMITTING" | "UNSUBMITTABLE" | "ENDED";
  isAutomation: boolean;
  judgement: {
    testStatus: "NONE" | "PENDING" | "SUCCESS" | "FAIL";
    pullRequestUrl: string;
    commitUrl: string;
    passCount: number;
    totalCount: number;
    message: string;
  };
};
