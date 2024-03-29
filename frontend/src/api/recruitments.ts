import axios from "axios";
import {
  Assignment,
  AssignmentData,
  Mission,
  Recruitment,
  RecruitmentItem,
} from "../../types/domains/recruitments";
import { RequestWithToken, ResponseDataWithMessage } from "../../types/utility";
import { headers } from "./api";

export type FetchRecruitmentItemsRequest = number;

export type FetchRecruitmentItemsResponseData = ResponseDataWithMessage<RecruitmentItem[]>;

export type FetchRecruitmentsResponseData = ResponseDataWithMessage<Recruitment[]>;

export type FetchMyMissionsRequest = RequestWithToken<{
  recruitmentId: number;
}>;

export type FetchMyMissionJudgmentRequest = RequestWithToken<{
  recruitmentId: number;
  missionId: number;
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

export type PatchAssignmentResponseData = void;

export type PostJudgmentRequest = RequestWithToken<{
  recruitmentId: number;
  missionId: number;
}>;

export type PostJudgmentResponseData = Mission["judgment"];

export const fetchRecruitmentItems = (recruitmentId: FetchRecruitmentItemsRequest) =>
  axios.get<FetchRecruitmentItemsResponseData>(`/api/recruitments/${recruitmentId}/items`);

export const fetchRecruitments = () =>
  axios.get<FetchRecruitmentsResponseData>("/api/recruitments");

export const fetchMyMissions = ({ token, recruitmentId }: FetchMyMissionsRequest) =>
  axios.get<FetchMyMissionsResponseData>(
    `/api/recruitments/${recruitmentId}/missions/me`,
    headers({ token })
  );

export const fetchMyMissionJudgment = ({
  recruitmentId,
  missionId,
  token,
}: FetchMyMissionJudgmentRequest) =>
  axios.get(
    `/api/recruitments/${recruitmentId}/missions/${missionId}/judgments/judge-example`,
    headers({ token })
  );

export const fetchAssignment = ({ token, recruitmentId, missionId }: FetchAssignmentRequest) =>
  axios.get<FetchAssignmentResponseData>(
    `/api/recruitments/${recruitmentId}/missions/${missionId}/assignments/me`,
    headers({ token })
  );

export const postMyMissionJudgment = ({ recruitmentId, missionId, token }: PostJudgmentRequest) =>
  axios.post<PostJudgmentResponseData>(
    `/api/recruitments/${recruitmentId}/missions/${missionId}/judgments/judge-example`,
    {},
    headers({ token })
  );

export const postAssignment = ({
  recruitmentId,
  missionId,
  token,
  assignmentData,
}: PostAssignmentRequest) =>
  axios.post<PostAssignmentResponseData>(
    `/api/recruitments/${recruitmentId}/missions/${missionId}/assignments`,
    assignmentData,
    headers({ token })
  );

export const patchAssignment = ({
  recruitmentId,
  missionId,
  token,
  assignmentData,
}: PatchAssignmentRequest) =>
  axios.patch<PatchAssignmentResponseData>(
    `/api/recruitments/${recruitmentId}/missions/${missionId}/assignments`,
    assignmentData,
    headers({ token })
  );
