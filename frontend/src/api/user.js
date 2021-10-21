import axios from "axios";
import { headers } from "./api";
import { formatDate } from "../utils/format/date";

const COMMON_PATH = "/api/users";

export const fetchRegister = ({
  name,
  email,
  phoneNumber,
  gender,
  birthday,
  password,
  confirmPassword,
  authenticationCode,
}) =>
  axios.post(`${COMMON_PATH}/register`, {
    name,
    email,
    phoneNumber,
    gender,
    birthday: formatDate(birthday),
    password,
    confirmPassword,
    authenticationCode,
  });

export const fetchLogin = ({ email, password }) =>
  axios.post(`${COMMON_PATH}/login`, { email, password });

export const fetchPasswordFind = ({ name, email, birthday }) =>
  axios.post(`${COMMON_PATH}/reset-password`, { name, email, birthday: formatDate(birthday) });

export const fetchPasswordEdit = ({ token, oldPassword, password, confirmPassword }) =>
  axios.post(
    `${COMMON_PATH}/edit-password`,
    { oldPassword, password, confirmPassword },
    headers({ token })
  );

export const fetchUserInfo = ({ token }) => axios.get(`${COMMON_PATH}/me`, headers({ token }));

export const fetchUserInfoEdit = ({ token, phoneNumber }) =>
  axios.patch(`${COMMON_PATH}/information`, { phoneNumber }, headers({ token }));

export const fetchAuthenticationCode = (email) =>
  axios.post(`${COMMON_PATH}/authentication-code?email=${email}`);

export const fetchVerifyAuthenticationCode = ({ email, authenticationCode }) =>
  axios.post(
    `${COMMON_PATH}/authenticate-email?email=${email}&authenticationCode=${authenticationCode}`
  );
