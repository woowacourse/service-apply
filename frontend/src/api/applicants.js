import axios from "axios"
import "./interceptor"

const BASE_URL = "/api/applicants"

export const fetchRegister = ({ name, phoneNumber, email, password, birthday, gender }) =>
  axios.post(`${BASE_URL}/register`, {
    name,
    phoneNumber,
    email,
    password,
    birthday,
    gender,
  })

export const fetchLogin = ({ name, email, birthday, password }) =>
  axios.post(`${BASE_URL}/login`, { name, email, birthday, password })

export const fetchPasswordFind = ({ name, email, birthday }) =>
  axios.post("/api/applicants/reset-password", { name, email, birthday })

export const fetchPasswordEdit = ({ token, beforePassword, newPassword }) =>
  axios.post(
    "/api/applicants/edit-password",
    { beforePassword, newPassword },
    {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    },
  )
