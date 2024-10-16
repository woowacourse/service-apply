import axios from "axios";
import { headers } from "./api";
import { formatDate } from "../utils/format/date";
import { RequestWithToken } from "../../types/utility";
import { Member } from "../../types/domains/member";

type FetchRegisterRequest = Omit<Member, "id"> & {
  confirmPassword: string;
  authenticationCode: string;
};

type FetchRegisterResponseData = string;

type FetchLoginRequest = Pick<Member, "email" | "password">;

type FetchLoginResponseData = string;

type FetchPasswordFindRequest = Pick<Member, "name" | "email" | "password" | "birthday">;

type FetchPasswordFindResponseData = void;

type FetchPasswordEditRequest = RequestWithToken<{
  oldPassword: string;
  password: string;
  confirmPassword: string;
}>;

type FetchPasswordEditResponseData = void;

type FetchMemberInfoRequest = RequestWithToken;

type FetchMemberInfoResponseData = Omit<Member, "password">;

type FetchMemberInfoEditRequest = RequestWithToken<{ phoneNumber: string }>;

type FetchMemberInfoEditResponseData = void;

type FetchAuthenticationCodeRequest = string;

type FetchAuthenticationCodeResponseData = void;

type FetchVerifyAuthenticationCodeRequest = {
  email: string;
  authenticationCode: string;
};

type FetchVerifyAuthenticationCodeResponseData = void;

export const fetchRegister = ({
  email,
  password,
  confirmPassword,
  name,
  birthday,
  phoneNumber,
  githubUsername,
  authenticationCode,
}: FetchRegisterRequest) =>
  axios.post<FetchRegisterResponseData>("/api/members/register", {
    email,
    password,
    confirmPassword,
    name,
    birthday: formatDate(birthday),
    phoneNumber,
    githubUsername,
    authenticationCode,
  });

export const fetchLogin = ({ email, password }: FetchLoginRequest) =>
  axios.post<FetchLoginResponseData>("/api/members/login", { email, password });

export const fetchPasswordFind = ({ name, email, birthday }: FetchPasswordFindRequest) =>
  axios.post<FetchPasswordFindResponseData>("/api/members/reset-password", {
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
    "/api/members/edit-password",
    { oldPassword, password, confirmPassword },
    headers({ token })
  );

export const fetchMemberInfo = ({ token }: FetchMemberInfoRequest) =>
  axios.get<FetchMemberInfoResponseData>("/api/members/me", headers({ token }));

export const fetchMemberInfoEdit = ({ token, phoneNumber }: FetchMemberInfoEditRequest) =>
  axios.patch<FetchMemberInfoEditResponseData>(
    "/api/members/information",
    { phoneNumber },
    headers({ token })
  );

export const fetchAuthenticationCode = (email: FetchAuthenticationCodeRequest) =>
  axios.post<FetchAuthenticationCodeResponseData>(
    `/api/members/authentication-code?email=${email}`
  );

export const fetchVerifyAuthenticationCode = ({
  email,
  authenticationCode,
}: FetchVerifyAuthenticationCodeRequest) =>
  axios.post<FetchVerifyAuthenticationCodeResponseData>(
    `/api/members/authenticate-email?email=${email}&authenticationCode=${authenticationCode}`
  );

export const fetchWithdraw = ({ token, password }: RequestWithToken<{ password: string }>) =>
  axios.delete<void>("/api/members/withdraw", { ...headers({ token }), data: { password } });
