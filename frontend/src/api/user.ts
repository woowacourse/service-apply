import axios from "axios";
import { headers } from "./api";
import { formatDate } from "../utils/format/date";
import * as T from "../../types/user";

const COMMON_PATH = "/api/users" as const;

export const fetchRegister = ({
  name,
  email,
  phoneNumber,
  gender,
  birthday,
  password,
  confirmPassword,
  authenticationCode,
}: T.FetchRegisterRequest) =>
  axios.post<T.FetchRegisterResponseData>(`${COMMON_PATH}/register`, {
    name,
    email,
    phoneNumber,
    gender,
    birthday: formatDate(birthday),
    password,
    confirmPassword,
    authenticationCode,
  });

export const fetchLogin = ({ email, password }: T.FetchLoginRequest) =>
  axios.post<T.FetchLoginResponseData>(`${COMMON_PATH}/login`, { email, password });

export const fetchPasswordFind = ({ name, email, birthday }: T.FetchPasswordFindRequest) =>
  axios.post<T.FetchPasswordFindResponseData>(`${COMMON_PATH}/reset-password`, {
    name,
    email,
    birthday: formatDate(birthday),
  });

export const fetchPasswordEdit = ({
  token,
  oldPassword,
  password,
  confirmPassword,
}: T.FetchPasswordEditRequest) =>
  axios.post<T.FetchPasswordEditResponseData>(
    `${COMMON_PATH}/edit-password`,
    { oldPassword, password, confirmPassword },
    headers({ token })
  );

export const fetchUserInfo = ({ token }: T.FetchUserInfoRequest) =>
  axios.get<T.FetchUserInfoResponseData>(`${COMMON_PATH}/me`, headers({ token }));

export const fetchUserInfoEdit = ({ token, phoneNumber }: T.FetchUserInfoEditRequest) =>
  axios.patch<T.FetchUserInfoEditResponseData>(
    `${COMMON_PATH}/information`,
    { phoneNumber },
    headers({ token })
  );

export const fetchAuthenticationCode = (email: T.FetchAuthenticationCodeRequest) =>
  axios.post<T.FetchAuthenticationCodeResponseData>(
    `${COMMON_PATH}/authentication-code?email=${email}`
  );

export const fetchVerifyAuthenticationCode = ({
  email,
  authenticationCode,
}: T.FetchVerifyAuthenticationCodeRequest) =>
  axios.post<T.FetchVerifyAuthenticationCodeResponseData>(
    `${COMMON_PATH}/authenticate-email?email=${email}&authenticationCode=${authenticationCode}`
  );
