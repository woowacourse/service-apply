const REGEX = /^010-\d{4}-\d{4}$/;

export const isValidPhoneNumber = (v) => REGEX.test(v);
