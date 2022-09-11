import axios from "axios";
import { headers } from "./api";
import { formatDate } from "../utils/format/date";
import { RequestWithToken, ResponseDataWithMessage } from "../../types/utility";
import { User } from "../../types/domains/user";

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
  axios.post<FetchRegisterResponseData>("/api/users/register", {
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
  axios.post<FetchLoginResponseData>("/api/users/login", { email, password });

export const fetchPasswordFind = ({ name, email, birthday }: FetchPasswordFindRequest) =>
  axios.post<FetchPasswordFindResponseData>("/api/users/reset-password", {
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
    "/api/users/edit-password",
    { oldPassword, password, confirmPassword },
    headers({ token })
  );

export const fetchUserInfo = ({ token }: FetchUserInfoRequest) =>
  axios.get<FetchUserInfoResponseData>("/api/users/me", headers({ token }));

export const fetchUserInfoEdit = ({ token, phoneNumber }: FetchUserInfoEditRequest) =>
  axios.patch<FetchUserInfoEditResponseData>(
    "/api/users/information",
    { phoneNumber },
    headers({ token })
  );

export const fetchAuthenticationCode = (email: FetchAuthenticationCodeRequest) =>
  axios.post<FetchAuthenticationCodeResponseData>(`/api/users/authentication-code?email=${email}`);

export const fetchVerifyAuthenticationCode = ({
  email,
  authenticationCode,
}: FetchVerifyAuthenticationCodeRequest) =>
  axios.post<FetchVerifyAuthenticationCodeResponseData>(
    `/api/users/authenticate-email?email=${email}&authenticationCode=${authenticationCode}`
  );
