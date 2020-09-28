import axios from "axios"

export const fetchRegister = ({ name, phoneNumber, email, password, birthday, gender }) => {
  return axios.post("/api/applicants/register", {
    name,
    phoneNumber,
    email,
    password,
    birthday,
    gender,
  })
}
export const fetchLogin = ({ name, email, birthday, password }) => {
  return axios.post("/api/applicants/login", { name, email, birthday, password })
}
