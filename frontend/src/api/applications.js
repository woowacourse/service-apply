import axios from "axios"

const BASE_URL = "/api/application-forms"

export const fetchMyApplicationForms = token => {
  return axios.get(`${BASE_URL}/me`, {
    headers: {
      Authorization: `Bearer ${token}`,
    },
  })
}
