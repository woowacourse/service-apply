import axios from "axios"

const BASE_URL = "/api/applicants"

export const fetchToken = ({ name, phoneNumber, email, password, birthday, gender }) => {
  return axios.post(`${BASE_URL}`, {
    name,
    phoneNumber,
    email,
    password,
    birthday,
    gender,
  })
}
