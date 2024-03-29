export type RequestWithToken<T = {}> = { token: string } & T;

export type ResponseDataWithMessage<T> = {
  message: string;
  body: T;
};

export type ValueOf<T> = T[keyof T];
