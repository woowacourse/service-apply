const REGEX = /^[가-힣]+$/;

export const isValidName = (v) => REGEX.test(v);
