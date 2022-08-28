import { User } from "./domains/user";
import {
  Assignment,
  AssignmentData,
  Mission,
  Recruitment,
  RecruitmentItem,
} from "./domains/recruitments";
import { Answer, ApplicationForm } from "./domains/applicationForms";

export type RequestWithToken<T = {}> = { token: string } & T;

export type ResponseDataWithMessage<T> = {
  message: string;
  body: T;
};

// User 관련 API
export type FetchRegisterRequest = Omit<User, "id"> & {
  confirmPassword: string;
  authenticationCode: string;
};

export type FetchRegisterResponseData = ResponseDataWithMessage<string>;

export type FetchLoginRequest = Pick<User, "email" | "password">;

export type FetchLoginResponseData = ResponseDataWithMessage<string>;

export type FetchPasswordFindRequest = Pick<User, "name" | "email" | "password" | "birthday">;

export type FetchPasswordFindResponseData = never;

export type FetchPasswordEditRequest = RequestWithToken<{
  oldPassword: string;
  password: string;
  confirmPassword: string;
}>;

export type FetchPasswordEditResponseData = never;

export type FetchUserInfoRequest = RequestWithToken;

export type FetchUserInfoResponseData = ResponseDataWithMessage<Omit<User, "password">>;

export type FetchUserInfoEditRequest = RequestWithToken<{ phoneNumber: string }>;

export type FetchUserInfoEditResponseData = never;

export type FetchAuthenticationCodeRequest = string;

export type FetchAuthenticationCodeResponseData = never;

export type FetchVerifyAuthenticationCodeRequest = {
  email: string;
  authenticationCode: string;
};

export type FetchVerifyAuthenticationCodeResponseData = never;

// Recruitments 관련 API
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

// ApplicationForms 관련 API
export type FetchMyApplicationFormsRequest = string;

export type FetchMyApplicationFormsResponseData = ResponseDataWithMessage<
  { recruitmentId: number; submitted: boolean }[]
>;

export type FetchFormRequest = RequestWithToken<{ recruitmentId: string | null }>;

export type FetchFormResponseData = ResponseDataWithMessage<ApplicationForm>;

export type CreateFormRequest = RequestWithToken<{ recruitmentId: string | null }>;

export type CreateFormResponseData = ResponseDataWithMessage<ApplicationForm>;

export type UpdateFormRequest = RequestWithToken<{
  data: {
    recruitmentId: string | null;
    referenceUrl: string;
    answers: Answer[];
    submitted: boolean;
  };
}>;

export type UpdateFormResponseData = never;
