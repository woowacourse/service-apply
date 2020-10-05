import axios from "axios"

const BASE_URL = "/api/applicants"

export const fetchRegister = ({ name, phoneNumber, email, password, birthday, gender }) => {
  return axios
    .post(`${BASE_URL}/register`, {
      name,
      phoneNumber,
      email,
      password,
      birthday,
      gender,
    })
    .catch(() =>
      Promise.resolve({
        data: "token",
      }),
    )
}
export const fetchLogin = ({ name, email, birthday, password }) => {
  return axios.post(`${BASE_URL}/login`, { name, email, birthday, password }).catch(() =>
    Promise.resolve({
      data: "token",
    }),
  )
}
