const REGEX = /^[가-힣]+$/;

export const isValidName = (v: string) => REGEX.test(v);
