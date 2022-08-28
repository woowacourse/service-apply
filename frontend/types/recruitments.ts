import { RequestWithToken, ResponseDataWithMessage } from "./utility";

export type RecruitmentStatus = "RECRUITABLE" | "RECRUITING" | "UNRECRUITABLE" | "ENDED";

export type Recruitment = {
  id: number;
  title: string;
  term: {
    id: number;
    name: string;
  };
  recruitable: boolean;
  hidden: boolean;
  startDateTime: string;
  endDateTime: string;
  status: RecruitmentStatus;
};

export type RecruitmentItem = {
  id: number;
  recruitmentId: number;
  title: string;
  poition: number;
  maximumLength: number;
  description: string;
};

export type Assignment = {
  id: number;
  githubUsername: string;
  pullRequestUrl: string;
  note: string;
};

export type Mission = {
  id: number;
  title: string;
  description: string;
  submittable: boolean;
  submitted: boolean;
  startDateTime: string;
  endDateTime: string;
  status: RecruitmentStatus;
};

export type AssignmentData = Omit<Assignment, "id">;

export type FetchItemsRequest = number;

export type FetchItemsResponseData = ResponseDataWithMessage<RecruitmentItem[]>;

export type FetchRecruitmentsResponseData = ResponseDataWithMessage<Recruitment[]>;

export type FetchMyMissionsRequest = RequestWithToken<{
  recruitmentId: number;
}>;

export type FetchMyMissionsResponseData = ResponseDataWithMessage<Mission[]>;

export type FetchAssignmentRequest = RequestWithToken<{
  recruitmentId: number;
  missionId: number;
}>;

export type FetchAssignmentResponseData = ResponseDataWithMessage<Assignment>;

export type PostAssignmentRequest = RequestWithToken<{
  recruitmentId: number;
  missionId: number;
  assignmentData: AssignmentData;
}>;

export type PostAssignmentResponseData = ResponseDataWithMessage<Assignment>;

export type PatchAssignmentRequest = RequestWithToken<{
  recruitmentId: number;
  missionId: number;
  assignmentData: AssignmentData;
}>;

export type PatchAssignmentResponseData = never;
