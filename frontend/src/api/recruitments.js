import axios from "axios"

const BASE_URL = "/api/recruitments"

export const fetchItems = recruitmentId => {
  return axios.get(`${BASE_URL}/${recruitmentId}/items`)
}

export const fetchMyRecruitments = token => {
  return axios.get(`${BASE_URL}/me`, {
    headers: {
      Authorization: `Bearer ${token}`,
    },
  })
}
