import axios from "axios";
import { headers } from "./api";

const COMMON_PATH = "/api/recruitments";

export const fetchItems = (recruitmentId) =>
  axios.get(`${COMMON_PATH}/${recruitmentId}/items`);

export const fetchRecruitments = () => axios.get(COMMON_PATH);

export const fetchMyMissions = ({ recruitmentId, token }) =>
  axios.get(`${COMMON_PATH}/${recruitmentId}/missions/me`, headers({ token }));
