import axios from "axios"

export const fetchItems = recruitmentId => {
  return axios.get(`/api/recruitments/${recruitmentId}/items`)
}

export const fetchToken = ({ name, phoneNumber, email, password, birthday, gender }) => {
  return axios.post(`/api/applicants`, { name, phoneNumber, email, password, birthday, gender })
}
