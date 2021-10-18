import { ERROR_MESSAGE } from "../../constants/messages";

const REGEX = /^010-\d{4}-\d{4}$/;

export const isValidPhoneNumber = (v) => REGEX.test(v);

export const validatePhoneNumber = (v) => {
  if (!isValidPhoneNumber(v)) throw new Error(ERROR_MESSAGE.VALIDATION.PHONE_NUMBER);
};
