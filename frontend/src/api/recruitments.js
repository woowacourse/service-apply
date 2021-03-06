import axios from "axios"
import "./interceptor"

const BASE_URL = "/api/recruitments"

export const fetchItems = recruitmentId => axios.get(`${BASE_URL}/${recruitmentId}/items`)

export const fetchRecruitments = () => axios.get(BASE_URL)
