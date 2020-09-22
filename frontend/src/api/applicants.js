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
export const fetchLogin = data => {
  return axios.post("/api/applicants/login", { ...data })
}
