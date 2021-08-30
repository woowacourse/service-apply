const REGEX = /^\d{2,3}-\d{3,4}-\d{4}$/;

export const isValidPhoneNumber = (v) => REGEX.test(v);
