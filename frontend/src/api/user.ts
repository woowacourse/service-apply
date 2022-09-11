import axios from "axios";
import { headers } from "./api";
import { formatDate } from "../utils/format/date";
import { RequestWithToken, ResponseDataWithMessage } from "../../types/utility";
import { User } from "../../types/domains/user";

const COMMON_PATH = "/api/users";

type FetchRegisterRequest = Omit<User, "id"> & {
  confirmPassword: string;
  authenticationCode: string;
};

type FetchRegisterResponseData = ResponseDataWithMessage<string>;

type FetchLoginRequest = Pick<User, "email" | "password">;

type FetchLoginResponseData = ResponseDataWithMessage<string>;

type FetchPasswordFindRequest = Pick<User, "name" | "email" | "password" | "birthday">;

type FetchPasswordFindResponseData = null;

type FetchPasswordEditRequest = RequestWithToken<{
  oldPassword: string;
  password: string;
  confirmPassword: string;
}>;

type FetchPasswordEditResponseData = null;

type FetchUserInfoRequest = RequestWithToken;

type FetchUserInfoResponseData = ResponseDataWithMessage<Omit<User, "password">>;

type FetchUserInfoEditRequest = RequestWithToken<{ phoneNumber: string }>;

type FetchUserInfoEditResponseData = null;

type FetchAuthenticationCodeRequest = string;

type FetchAuthenticationCodeResponseData = null;

type FetchVerifyAuthenticationCodeRequest = {
  email: string;
  authenticationCode: string;
};

type FetchVerifyAuthenticationCodeResponseData = null;

export const fetchRegister = ({
  name,
  email,
  phoneNumber,
  gender,
  birthday,
  password,
  confirmPassword,
  authenticationCode,
}: FetchRegisterRequest) =>
  axios.post<FetchRegisterResponseData>(`${COMMON_PATH}/register`, {
    name,
    email,
    phoneNumber,
    gender,
    birthday: formatDate(birthday),
    password,
    confirmPassword,
    authenticationCode,
  });

export const fetchLogin = ({ email, password }: FetchLoginRequest) =>
  axios.post<FetchLoginResponseData>(`${COMMON_PATH}/login`, { email, password });

export const fetchPasswordFind = ({ name, email, birthday }: FetchPasswordFindRequest) =>
  axios.post<FetchPasswordFindResponseData>(`${COMMON_PATH}/reset-password`, {
    name,
    email,
    birthday: formatDate(birthday),
  });

export const fetchPasswordEdit = ({
  token,
  oldPassword,
  password,
  confirmPassword,
}: FetchPasswordEditRequest) =>
  axios.post<FetchPasswordEditResponseData>(
    `${COMMON_PATH}/edit-password`,
    { oldPassword, password, confirmPassword },
    headers({ token })
  );

export const fetchUserInfo = ({ token }: FetchUserInfoRequest) =>
  axios.get<FetchUserInfoResponseData>(`${COMMON_PATH}/me`, headers({ token }));

export const fetchUserInfoEdit = ({ token, phoneNumber }: FetchUserInfoEditRequest) =>
  axios.patch<FetchUserInfoEditResponseData>(
    `${COMMON_PATH}/information`,
    { phoneNumber },
    headers({ token })
  );

export const fetchAuthenticationCode = (email: FetchAuthenticationCodeRequest) =>
  axios.post<FetchAuthenticationCodeResponseData>(
    `${COMMON_PATH}/authentication-code?email=${email}`
  );

export const fetchVerifyAuthenticationCode = ({
  email,
  authenticationCode,
}: FetchVerifyAuthenticationCodeRequest) =>
  axios.post<FetchVerifyAuthenticationCodeResponseData>(
    `${COMMON_PATH}/authenticate-email?email=${email}&authenticationCode=${authenticationCode}`
  );
