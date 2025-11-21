const REGEX = /^\+[1-9]\d{1,14}$/;

export const isValidPhoneNumber = (v: string) => REGEX.test(v);
