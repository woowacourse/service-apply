import axios from "axios"

export const fetchToken = ({
  name,
  phoneNumber,
  email,
  password,
  birthday,
  gender,
  recruitmentId,
}) => {
  return axios.post(`/api/applicants/${recruitmentId}`, {
    name,
    phoneNumber,
    email,
    password,
    birthday,
    gender,
  })
}
