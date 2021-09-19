import { ERROR_MESSAGE } from "../../constants/messages";

const REGEX = /^\d{2,3}-\d{3,4}-\d{4}$/;

const isValidPhoneNumber = (v) => REGEX.test(v);

export const validatePhoneNumber = (v) => {
  if (!isValidPhoneNumber(v))
    throw new Error(ERROR_MESSAGE.VALIDATION.PHONE_NUMBER);
};
