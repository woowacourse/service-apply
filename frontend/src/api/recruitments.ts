import axios from "axios";
import { headers } from "./api";
import { RequestWithToken, ResponseDataWithMessage } from "../../types/utility";
import {
  Assignment,
  AssignmentData,
  Mission,
  Recruitment,
  RecruitmentItem,
} from "../../types/domains/recruitments";

const COMMON_PATH = "/api/recruitments";

export type FetchRecruitmentItemsRequest = number;

export type FetchRecruitmentItemsResponseData = ResponseDataWithMessage<RecruitmentItem[]>;

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

export type PatchAssignmentResponseData = null;

export const fetchRecruitmentItems = (recruitmentId: FetchRecruitmentItemsRequest) =>
  axios.get<FetchRecruitmentItemsResponseData>(`${COMMON_PATH}/${recruitmentId}/items`);

export const fetchRecruitments = () => axios.get<FetchRecruitmentsResponseData>(COMMON_PATH);

export const fetchMyMissions = ({ token, recruitmentId }: FetchMyMissionsRequest) =>
  axios.get<FetchMyMissionsResponseData>(
    `${COMMON_PATH}/${recruitmentId}/missions/me`,
    headers({ token })
  );

export const fetchAssignment = ({ token, recruitmentId, missionId }: FetchAssignmentRequest) =>
  axios.get<FetchAssignmentResponseData>(
    `${COMMON_PATH}/${recruitmentId}/missions/${missionId}/assignments/me`,
    headers({ token })
  );

export const postAssignment = ({
  recruitmentId,
  missionId,
  token,
  assignmentData,
}: PostAssignmentRequest) =>
  axios.post<PostAssignmentResponseData>(
    `${COMMON_PATH}/${recruitmentId}/missions/${missionId}/assignmets`,
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
    `${COMMON_PATH}/${recruitmentId}/missions/${missionId}/assignments`,
    assignmentData,
    headers({ token })
  );
