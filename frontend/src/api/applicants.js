import axios from "axios";
import "./interceptor";

const BASE_URL = "/api/applicants";

export const postRegister = ({
  name,
  phoneNumber,
  email,
  password,
  birthday,
  gender,
}) =>
  axios.post(`${BASE_URL}/register`, {
    name,
    phoneNumber,
    email,
    password,
    birthday,
    gender,
  });

export const fetchLogin = ({ email, password }) =>
  axios.post(`${BASE_URL}/login`, { email, password });

export const fetchPasswordFind = ({ name, email, birthday }) =>
  axios.post("/api/applicants/reset-password", { name, email, birthday });

export const fetchPasswordEdit = ({ token, password, newPassword }) =>
  axios.post(
    "/api/applicants/edit-password",
    { password, newPassword },
    {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    }
  );
