import axios from "axios";
import { headers } from "./api";

const COMMON_PATH = "/api/recruitments";

export const fetchItems = (recruitmentId) => axios.get(`${COMMON_PATH}/${recruitmentId}/items`);

export const fetchRecruitments = () => axios.get(COMMON_PATH);

export const fetchMyMissions = ({ recruitmentId, token }) =>
  axios.get(`${COMMON_PATH}/${recruitmentId}/missions/me`, headers({ token }));

export const fetchMyMissionJudgement = ({ recruitmentId, missionId, token }) =>
  axios.get(`${COMMON_PATH}/${recruitmentId}/missions/${missionId}/judgement`, headers({ token }));

export const fetchAssignment = ({ recruitmentId, missionId, token }) =>
  axios.get(
    `${COMMON_PATH}/${recruitmentId}/missions/${missionId}/assignments/me`,
    headers({ token })
  );

export const postAssignment = ({ recruitmentId, missionId, token, assignmentData }) =>
  axios.post(
    `${COMMON_PATH}/${recruitmentId}/missions/${missionId}/assignments`,
    assignmentData,
    headers({ token })
  );

export const patchAssignment = ({ recruitmentId, missionId, token, assignmentData }) =>
  axios.patch(
    `${COMMON_PATH}/${recruitmentId}/missions/${missionId}/assignments`,
    assignmentData,
    headers({ token })
  );
