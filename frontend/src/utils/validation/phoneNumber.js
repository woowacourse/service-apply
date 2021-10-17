import { ERROR_MESSAGE } from "../../constants/messages";

export const PHONE_NUMBER_REGEX = /010-\d{4}-\d{4}/;

export const isValidPhoneNumber = (v) => PHONE_NUMBER_REGEX.test(v);

export const validatePhoneNumber = (v) => {
  if (!isValidPhoneNumber(v))
    throw new Error(ERROR_MESSAGE.VALIDATION.PHONE_NUMBER);
};
