import axios from "axios";
import { headers } from "./api";
import { RequestWithToken, ResponseDataWithMessage } from "../../types/utility";

const COMMON_PATH = "/api/recruitments" as const;

type RecruitmentStatus = "RECRUITABLE" | "RECRUITING" | "UNRECRUITABLE" | "ENDED";

type RecruitmentItem = {
  id: number;
  recruitmentId: number;
  title: string;
  poition: number;
  maximumLength: number;
  description: string;
};

type Assignment = {
  id: number;
  githubUsername: string;
  pullRequestUrl: string;
  note: string;
};

type AssignmentData = Omit<Assignment, "id">;

type FetchItemsRequest = number;

type FetchItemsResponseData = ResponseDataWithMessage<RecruitmentItem[]>;

type FetchRecruitmentsResponseData = ResponseDataWithMessage<
  {
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
  }[]
>;

type FetchMyMissionsRequest = RequestWithToken<{
  recruitmentId: number;
}>;

type FetchMyMissionsResponseData = ResponseDataWithMessage<
  {
    id: number;
    title: string;
    description: string;
    submittable: boolean;
    submitted: boolean;
    startDateTime: string;
    endDateTime: string;
    status: RecruitmentStatus;
  }[]
>;

type FetchAssignmentRequest = RequestWithToken<{
  recruitmentId: number;
  missionId: number;
}>;

type FetchAssignmentResponseData = ResponseDataWithMessage<Assignment>;

type PostAssignmentRequest = RequestWithToken<{
  recruitmentId: number;
  missionId: number;
  assignmentData: AssignmentData;
}>;

type PostAssignmentResponseData = ResponseDataWithMessage<Assignment>;

type PatchAssignmentRequest = RequestWithToken<{
  recruitmentId: number;
  missionId: number;
  assignmentData: AssignmentData;
}>;

type PatchAssignmentResponseData = never;

export const fetchItems = (recruitmentId: FetchItemsRequest) =>
  axios.get<FetchItemsResponseData>(`${COMMON_PATH}/${recruitmentId}/items`);

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
