import axios from "axios"

export const fetchToken = ({ name, phoneNumber, email, password, birthday, gender }) => {
  return axios.post("/api/applicants", {
    name,
    phoneNumber,
    email,
    password,
    birthday,
    gender,
  })
}
export const fetchLogin = data => {
  return axios.post("/api/login", { ...data })
}
