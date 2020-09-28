import axios from "axios"

const BASE_URL = "/api/applications"

export const fetchMyApplications = token => {
  return axios.get(`${BASE_URL}/me`, {
    headers: {
      Authorization: `Bearer ${token}`,
    },
  })
}
