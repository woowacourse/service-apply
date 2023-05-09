export type RequestWithToken<T = {}> = { token: string } & T;

export type ValueOf<T> = T[keyof T];
