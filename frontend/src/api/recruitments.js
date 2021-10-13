import axios from "axios";

const COMMON_PATH = "/api/recruitments";

export const fetchItems = (recruitmentId) =>
  axios.get(`${COMMON_PATH}/${recruitmentId}/items`);

export const fetchRecruitments = () => axios.get(COMMON_PATH);
