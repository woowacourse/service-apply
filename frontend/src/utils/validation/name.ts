const REGEX = /^[a-zA-Z가-힣'\-\s]{1,60}$/;

export const isValidName = (v: string) => REGEX.test(v);
