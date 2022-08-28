import axios from "axios";
import { headers } from "./api";
import * as T from "../../types/api";

const COMMON_PATH = "/api/recruitments";

export const fetchItems = (recruitmentId: T.FetchItemsRequest) =>
  axios.get<T.FetchItemsResponseData>(`${COMMON_PATH}/${recruitmentId}/items`);

export const fetchRecruitments = () => axios.get<T.FetchRecruitmentsResponseData>(COMMON_PATH);

export const fetchMyMissions = ({ token, recruitmentId }: T.FetchMyMissionsRequest) =>
  axios.get<T.FetchMyMissionsResponseData>(
    `${COMMON_PATH}/${recruitmentId}/missions/me`,
    headers({ token })
  );

export const fetchAssignment = ({ token, recruitmentId, missionId }: T.FetchAssignmentRequest) =>
  axios.get<T.FetchAssignmentResponseData>(
    `${COMMON_PATH}/${recruitmentId}/missions/${missionId}/assignments/me`,
    headers({ token })
  );

export const postAssignment = ({
  recruitmentId,
  missionId,
  token,
  assignmentData,
}: T.PostAssignmentRequest) =>
  axios.post<T.PostAssignmentResponseData>(
    `${COMMON_PATH}/${recruitmentId}/missions/${missionId}/assignmets`,
    assignmentData,
    headers({ token })
  );

export const patchAssignment = ({
  recruitmentId,
  missionId,
  token,
  assignmentData,
}: T.PatchAssignmentRequest) =>
  axios.patch<T.PatchAssignmentResponseData>(
    `${COMMON_PATH}/${recruitmentId}/missions/${missionId}/assignments`,
    assignmentData,
    headers({ token })
  );
