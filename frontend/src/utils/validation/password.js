import { ERROR_MESSAGE } from "../../constants/messages";

const REGEX =
  /^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[*.!@#$%^&(){}[\]:;"'<>,?\\/~_+\-=|]).{8,20}$/;

const isValidPassword = (v) => REGEX.test(v);

export const validatePassword = (v) => {
  if (!isValidPassword(v)) throw new Error(ERROR_MESSAGE.VALIDATION.PASSWORD);
};

export const validateRePassword =
  (v) =>
  ({ password }) => {
    if (v !== password) throw new Error(ERROR_MESSAGE.VALIDATION.RE_PASSWORD);
  };
