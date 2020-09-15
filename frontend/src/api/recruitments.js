import axios from "axios"

export const fetchItems = recruitmentId => {
  return axios.get(`/api/recruitments/${recruitmentId}/items`)
}
