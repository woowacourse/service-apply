import axios from "axios";
import { headers } from "./api";

const COMMON_PATH = "/api/users";

export const fetchRegister = ({
  name,
  phoneNumber,
  email,
  password,
  birthday,
  gender,
}) =>
  axios.post(`${COMMON_PATH}/register`, {
    name,
    phoneNumber,
    email,
    password,
    birthday,
    gender,
  });

export const fetchLogin = ({ email, password }) =>
  axios.post(`${COMMON_PATH}/login`, { email, password });

export const fetchPasswordFind = ({ name, email, birthday }) =>
  axios.post(`${COMMON_PATH}/reset-password`, { name, email, birthday });

export const fetchPasswordEdit = ({ token, password, newPassword }) =>
  axios.post(
    `${COMMON_PATH}/edit-password`,
    { password, newPassword },
    headers({ token })
  );

export const fetchInformation = ({ token }) =>
  axios.get(`${COMMON_PATH}/information`, headers({ token }));

export const fetchInformationEdit = ({ token, phoneNumber }) =>
  axios.patch(
    `${COMMON_PATH}/information`,
    { phoneNumber },
    headers({ token })
  );
