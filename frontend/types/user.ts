import { RequestWithToken, ResponseDataWithMessage } from "./utility";

export type User = {
  id: number;
  name: string;
  email: string;
  phoneNumber: string;
  gender: "MALE" | "FEMALEE";
  birthday: Date;
  password: string;
};

export type FetchRegisterRequest = Omit<User, "id"> & {
  confirmPassword: string;
  authenticationCode: string;
};

export type FetchRegisterResponseData = ResponseDataWithMessage<string>;

export type FetchLoginRequest = Pick<User, "email" | "password">;

export type FetchLoginResponseData = ResponseDataWithMessage<string>;

export type FetchPasswordFindRequest = Pick<User, "name" | "email" | "password" | "birthday">;

export type FetchPasswordFindResponseData = never;

export type FetchPasswordEditRequest = RequestWithToken<{
  oldPassword: string;
  password: string;
  confirmPassword: string;
}>;

export type FetchPasswordEditResponseData = never;

export type FetchUserInfoRequest = RequestWithToken;

export type FetchUserInfoResponseData = ResponseDataWithMessage<Omit<User, "password">>;

export type FetchUserInfoEditRequest = RequestWithToken<{ phoneNumber: string }>;

export type FetchUserInfoEditResponseData = never;

export type FetchAuthenticationCodeRequest = string;

export type FetchAuthenticationCodeResponseData = never;

export type FetchVerifyAuthenticationCodeRequest = {
  email: string;
  authenticationCode: string;
};

export type FetchVerifyAuthenticationCodeResponseData = never;
